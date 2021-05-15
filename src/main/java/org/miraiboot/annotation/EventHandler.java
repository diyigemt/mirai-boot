package org.miraiboot.annotation;

import org.miraiboot.constant.EventHandlerType;

import java.lang.annotation.*;

import static org.miraiboot.constant.EventHandlerType.MESSAGE_HANDLER_ALL;


/**
 * 将受到注解的方法注册为事件handler
 * @author diyigemt
 * @since 3.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandler {
  // 触发handler的命令 使用默认时取受到注解的方法名
  String target() default "";
  // 处理类型 群消息或好友消息
  EventHandlerType[] type() default MESSAGE_HANDLER_ALL;
}
