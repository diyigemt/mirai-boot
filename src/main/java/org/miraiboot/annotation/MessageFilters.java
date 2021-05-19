package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * <h2>消息事件过滤器组</h2>
 * @author diyigemt
 * @since 1.0.0
 * @see MessageFilter
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageFilters {
  MessageFilter[] value() default {};
}
