package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * 消息事件过滤器组
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
