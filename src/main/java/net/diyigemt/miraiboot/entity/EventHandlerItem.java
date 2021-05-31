package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.constant.EventHandlerType;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <h2>EventHandler信息存储类</h2>
 * @see EventHandler
 * @author diyigemt
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class EventHandlerItem {
  private String target;
  private final Class<?> invoker;
  private final Method handler;
  private final EventHandlerType[] type;
  /**
   * 同一个类中处理异常的方法
   */
  private final List<ExceptionHandlerItem> exceptionHandlers;
}
