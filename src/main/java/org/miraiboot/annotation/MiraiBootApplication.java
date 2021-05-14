package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * 开玩笑的 没什么用
 * @author diyigemt
 * @since 3.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiraiBootApplication {
  String description() default "";
}
