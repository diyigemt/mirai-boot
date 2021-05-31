package net.diyigemt.miraiboot.entity;

import net.diyigemt.miraiboot.utils.ExceptionHandlerManager;

import java.lang.reflect.InvocationTargetException;

/**
 * <h2>默认事件异常处理器</h2>
 */
public final class EventExceptionHandlerDefault {
  public void onException(InvocationTargetException e, MessageEventPack eventPack, PreProcessorData<?> data) {
    boolean res = ExceptionHandlerManager.getInstance().emit(e, eventPack, data);
    if (!res) {
      e.printStackTrace();
    }
  }
}
