package net.diyigemt.miraiboot.annotation;

import net.diyigemt.miraiboot.constant.MessageFilterMatchType;

import java.lang.annotation.*;

/**
 * <h2>消息事件过滤器</h2>
 * 在所有规则通过时EventHandler才会被触发<br/>
 * 其实和@EventHandler有点重复了<br/>
 * 可以重复注解 此时将会对所有进行匹配 (更多匹配规则在做了)
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MessageFilters.class)
@Documented
public @interface MessageFilter {

  /**
   * 匹配关键词的内容 为空表示忽略
   * @since 1.0.0
   */
  String value() default "";
  /**
   * 匹配类型  详见MessageEventFilterMatchType
   * @see MessageFilterMatchType
   * @since 1.0.0
   */
  MessageFilterMatchType matchType() default MessageFilterMatchType.NULL;
  /**
   * 匹配的账号列表
   * 若消息发送着不在列表内则不做响应
   * 为空则允许所有
   * @since 1.0.0
   */
  String[] accounts() default {};
  /**
   * 匹配的群列表
   * 若消息发送着不在列表内则不做响应
   * 为空则允许所有
   * @since 1.0.0
   */
  String[] groups() default {};
  /**
   * 匹配的机器人qq号列表
   * 若消息接受的bot不在列表内则不做响应
   * 为空则允许所有
   * @since 1.0.0
   */
  String[] bots() default {};
  /**
   * 是否当bot被at时才触发<br/>
   * <strong>好友消息时忽略<strong/>
   * @since 1.0.0
   */
  boolean isAt() default false;
  /**
   * 是否at全体时才触发<br/>
   * <strong>好友消息时忽略<strong/>
   * @since 1.0.0
   */
  boolean isAtAll() default false;
  /**
   * 是否有人被at时才触发
   * 不一定是bot被at<br/>
   * <strong>好友消息时忽略<strong/>
   * @since 1.0.0
   */
  boolean isAtAny() default false;
}
