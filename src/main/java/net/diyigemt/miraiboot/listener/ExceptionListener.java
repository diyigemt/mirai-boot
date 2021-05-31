package net.diyigemt.miraiboot.listener;


import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.ExceptionHandlerManager;

/**
 * <h2>异常监听器</h2>
 * 触发关键词为异常的全类名
 * @author diyigemt
 * @since 1.0.0
 */
public class ExceptionListener implements Thread.UncaughtExceptionHandler {
  @Override
  public void uncaughtException(Thread t, Throwable e) {
    boolean res = ExceptionHandlerManager.getInstance().emit(e);
    if (!res) e.printStackTrace();
  }
}
