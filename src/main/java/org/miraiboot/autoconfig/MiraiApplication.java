package org.miraiboot.autoconfig;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.entity.ConfigFile;
import org.miraiboot.listener.GroupListener;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerUtil;
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

    // 开始自动包扫描
    String packageName = target.getPackageName();
    List<Class<?>> classHasAnnotation = GlobalLoader.getClassHasAnnotation(EventHandlerComponent.class, packageName);
    if (classHasAnnotation != null && !classHasAnnotation.isEmpty()) {
      for (Class<?> aClass : classHasAnnotation) {
        for (Method method : aClass.getMethods()) {
          if (method.isAnnotationPresent(EventHandler.class)) {
            EventHandler methodAnnotation = method.getAnnotation(EventHandler.class);
            String targetName = methodAnnotation.target();
            if (targetName.equals("")) targetName = method.getName();
            EventHandlerUtil.getInstance().on(targetName, aClass, method);
          }
        }
      }
    }

    GlobalLoader.init(target);
    Scanner scanner = new Scanner(System.in);
    Bot bean = BotFactory.INSTANCE.newBot(1741557205L, "Hunter28", botConfiguration -> {
      botConfiguration.fileBasedDeviceInfo("device.json");
      botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
      botConfiguration.noNetworkLog();
    });
    bean.getEventChannel().subscribeAlways(GroupMessageEvent.class, new GroupListener());
    bean.login();
    while (true) {
      String command = scanner.next();
      if (command.equals("exit")) {
        bean.close();
        break;
      }
    }
  }
}
