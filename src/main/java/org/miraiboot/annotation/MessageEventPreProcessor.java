package org.miraiboot.annotation;

import org.miraiboot.constant.MessageEventFilterMatchType;
import org.miraiboot.constant.MessageEventPreProcessorMessageType;

import java.lang.annotation.*;

/**
 * 消息事件预处理器
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageEventPreProcessor {
  /**
   * 将所有纯文本消息提取出来
   * 保存在PreProcessorData.text中
   * @since 1.0.0
   */
  boolean textProcessor() default false;
  /**
   * 将对应类型的消息提取出来
   * 保存在PreProcessorData.classified中
   * @see MessageEventPreProcessorMessageType
   * @since 1.0.0
   */
  MessageEventPreProcessorMessageType[] messageProcessor() default {};
}
