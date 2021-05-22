package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

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
