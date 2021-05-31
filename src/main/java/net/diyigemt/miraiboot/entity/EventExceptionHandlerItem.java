package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.diyigemt.miraiboot.constant.ConstantException;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>与事件处理器关联的异常处理器</h2>
 * @since 1.0.5
 */
@Data
public class EventExceptionHandlerItem {
  private final List<ExceptionHandlerItem> handlers;
  private final ExceptionHandlerItem mainHandler;

  public EventExceptionHandlerItem(List<ExceptionHandlerItem> handlers, ExceptionHandlerItem mainHandler) {
    this.handlers = handlers == null ? new ArrayList<>() : handlers;
    this.mainHandler = mainHandler == null ? ConstantException.DEFAULT_EXCEPTION_HANDLER : null;
  }
}
