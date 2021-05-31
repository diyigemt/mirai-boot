package net.diyigemt.miraiboot.constant;

import net.diyigemt.miraiboot.entity.EventExceptionHandlerDefault;
import net.diyigemt.miraiboot.entity.ExceptionHandlerItem;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 存储有关异常处理的常量
 * @since 1.0.5
 * @author diyigemt
 */
public final class ConstantException {
  static {
    ExceptionHandlerItem item = null;
    try {
      Method onException = EventExceptionHandlerDefault.class.getMethod("onException", InvocationTargetException.class, MessageEventPack.class, PreProcessorData.class);
      item = new ExceptionHandlerItem("", InvocationTargetException.class, EventExceptionHandlerDefault.class, onException, 0);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    DEFAULT_EXCEPTION_HANDLER = item;
  }
  public static final ExceptionHandlerItem DEFAULT_EXCEPTION_HANDLER;
  public static final int DEFAULT_PRIORITY = 0;
  public static final int MAX_PARAM_COUNT = 3;
  public static final String OTHER_EVENT_EXCEPTION_HANDLER_NAME = "other_exception_handler";
}
