package org.miraiboot.autoconfig;

import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.FileUtil;

import java.io.File;
import java.io.InputStream;

public class MiraiApplication {
  public static void run(Class<?> target, String... args) {
    // TODO AutoConfiguration
    // 打印描述
    MiraiBootApplication miraiBootApplication = target.getAnnotation(MiraiBootApplication.class);
    MiraiMain.logger.info(miraiBootApplication.description());
    // 尝试读取配置文件
    MiraiMain.logger.info("开始读取配置文件");
    if (!FileUtil.getInstance().checkConfigFileExist(target)) {
      MiraiMain.logger.info("未找到配置文件, 请自行在新创建的配置文件中修改");
      if (!FileUtil.getInstance().createConfigFile(target)) {
        MiraiMain.logger.warning("配置文件创建失败");
      }
      return;
    }
//    ConfigFile configFile = new Yaml().loadAs(resourceAsStream, ConfigFile.class);
//
//    // 开始自动包扫描
//    String packageName = target.getPackageName();
//    List<Class<?>> classHasAnnotation = GlobalLoader.getClassHasAnnotation(EventHandlerComponent.class, packageName);
//    if (classHasAnnotation != null && !classHasAnnotation.isEmpty()) {
//      for (Class<?> aClass : classHasAnnotation) {
//        for (Method method : aClass.getMethods()) {
//          if (method.isAnnotationPresent(EventHandler.class)) {
//            EventHandler methodAnnotation = method.getAnnotation(EventHandler.class);
//            String targetName = methodAnnotation.target();
//            if (targetName.equals("")) targetName = method.getName();
//            EventHandlerUtil.getInstance().on(targetName, aClass, method);
//          }
//        }
//      }
//    }
//
//    GlobalLoader.init();
//    Scanner scanner = new Scanner(System.in);
//    Bot bean = BotFactory.INSTANCE.newBot(1741557205L, "Hunter28", botConfiguration -> {
//      botConfiguration.fileBasedDeviceInfo("device.json");
//      botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
//      botConfiguration.noNetworkLog();
//    });
//    bean.getEventChannel().subscribeAlways(GroupMessageEvent.class, new GroupListener());
//    bean.login();
//    while (true) {
//      String command = scanner.next();
//      if (command.equals("exit")) {
//        bean.close();
//        break;
//      }
//    }
  }
}
