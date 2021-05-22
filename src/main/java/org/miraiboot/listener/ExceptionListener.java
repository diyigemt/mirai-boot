package org.miraiboot.listener;


import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.ExceptionHandlerManager;

/**
 * <h2>异常监听器</h2>
 * 触发关键词为异常的全类名
 * @author diyigemt
 * @since 1.0.0
 */
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
