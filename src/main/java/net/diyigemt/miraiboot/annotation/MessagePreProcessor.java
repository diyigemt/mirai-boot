package net.diyigemt.miraiboot.annotation;

import net.diyigemt.miraiboot.constant.MessagePreProcessorMessageType;
import net.diyigemt.miraiboot.entity.PreProcessorData;

import java.lang.annotation.*;

/**
 * <h2>消息事件预处理器</h2>
 * 对收到的消息事件进行预处理<br/>
 * 例如
 * <pre>
 * {@code
 * 以下配置将会响应 搜图 指令 并将消息中的图片过滤至 data.classified中
 * @EventHandler(target = "搜图")
 * @MessagePreProcessor(filterType = MessagePreProcessorMessageType.IMAGE)
 * public void searchImage(MessageEvent event, PreProcessData data) { your code }
 * }
 * </pre>
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MessagePreProcessors.class)
@Documented
public @interface MessagePreProcessor {
  /**
   * 将所有纯文本消息提取出来
   * 保存在PreProcessorData.text中<br/>
   * 其实无论是true还是false此项均会起作用
   * @see PreProcessorData
   * @since 1.0.0
   */
  boolean textProcessor() default false;
  /**
   * 将对应类型的消息提取出来
   * 保存在PreProcessorData.classified中
   * @see PreProcessorData
   * @see MessagePreProcessorMessageType
   * @since 1.0.0
   */
  MessagePreProcessorMessageType[] filterType() default {};
}
