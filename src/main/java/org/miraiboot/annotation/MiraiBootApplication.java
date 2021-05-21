package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * 开玩笑的 没什么用
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiraiBootApplication {
  /**
   * 初始化时控制台打印信息 将会在banner后打印
   */
  String description() default "";
}
