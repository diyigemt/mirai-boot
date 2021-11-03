package net.diyigemt.miraiboot.entity;

import lombok.Getter;
import net.diyigemt.miraiboot.annotation.TimerHandler;

import java.lang.reflect.Method;

/**
 * <h2>TimerHandler信息存储类</h2>
 * @see TimerHandler
 * @author diyigemt
 * @since 1.0.7
 */
@Getter
public final class TimerItem extends MiraiBootHandlerItem {

  /**
   * 执行周期,对应注解的value
   */
  private final String period;

  public TimerItem(String name, Class<?> invoker, Method handler, String period) {
    super(name, invoker, handler);
    this.period = period;
  }
}
