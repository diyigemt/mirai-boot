package org.miraiboot.autoconfig;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.AutoInit;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.constant.ConstantGlobal;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.dao.PermissionDAO;
import org.miraiboot.entity.ConfigFile;
import org.miraiboot.entity.ConfigFileBot;
import org.miraiboot.entity.ConfigFileBotConfiguration;
import org.miraiboot.entity.ConfigFileMain;
import org.miraiboot.function.TestAlias;
import org.miraiboot.listener.MessageEventListener;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.permission.AuthMgr;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.utils.*;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

public class MiraiApplication {
  public static void run(Class<?> mainClass, String... args) {
    // TODO AutoConfiguration
    // 打印描述
    MiraiBootApplication miraiBootApplication = mainClass.getAnnotation(MiraiBootApplication.class);
    if (miraiBootApplication == null) {
      MiraiMain.logger.error("没有找到主类! 请将主类加上@MiraiBootApplication注解");
      return;
    }
    MiraiMain.logger.info(miraiBootApplication.description());
    // 初始化FileUtil
    FileUtil.init(mainClass);
    // 尝试读取配置文件
    MiraiMain.logger.info("开始读取配置文件");
    File configFile = FileUtil.getInstance().getConfigFile(mainClass);
    if (configFile == null) {
      MiraiMain.logger.warning("未找到配置文件, 请自行在新创建的配置文件中修改");
      configFile = FileUtil.getInstance().createConfigFile(mainClass);
      if (configFile == null) {
        MiraiMain.logger.error("配置文件创建失败");
      }
      return;
    }
    ConfigFile config = null;
    try {
      config = new Yaml().loadAs(new InputStreamReader(new FileInputStream(configFile)), ConfigFile.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      MiraiMain.logger.error("配置文件读取出错");
      return;
    }
    // 注册全局配置
    GlobalConfig.getInstance().putAll(config.getMiraiboot().getConfigs());
    GlobalConfig.getInstance().init();
    MiraiMain.logger.info("配置文件读取成功");
    // 开始自动包扫描 注册event handler
    String packageName = mainClass.getPackageName();
    List<Class<?>> classes = GlobalLoader.getClasses(packageName);
    // 添加权限管理命令和别名命令
    classes.add(TestAlias.class);
    classes.add(AuthMgr.class);
    // 初始化permission数据库
    classes.add(PermissionDAO.class);
    // 开始处理事件handler
    if (!classes.isEmpty()) {
      for (Class<?> clazz : classes) {
        if (clazz.isAnnotationPresent(AutoInit.class)) {
          handleAutoInit(mainClass, clazz);
        }
        if (clazz.isAnnotationPresent(EventHandlerComponent.class)) {
          handleEventHandler(clazz);
        }
      }
    }
    // 事件注册完成 对正则进行编译
    CommandUtil.getInstance().compileCommandPattern();
    // 开始读取配置文件
    ConfigFileMain miraiboot = config.getMiraiboot();
    final boolean isNetwork = miraiboot.getLogger().isNetwork();
    // 设置事件监听的logger是否启用
    MessageEventListener.eventLoggerEnable = miraiboot.getLogger().isEventStatus();
    // 注册Bot
    for (ConfigFileBot configFileBot : miraiboot.getBots()) {
      long account = configFileBot.getAccount();
      if (account == 123L) continue;
      String value = configFileBot.getPassword().getValue();
      if (value.equals("pwd")) continue;
      Bot bot = BotFactory.INSTANCE.newBot(account, value, botConfiguration -> {
        ConfigFileBotConfiguration configuration = configFileBot.getConfiguration();
        botConfiguration.fileBasedDeviceInfo(FileUtil.getInstance().getBotDeviceFilePath(account, configuration.getDevice()));
        botConfiguration.setProtocol(configuration.getProtocol());
        if (!isNetwork) {
          botConfiguration.noNetworkLog();
        }
      });
      // 注册统一的事件监听器
      bot.getEventChannel().subscribeAlways(MessageEvent.class, new MessageEventListener());
      BotManager.getInstance().register(configFileBot.getAccount(), bot);
    }
    // 注册完成 统一登录
    MiraiMain.logger.info("初始化完成 开始登录bot");
    BotManager.getInstance().loginAll();
    MiraiMain.logger.info("bot登录成功 系统启动完成");
    // 注册配置文件中的指令别名
    EventHandlerManager.getInstance().registerAlias(miraiboot.getAlias());
    // 阻塞主线程
    Scanner scanner = new Scanner(System.in);
    while (true) {
      String command = scanner.next();
      if (command.equals("exit")) {
        EventHandlerManager.getInstance().cancelAll();
        BotManager.getInstance().logoutAll();
        break;
      }
    }
  }

  /**
   * 初始化带有@AutoInit的类
   * @param mainClass 主类
   * @param clazz 目标类
   */
  private static void handleAutoInit(Class<?> mainClass, Class<?> clazz) {
    try {
      Method init = clazz.getMethod("init", Class.class);
      init.invoke(null, mainClass);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将被@EventHandlerComponent注册的类<br/>
   * 中被@EventHandler注册的方法加入EventhendlerManager中统一管理
   * @param clazz 被@EventHandlerComponent注册的类
   */
  private static void handleEventHandler(Class<?> clazz) {
    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(EventHandler.class)) {
        EventHandler methodAnnotation = method.getAnnotation(EventHandler.class);
        // 注册强制触发EventHandler
        if (methodAnnotation.isAny()) {
          EventHandlerManager.getInstance().on("", clazz, method);
          continue;
        }
        String targetName = methodAnnotation.target();
        String start = methodAnnotation.start();
        if (targetName.equals("")) {
          targetName = method.getName();
        }
        if (start.equals("")) {
          Object o = GlobalConfig.getInstance().get(ConstantGlobal.DEFAULT_COMMAND_START);
          if (!o.toString().equals("")) targetName = o + targetName;
        } else {
          // 注册指令开头
          CommandUtil.getInstance().registerCommandStart(start);
        }
        // 注册与指令对应的权限id
        int permissionIndex = 0;
        if (method.isAnnotationPresent(CheckPermission.class)) {
          CheckPermission permission = method.getAnnotation(CheckPermission.class);
          permissionIndex = permission.permissionIndex();
        }
        FunctionId.put(targetName, permissionIndex);
        EventHandlerManager.getInstance().on(targetName, clazz, method);
      }
    }
  }
}
