package net.diyigemt.miraiboot.interfaces;

import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.MessageFilterItem;

/**
 * <h2>消息过滤器的接口</h2>
 * 所有实现了该接口的类均可以作为消息过滤器
 * @since 1.0.5
 * @author diyigemt
 */
public interface IMessageFilter {

  /**
   * 具体实现
   * @param source 消息纯文本
   * @param eventPack 消息事件封装
   * @param item 同注解下的其他配置
   * @return 是否通过, true:通过
   */
  boolean check(String source, MessageEventPack eventPack, MessageFilterItem item);
}
