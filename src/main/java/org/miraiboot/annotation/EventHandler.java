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
  /**
   * 是否为常驻消息处理器<br/>
   * 设置为true时<strong>无论如何</strong>该消息处理器均会触发
   */
  boolean isAny() default false;
  /**
   * 匹配的指令开头 为空表示忽略<br/>
   * 如:@MessageEventFilter(start = "/")
   * 表示将从 '/'之后的内容获取指令名和参数<br/>
   * 如:消息 '一些乱七八糟的内容 /help asd'<br/>
   * 将会获取到指令 help 和参数 asd
   */
  String start() default "";
  /**
   * <h3>参数分隔符 用于获取指令之后的文本参数</h3>
   * 值为正则表达式 默认使用'\\s+' 即以至少1个空格为分割<br/>
   * 如:@MessageEventFilter(start = "/", split = "\\s+")<br/>
   * 处理消息 '一些乱七八糟的内容 /help asd    ssd'<br/>
   * 将会得到 指令 help 和参数 asd与ssd
   */
  String split() default "\\s+";
  /**
   * 触发handler的指令 使用默认时取受到注解的方法名
   */
  String target() default "";
  /**
   * 处理类型 群消息或好友消息
   */
  EventHandlerType[] type() default MESSAGE_HANDLER_ALL;
}
