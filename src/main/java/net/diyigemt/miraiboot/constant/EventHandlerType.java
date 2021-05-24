package net.diyigemt.miraiboot.constant;

import net.diyigemt.miraiboot.annotation.EventHandler;

/**
 * <h2>消息处理Handler类型</h2>
 * 目前仅有群消息 好友消息和全部<br/>
 * 更多消息类型在做了
 * @author diyigemt
 * @since 1.0.0
 * @see EventHandler
 */
public enum EventHandlerType {
  /**
   * 处理好友消息
   * @since 1.0.0
   */
  MESSAGE_HANDLER_FRIEND,

  /**
   * 处理临时会话
   */
  MESSAGE_HANDLER_TEMP,

  /**
   * 处理群消息
   * @since 1.0.0
   */
  MESSAGE_HANDLER_GROUP,

  /**
   * 处理所有消息
   * @since 1.0.0
   */
  MESSAGE_HANDLER_ALL,

  /**
   * 处理其他事件
   * @since 1.0.0
   */
  OTHER_HANDLER;
}
