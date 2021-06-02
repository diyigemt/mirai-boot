package net.diyigemt.miraiboot.exception;

import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;

import java.lang.reflect.InvocationTargetException;

/**
 * <h2>消息事件处理器本身引发的异常</h2>
 * @since 1.0.5
 */
public class EventHandlerInvokeException extends RuntimeException {
  private final InvocationTargetException targetException;
  private final MessageEventPack eventPack;
  private final PreProcessorData<?> data;

  public EventHandlerInvokeException(MessageEventPack eventPack, PreProcessorData<?> data, InvocationTargetException targetException) {
    this.eventPack = eventPack;
    this.data = data;
    this.targetException = targetException;
  }

  /**
   * 获取invocation异常本身
   * @return 异常本身
   */
  public InvocationTargetException getTargetException() {
    return targetException;
  }

  /**
   * 获取引发异常的事件
   * @return 事件
   */
  public MessageEventPack getEventPack() {
    return eventPack;
  }

  /**
   * 获取异常事件的预处理数据
   * @return 数据
   */
  public PreProcessorData<?> getData() {
    return data;
  }

}
