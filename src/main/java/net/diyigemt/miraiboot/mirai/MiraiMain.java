package net.diyigemt.miraiboot.mirai;

import net.diyigemt.miraiboot.constant.EventType;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.MiraiLogger;
import net.diyigemt.miraiboot.entity.MessageEventPack;

import java.util.Arrays;

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
		Arrays.asList(msg).forEach(builder::append);
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
		Arrays.asList(msg).forEach(builder::append);
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
	 * <h2>向指定的好友发送消息</h2>
	 * @param friend 指定的好友
	 * @param msg 消息
	 */
	public void sendFriendMessage(Friend friend, String... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		Arrays.asList(msg).forEach(builder::append);
		sendFriendMessage(friend, builder.build());
	}


	/**
	 * <h2>向指定的好友发送消息</h2>
	 * @param friend 指定的好友
	 * @param msg 消息
	 */
	public void sendFriendMessage(Friend friend, SingleMessage... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		Arrays.asList(msg).forEach(builder::append);
		sendFriendMessage(friend, builder.build());
	}

	/**
	 * <h2>向指定的好友发送消息</h2>
	 * @param friend 指定的好友
	 * @param chain 构造好的消息链
	 */
	public void sendFriendMessage(Friend friend, MessageChain chain) {
		friend.sendMessage(chain);
	}

	/**
	 * <h2>向指定的群发送消息</h2>
	 * @param group 指定的群
	 * @param msg 消息
	 */
	public void sendGroupMessage(Group group, String... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		Arrays.asList(msg).forEach(builder::append);
		sendGroupMessage(group, builder.build());
	}


	/**
	 * <h2>向指定的群发送消息</h2>
	 * @param group 指定的群
	 * @param msg 消息
	 */
	public void sendGroupMessage(Group group, SingleMessage... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		Arrays.asList(msg).forEach(builder::append);
		sendGroupMessage(group, builder.build());
	}

	/**
	 * <h2>向指定的群发送消息</h2>
	 * @param group 指定的群
	 * @param chain 构造好的消息链
	 */
	public void sendGroupMessage(Group group, MessageChain chain) {
		group.sendMessage(chain);
	}

	/**
	 * <h2>向群中指定的人发送临时消息</h2>
	 * @param member 指定的人
	 * @param msg 消息
	 */
	public void sendTempMessage(NormalMember member, String... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		Arrays.asList(msg).forEach(builder::append);
		sendTempMessage(member, builder.build());
	}


	/**
	 * <h2>向群中指定的人发送临时消息</h2>
	 * @param member 指定的人
	 * @param msg 消息
	 */
	public void sendTempMessage(NormalMember member, SingleMessage... msg) {
		MessageChainBuilder builder = new MessageChainBuilder();
		Arrays.asList(msg).forEach(builder::append);
		sendTempMessage(member, builder.build());
	}

	/**
	 * <h2>向群中指定的人发送临时消息</h2>
	 * @param member 指定的人
	 * @param chain 构造好的消息链
	 */
	public void sendTempMessage(NormalMember member, MessageChain chain) {
		member.sendMessage(chain);
	}

	/**
	 * <h2>快速回复消息</h2>
	 * 使用列
	 * @param event 消息事件
	 * @param msg 要回复的消息内容
	 */
	@Deprecated
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
	@Deprecated
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
	@Deprecated
	public void quickReply(MessageEvent event, MessageChain chain) {
		event.getSubject().sendMessage(chain);
	}
}
