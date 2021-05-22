package org.miraiboot.listener;


import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.ExceptionHandlerManager;

public class ExceptionListener implements Thread.UncaughtExceptionHandler {
  @Override
  public void uncaughtException(Thread t, Throwable e) {
    String canonicalName = e.getClass().getCanonicalName();
    String res = ExceptionHandlerManager.getInstance().emit(canonicalName, e);
    if (res != null) {
      MiraiMain.logger.error(res);
    }
  }
}
