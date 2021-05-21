package org.miraiboot.mirai;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.MiraiLogger;
import org.miraiboot.constant.EventType;
import org.miraiboot.entity.MessageEventPack;

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
	 * <h2>回复消息并at发送者</h2>
	 * @param eventPack 消息封装
	 * @param messages 消息文本内容
	 */
	public void reply(MessageEventPack eventPack, String... messages) {
		reply(eventPack, true, messages);
	}

	/**
	 * <h2>回复消息并at发送者</h2>
	 * @param eventPack 消息封装
	 * @param messages 消息内容
	 */
	public void reply(MessageEventPack eventPack, SingleMessage... messages) {
		reply(eventPack, true, messages);
	}

	/**
	 * <h2>回复消息并at发送者</h2>
	 * @param eventPack 消息封装
	 * @param chain 构建好的消息链
	 */
	public void reply(MessageEventPack eventPack, MessageChain chain) {
		reply(eventPack, chain, true);
	}

	/**
	 * <h2>回复消息不at发送者</h2>
	 * @param eventPack 消息封装
	 * @param messages 消息文本内容
	 */
	public void replyNotAt(MessageEventPack eventPack, String... messages) {
		reply(eventPack, false, messages);
	}

	/**
	 * <h2>回复消息不at发送者</h2>
	 * @param eventPack 消息封装
	 * @param messages 消息内容
	 */
	public void replyNotAt(MessageEventPack eventPack, SingleMessage... messages) {
		reply(eventPack, false, messages);
	}

	/**
	 * <h2>回复消息不at发送者</h2>
	 * @param eventPack 消息封装
	 * @param chain 构建好的消息链
	 */
	public void replyNotAt(MessageEventPack eventPack, MessageChain chain) {
		reply(eventPack, chain, false);
	}

	/**
	 * <h2>构建回复消息并根据at判断是否at发送者</h2>
	 * @param eventPack 消息封装
	 * @param at 是否at
	 * @param msg 消息内容
	 */
	private void reply(MessageEventPack eventPack, boolean at, SingleMessage... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		for (int i = 0; i < msg.length; i++) {
			builder.append(msg[i]);
			if (i + 1 == msg.length) break;
			builder.append("\n");
		}
		reply(eventPack, builder.build(), at);
	}

	/**
	 * <h2>构建回复消息并根据at判断是否at发送者</h2>
	 * @param eventPack 消息封装
	 * @param at 是否at
	 * @param msg 消息文本内容
	 */
	private void reply(MessageEventPack eventPack, boolean at, String... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		for (int i = 0; i < msg.length; i++) {
			builder.append(msg[i]);
			if (i + 1 == msg.length) break;
			builder.append("\n");
		}
		reply(eventPack, builder.build(), at);
	}

	/**
	 * <h2>用构建好的消息回复</h2>
	 * @param eventPack 消息封装
	 * @param chain 构建好的消息链
	 * @param at 是否at
	 */
	private void reply(MessageEventPack eventPack, MessageChain chain, boolean at) {
		if (at && eventPack.getEventType() == EventType.GROUP_MESSAGE_EVENT) replySelf(eventPack, chain);
		else replySelfNotAt(eventPack, chain);
	}

	/**
	 * <h2>用构建好的消息回复并at发送者</h2>
	 * @param eventPack 消息封装
	 * @param chain 构建好的消息链
	 */
	private void replySelf(MessageEventPack eventPack, MessageChain chain) {
		MessageChainBuilder builder = new MessageChainBuilder()
				.append(new At(eventPack.getSenderId()))
				.append("\n")
				.append(chain);
		eventPack.getSubject().sendMessage(builder.build());
	}

	/**
	 * <h2>用构建好的消息回复不at发送者</h2>
	 * @param eventPack 消息封装
	 * @param chain 构建好的消息链
	 */
	private void replySelfNotAt(MessageEventPack eventPack, MessageChain chain) {
		eventPack.getSubject().sendMessage(chain);
	}

	/**
	 * <h2>快速回复消息</h2>
	 * 使用列
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
