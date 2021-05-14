package org.miraiboot.autoconfig;

import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MiraiApplication {
  public static void run(Class<? extends MainMiraiApplication> target, String... args) {
    // TODO AutoConfiguration
    //打印描述
    MiraiBootApplication miraiBootApplication = target.getAnnotation(MiraiBootApplication.class);
    MiraiMain.logger.info(miraiBootApplication.description());
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
    try {
      Method main = target.getMethod("run", args.getClass());
      main.invoke(target.getDeclaredConstructor().newInstance(), (Object) args);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
  }
}
