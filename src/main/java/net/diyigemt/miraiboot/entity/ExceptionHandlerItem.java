package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * <h2>异常处理信息存储类</h2>
 * <strong>优先级越高越先触发!!</strong>
 * @author diyigemt
 * @since 1.0.0
 */
@Getter
public final class ExceptionHandlerItem extends MiraiBootHandlerItem implements Comparable<ExceptionHandlerItem> {
  private final Class<? extends Throwable> target;
  private final int priority;

  public ExceptionHandlerItem(String name, Class<?> invoker, Method handler, Class<? extends Throwable> target, int priority) {
    super(name, invoker, handler);
    this.target = target;
    this.priority = priority;
  }

  @Override
  public int compareTo(@NotNull ExceptionHandlerItem o) {
    return -Integer.compare(priority, o.priority);
  }

  public boolean check(Class<? extends Throwable> t) {
    return target.isAssignableFrom(t);
  }
}
