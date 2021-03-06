package net.diyigemt.miraiboot.constant;

/**
 * <h2>事件类型</h2>
 * @author diyigemt
 * @since 1.0.0
 */

public enum EventType {
	/**
	 * 群消息事件
	 * @since 1.0.0
	 */
	GROUP_MESSAGE_EVENT,
	/**
	 * 好友消息事件
	 * @since 1.0.0
	 */
	FRIEND_MESSAGE_EVENT,

	/**
	 * 通过群发送的临时消息事件
	 * @since 1.0.0
	 */
	GROUP_TMP_MESSAGE_EVENT,

	/**
	 * 其他的事件
	 * @since 1.0.0
	 */
	OTHER_EVENT;
}
