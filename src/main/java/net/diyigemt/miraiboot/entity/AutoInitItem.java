package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.diyigemt.miraiboot.annotation.AutoInit;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * <h2>自动初始化信息存储类</h2>
 * @author diyigem
 * @see AutoInit
 */
@Data
public class AutoInitItem implements Comparable<AutoInitItem> {
  private final String name;
  private final int priority;
  private final Method handler;

  @Override
  public int compareTo(@NotNull AutoInitItem o) {
    return -Integer.compare(this.priority, o.priority);
  }
}
