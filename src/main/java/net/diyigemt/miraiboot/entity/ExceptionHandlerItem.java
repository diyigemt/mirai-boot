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
public final class ExceptionHandlerItem implements Comparable<ExceptionHandlerItem> {
  private String name;
  private final Class<? extends Throwable> target;
  private final Class<?> invoker;
  private final Method handler;
  private final int priority;

  @Override
  public int compareTo(@NotNull ExceptionHandlerItem o) {
    return -Integer.compare(priority, o.priority);
  }

  public boolean check(Class<? extends Throwable> t) {
    return target.isAssignableFrom(t);
  }
}
