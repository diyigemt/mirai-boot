package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * 消息事件预处理器组
 * @author diyigemt
 * @since 1.0.0
 * @see MessagePreProcessor
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessagePreProcessors {
  MessagePreProcessor[] value() default {};
}
