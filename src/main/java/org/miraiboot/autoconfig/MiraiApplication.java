package org.miraiboot.autoconfig;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.*;
import org.miraiboot.constant.ConstantGlobal;
import org.miraiboot.constant.EventHandlerType;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.dao.PermissionDAO;
import org.miraiboot.entity.*;
import org.miraiboot.function.TestAlias;
import org.miraiboot.listener.BotEventListener;
import org.miraiboot.listener.ExceptionListener;
import org.miraiboot.listener.MessageEventListener;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.permission.AuthMgr;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.utils.*;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h2>主实现逻辑</h2>
 * @author diyigemt
 * @since 1.0.0
 */
public class MiraiApplication {
  public static void run(Class<?> mainClass, String... args) {
    // 打印banner
    InputStream banner = mainClass.getResourceAsStream("/banner.txt");
    if (banner != null) {
      BufferedReader bannerReader = new BufferedReader(new InputStreamReader(banner));
      String line = null;
      try {
        while ((line = bannerReader.readLine()) != null) {
          System.out.println(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          banner.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    // 注册全局Exception Handler
    Thread.currentThread().setUncaughtExceptionHandler(new ExceptionListener());
    // AutoConfiguration
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
    File configFile = FileUtil.getInstance().getConfigFile();
    if (configFile == null) {
      MiraiMain.logger.warning("未找到配置文件, 请自行在新创建的配置文件中修改");
      configFile = FileUtil.getInstance().createConfigFile();
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
    // 开始处理事件handler和autoInit
    List<AutoInitItem> inits = new ArrayList<AutoInitItem>();
    if (!classes.isEmpty()) {
      for (Class<?> clazz : classes) {
        if (clazz.isAnnotationPresent(AutoInit.class)) {
          handleAutoInit(clazz, inits);
        }
        if (clazz.isAnnotationPresent(EventHandlerComponent.class)) {
          handleEventHandler(clazz);
        }
        if (clazz.isAnnotationPresent(ExceptionHandlerComponent.class)) {
          handleExceptionHandler(clazz);
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
    BotEventListener.eventLoggerEnable = miraiboot.getLogger().isEventStatus();
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
      bot.getEventChannel().subscribeAlways(BotEvent.class, new BotEventListener());
      bot.getEventChannel().subscribeAlways(MessageEvent.class, new MessageEventListener());
      BotManager.getInstance().register(configFileBot.getAccount(), bot);
    }
    // 注册配置文件中的指令别名
    EventHandlerManager.getInstance().registerAlias(miraiboot.getAlias());
    // 开始自动初始化
    Collections.sort(inits);
    for (AutoInitItem item : inits) {
      int parameterCount = item.getHandler().getParameterCount();
      Object[] param = null;
      if (parameterCount != 0) {
        param = new Object[parameterCount];
        param[0] = config;
      }
      try {
        if (parameterCount == 0) {
          item.getHandler().invoke(null);
        } else {
          item.getHandler().invoke(null, param);
        }
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    // 初始化完成 统一登录
    MiraiMain.logger.info("初始化完成 开始登录bot");
    BotManager.getInstance().loginAll();
    MiraiMain.logger.info("bot登录成功 系统启动完成");
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
   * 初始化被@AutoInit注释的类
   * @param res 暂存 等候排序
   * @param clazz 目标类
   */
  private static void handleAutoInit(Class<?> clazz, List<AutoInitItem> res) {
    try {
      Method init = clazz.getMethod("init", ConfigFile.class);
      AutoInit annotation = clazz.getAnnotation(AutoInit.class);
      AutoInitItem item = new AutoInitItem(annotation.value(), init);
      res.add(item);
    } catch (NoSuchMethodException e) {
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
      if (!method.isAnnotationPresent(EventHandler.class)) continue;
      EventHandler methodAnnotation = method.getAnnotation(EventHandler.class);
      // 注册其他事件Handler
      AtomicBoolean b = new AtomicBoolean(false);
      EventHandlerType[] types = methodAnnotation.type();
      for (EventHandlerType type : types) {
        if (type == EventHandlerType.OTHER_HANDLER) {
          EventHandlerManager.getInstance().onOther("", clazz, method);
          b.set(true);
          break;
        }
      }
      // 如果注册为BotEventHandler 将不能被注册为消息事件Handler
      if (b.get()) continue;
      // 注册强制触发EventHandler
      if (methodAnnotation.isAny()) {
        EventHandlerManager.getInstance().onAny(clazz, method);
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
        targetName = start + targetName;
        // 注册指令开头
        CommandUtil.getInstance().registerCommandStart(start);
      }
      // 注册与指令对应的权限id
      EventHandlerComponent classAnnotation = clazz.getAnnotation(EventHandlerComponent.class);
      int permissionIndex = classAnnotation.value();
      if (method.isAnnotationPresent(CheckPermission.class)) {
        CheckPermission permission = method.getAnnotation(CheckPermission.class);
        permissionIndex = permission.FunctionID() == 0 ? permissionIndex : permission.FunctionID();
      }
      FunctionId.put(targetName, permissionIndex);
      EventHandlerManager.getInstance().on(targetName, clazz, method);
    }
  }
  private static void handleExceptionHandler(Class<?> clazz) {
    ExceptionHandlerComponent classAnnotation = clazz.getAnnotation(ExceptionHandlerComponent.class);
    int classPriority = classAnnotation.value();
    for (Method method : clazz.getMethods()) {
      if (!method.isAnnotationPresent(ExceptionHandler.class)) continue;
      ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);
      Class<? extends Exception>[] targets = annotation.targets();
      if (targets.length == 0) return;
      if (!method.isAnnotationPresent(ExceptionHandler.class)) continue;
      // 检查返回值类型
      Class<?> returnType = method.getReturnType();
      if (!(returnType == void.class || returnType == boolean.class)) continue;
      int priority = annotation.priority();
      if (priority == 0 && classPriority != 0) priority = classPriority;
      for (Class<? extends Exception> c : targets) {
        String target = c.getCanonicalName();
        ExceptionHandlerManager.getInstance().on(target, clazz, method, priority);
      }
    }
  }
}
