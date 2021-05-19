package org.miraiboot.mirai;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * <h2>全局实例 用于回复消息和打印log</h2>
 * @author diyigemt
 * @since 1.0.0
 */
public class MiraiMain {
	public static final MiraiLogger logger = MiraiLogger.create("mirai-boot-status");
	// 全局唯一实例
	public static final MiraiMain INSTANCE = new MiraiMain();

	/**
	 * <h2>获取实例</h2>
	 * @return 全局实例
	 */
	public static MiraiMain getInstance() {
		return INSTANCE;
	}

	/**
	 * <h2>快速回复消息</h2>
	 * @param event 消息事件
	 * @param msg 要回复的消息内容
	 */
	public void quickReply(MessageEvent event, String msg) {
		long senderId = event.getSender().getId();
		MessageChain chain = null;
		if (event instanceof GroupMessageEvent) {
			// 如果是群事件 回复时@本人
			chain = new MessageChainBuilder().append(new At(senderId)).append("\n").append(msg).build();
		}
		if (event instanceof FriendMessageEvent) {
			chain = new MessageChainBuilder().append(msg).build();
		}
		this.quickReply(event, chain);
	}

	/**
	 * <h2>快速回复消息</h2>
	 * @param event 消息事件
	 * @param messages 消息数组
	 */
	public void quickReply (MessageEvent event, SingleMessage... messages) {
		MessageChainBuilder builder = new MessageChainBuilder();
		if (event instanceof GroupMessageEvent) {
			// 如果是群事件 回复时@本人
			long senderId = event.getSender().getId();
			builder.append(new At(senderId)).append("\n");
		}
		for (SingleMessage message : messages) {
			builder.append(message);
		}
		this.quickReply(event, builder.build());
	}

	/**
	 * <h2>快速回复消息</h2>
	 * @param event 消息事件
	 * @param chain 消息链
	 */
	public void quickReply(MessageEvent event, MessageChain chain) {
		event.getSubject().sendMessage(chain);
	}
}
