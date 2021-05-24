package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * <h2>异常处理信息存储类</h2>
 * <strong>优先级越高越先触发!!</strong>
 * @author diyigemt
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class ExceptionHandlerItem implements Comparable<ExceptionHandlerItem> {
  private String target;
  private Class<?> invoker;
  private Method handler;
  private int priority;

  @Override
  public int compareTo(@NotNull ExceptionHandlerItem o) {
    return -Integer.compare(priority, o.priority);
  }
}
