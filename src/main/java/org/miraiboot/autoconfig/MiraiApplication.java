package org.miraiboot.autoconfig;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.entity.ConfigFile;
import org.miraiboot.entity.ConfigFileBot;
import org.miraiboot.entity.ConfigFileBotConfiguration;
import org.miraiboot.listener.EventListener;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.BotManager;
import org.miraiboot.utils.EventHandlerManager;
import org.miraiboot.utils.FileUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

public class MiraiApplication {
  public static void run(Class<?> target, String... args) {
    // TODO AutoConfiguration
    // 打印描述
    MiraiBootApplication miraiBootApplication = target.getAnnotation(MiraiBootApplication.class);
    if (miraiBootApplication == null) {
      MiraiMain.logger.error("没有找到主类! 请将主类加上@MiraiBootApplication注解");
      return;
    }
    MiraiMain.logger.info(miraiBootApplication.description());
    // 初始化实现了InitializeUtil接口的工具类
    GlobalLoader.initUtil(target);
    // 尝试读取配置文件
    MiraiMain.logger.info("开始读取配置文件");
    File configFile = FileUtil.getInstance().getConfigFile(target);
    if (configFile == null) {
      MiraiMain.logger.warning("未找到配置文件, 请自行在新创建的配置文件中修改");
      configFile = FileUtil.getInstance().createConfigFile(target);
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
    }

    // 开始自动包扫描 注册event handler
    String packageName = target.getPackageName();
    List<Class<?>> classHasAnnotation = GlobalLoader.getClassHasAnnotation(EventHandlerComponent.class, packageName);
    if (classHasAnnotation != null && !classHasAnnotation.isEmpty()) {
      for (Class<?> aClass : classHasAnnotation) {
        for (Method method : aClass.getMethods()) {
          if (method.isAnnotationPresent(EventHandler.class)) {
            EventHandler methodAnnotation = method.getAnnotation(EventHandler.class);
            String targetName = methodAnnotation.target();
            if (targetName.equals("")) targetName = method.getName();
            EventHandlerManager.getInstance().on(targetName, aClass, method);
          }
        }
      }
    }
    Scanner scanner = new Scanner(System.in);
    final boolean isNetwork = config.getMiraiboot().getLogger().isNetwork();
    // 注册Bot
    for (ConfigFileBot configFileBot : config.getMiraiboot().getBots()) {
      long account = configFileBot.getAccount();
      if (account == 123L) continue;
      String value = configFileBot.getPassword().getValue();
      if (value.equals("pwd")) continue;
      Bot bot = BotFactory.INSTANCE.newBot(account, value, botConfiguration -> {
        ConfigFileBotConfiguration configuration = configFileBot.getConfiguration();
        botConfiguration.fileBasedDeviceInfo(configuration.getDevice());
        botConfiguration.setProtocol(configuration.getProtocol());
        if (!isNetwork) {
          botConfiguration.noNetworkLog();
        }
      });
      // 注册统一的事件监听器
      bot.getEventChannel().subscribeAlways(MessageEvent.class, new EventListener());
      BotManager.getInstance().register(configFileBot.getAccount(), bot);
    }
    // 注册完成 统一登录
    BotManager.getInstance().loginAll();
    // 阻塞主线程
    while (true) {
      String command = scanner.next();
      if (command.equals("exit")) {
        BotManager.getInstance().logoutAll();
        break;
      }
    }
  }
}
