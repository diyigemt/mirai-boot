package net.diyigemt.miraiboot.autoconfig;

import net.diyigemt.miraiboot.annotation.*;
import net.diyigemt.miraiboot.constant.ConstantGlobal;
import net.diyigemt.miraiboot.constant.EventHandlerType;
import net.diyigemt.miraiboot.constant.FunctionId;
import net.diyigemt.miraiboot.core.MiraiBootConsole;
import net.diyigemt.miraiboot.core.PluginMgr;
import net.diyigemt.miraiboot.core.RegisterProcess;
import net.diyigemt.miraiboot.dao.PermissionDAO;
import net.diyigemt.miraiboot.entity.*;
import net.diyigemt.miraiboot.function.Alias;
import net.diyigemt.miraiboot.function.console.ConsoleExit;
import net.diyigemt.miraiboot.listener.BotEventListener;
import net.diyigemt.miraiboot.listener.MessageEventListener;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.permission.AuthMgr;
import net.diyigemt.miraiboot.permission.CheckPermission;
import net.diyigemt.miraiboot.utils.*;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h2>主实现逻辑</h2>
 * @author diyigemt
 * @since 1.0.0
 */
public class MiraiApplication {

  public static ConfigFile config;
  public static void run(Class<?> mainClass, String... args) {
    // 打印banner
    InputStream banner = mainClass.getResourceAsStream("/banner.txt");
    if (banner != null) {
      BufferedReader bannerReader = new BufferedReader(new InputStreamReader(banner));
      String line;
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
    classes.add(Alias.class);
    classes.add(AuthMgr.class);
    // 初始化permission数据库
    classes.add(PermissionDAO.class);
    // 初始化自带控制台指令
    List<Class<?>> consoleCommand = GlobalLoader.getClasses(ConsoleExit.class.getPackageName());
    classes.addAll(consoleCommand);
    classes.addAll(PluginLoader.getPluginClasses(mainClass));
    // 开始处理事件handler和autoInit
    List<AutoInitItem> inits = RegisterProcess.AnnotationScanner(classes);
    //事件注册完成，释放所有List
    classes.clear();
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
    inits.clear();//初始化完成后释放所有记载
    System.gc();//清理插件加载时因各种失败而变得无用的class
    // 初始化完成 统一登录
    MiraiMain.logger.info("初始化完成 开始登录bot");
    BotManager.getInstance().loginAll();
    MiraiMain.logger.info("bot登录成功 系统启动完成");
    // 阻塞主线程
//    Map<String, List<PluginItem>> text = PluginMgr.manifests;
//    int i = 0;
    MiraiBootConsole.getInstance().listenLoop();
  }
}
