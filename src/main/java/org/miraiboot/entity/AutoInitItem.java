package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoInitItem implements Comparable<AutoInitItem> {
  private int priority;
  private Method handler;

  @Override
  public int compareTo(@NotNull AutoInitItem o) {
    return -Integer.compare(this.priority, o.priority);
  }
}
