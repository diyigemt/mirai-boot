package org.miraiboot.entity;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.OnlineMessageSource.Incoming;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.constant.EventType;
import org.miraiboot.interfaces.EventHandlerNext;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>对消息事件的简单封装</h2>
 * @author diyigemt
 * @since 1.0.0
 */
public class MessageEventPack {
	/**
	 * 消息事件本身
	 */
	private MessageEvent event;
	/**
	 * 消息事件类型 群消息或者好友消息
	 */
	private EventType eventType;

	public MessageEventPack(MessageEvent event) {
		if (event instanceof GroupMessageEvent) this.eventType = EventType.GROUP_MESSAGE_EVENT;
		else if (event instanceof FriendMessageEvent) this.eventType = EventType.FRIEND_MESSAGE_EVENT;
		else if (event instanceof GroupTempMessageEvent) this.eventType = EventType.GROUP_TMP_MESSAGE_EVENT;
		else this.eventType = EventType.OTHER_EVENT;
		this.event = event;
	}

	/**
	 * <h2>回复一串文本消息并at发送者</h2>
	 * @param s 一串文本
	 */
	public void reply(String... s) {
		MiraiMain.getInstance().reply(this, s);
	}

	/**
	 * <h2>回复一串文本消息不at发送者</h2>
	 * @param s 一串文本
	 */
	public void replyNotAt(String... s) {
		MiraiMain.getInstance().replyNotAt(this, s);
	}

	/**
	 * <h2>回复一串文本消息并at发送者</h2>
	 * @param msg 一串消息
	 */
	public void reply(SingleMessage... msg) {
		MiraiMain.getInstance().reply(this, msg);
	}


	/**
	 * <h2>回复一串文本消息不at发送者</h2>
	 * @param msg 一串消息
	 */
	public void replyNotAt(SingleMessage... msg) {
		MiraiMain.getInstance().replyNotAt(this, msg);
	}

	/**
	 * <h2>回复一串文本消息并at发送者</h2>
	 * @param chain 构建好的消息链
	 */
	public void reply(MessageChain chain) {
		MiraiMain.getInstance().reply(this, chain);
	}


	/**
	 * <h2>回复一串文本消息不at发送者</h2>
	 * @param chain 构建好的消息链
	 */
	public void replyNotAt(MessageChain chain) {
		MiraiMain.getInstance().replyNotAt(this, chain);
	}

	/**
	 * <h2>向指定的群发送消息</h2>
	 * @param groupId 指定的群号
	 * @param msg 消息
	 * @return 群不存在时(机器人不在群中)返回false
	 */
	public boolean sendGroupMessage(long groupId, String... msg) {
		Group group = getGroup(groupId);
		if (group == null) return false;
		MiraiMain.getInstance().sendGroupMessage(group, msg);
		return true;
	}

	/**
	 * <h2>向指定的群发送消息</h2>
	 * @param groupId 指定的群号
	 * @param msg 消息
	 * @return 群不存在时(机器人不在群中)返回false
	 */
	public boolean sendGroupMessage(long groupId, SingleMessage... msg) {
		Group group = getGroup(groupId);
		if (group == null) return false;
		MiraiMain.getInstance().sendGroupMessage(group, msg);
		return true;
	}

	/**
	 * <h2>向指定的群发送消息</h2>
	 * @param groupId 指定的群号
	 * @param chain 构建好的消息链
	 * @return 群不存在时(机器人不在群中)返回false
	 */
	public boolean sendGroupMessage(long groupId, MessageChain chain) {
		Group group = getGroup(groupId);
		if (group == null) return false;
		MiraiMain.getInstance().sendGroupMessage(group, chain);
		return true;
	}

	/**
	 * <h2>向指定的群发送消息</h2>
	 * @param groupId 指定的群号
	 * @param chains 构建好的消息链列表
	 * @return 群不存在时(机器人不在群中)返回false 消息为空返回false
	 */
	public boolean sendGroupMessage(long groupId, List<MessageChain> chains) {
		if (chains == null || chains.size() == 0) return false;
		Group group = getGroup(groupId);
		if (group == null) return false;
		for (MessageChain chain : chains) {
			MiraiMain.getInstance().sendGroupMessage(group, chain);
		}
		return true;
	}

	/**
	 * <h2>向群中指定的人发送临时消息</h2>
	 * @param groupId 指定的群号
	 * @param targetId 指定人的qq号
	 * @param msg 消息
	 * @return 群不存在时(机器人不在群中) 或者目标不在群中时返回false
	 */
	public boolean sendTempMessage(long groupId, long targetId, String... msg) {
		Group group = getGroup(groupId);
		if (group == null) return false;
		NormalMember member = group.get(targetId);
		if (member == null) return false;
		MiraiMain.getInstance().sendTempMessage(member, msg);
		return true;
	}

	/**
	 * <h2>向群中指定的人发送临时消息</h2>
	 * @param groupId 指定的群号
	 * @param targetId 指定人的qq号
	 * @param msg 消息
	 * @return 群不存在时(机器人不在群中) 或者目标不在群中时返回false
	 */
	public boolean sendTempMessage(long groupId, long targetId, SingleMessage... msg) {
		Group group = getGroup(groupId);
		if (group == null) return false;
		NormalMember member = group.get(targetId);
		if (member == null) return false;
		MiraiMain.getInstance().sendTempMessage(member, msg);
		return true;
	}

	/**
	 * <h2>向群中指定的人发送临时消息</h2>
	 * @param groupId 指定的群号
	 * @param targetId 指定人的qq号
	 * @param chain 构建好的消息链
	 * @return 群不存在时(机器人不在群中) 或者目标不在群中时返回false
	 */
	public boolean sendTempMessage(long groupId, long targetId, MessageChain chain) {
		Group group = getGroup(groupId);
		if (group == null) return false;
		NormalMember member = group.get(targetId);
		if (member == null) return false;
		MiraiMain.getInstance().sendTempMessage(member, chain);
		return true;
	}

	/**
	 * <h2>向指定的好友发送消息</h2>
	 * @param friendId 指定的好友qq号
	 * @param msg 消息
	 * @return 好友不存在时返回false
	 */
	public boolean sendFriendMessage(long friendId, String... msg) {
		Friend friend = getFriend(friendId);
		if (friend == null) return false;
		MiraiMain.getInstance().sendFriendMessage(friend, msg);
		return true;
	}

	/**
	 * <h2>向指定的好友发送消息</h2>
	 * @param friendId 指定的好友qq号
	 * @param msg 消息
	 * @return 好友不存在时返回false
	 */
	public boolean sendFriendMessage(long friendId, SingleMessage... msg) {
		Friend friend = getFriend(friendId);
		if (friend == null) return false;
		MiraiMain.getInstance().sendFriendMessage(friend, msg);
		return true;
	}

	/**
	 * <h2>向指定的好友发送消息</h2>
	 * @param friendId 指定的好友qq号
	 * @param chain 构建好的消息链
	 * @return 好友不存在时返回false
	 */
	public boolean sendFriendMessage(long friendId, MessageChain chain) {
		Friend friend = getFriend(friendId);
		if (friend == null) return false;
		MiraiMain.getInstance().sendFriendMessage(friend, chain);
		return true;
	}

	/**
	 * <h2>向指定的好友发送消息</h2>
	 * @param friendId 指定的好友qq号
	 * @param chains 构建好的消息链列表
	 * @return 好友不存在时返回false 消息为空返回false
	 */
	public boolean sendFriendMessage(long friendId, List<MessageChain> chains) {
		if (chains == null || chains.size() == 0) return false;
		Friend friend = getFriend(friendId);
		if (friend == null) return false;
		for (MessageChain chain : chains) {
			MiraiMain.getInstance().sendFriendMessage(friend, chain);
		}
		return true;
	}

	/**
	 * <h2>注册一个上下文监听器</h2>
	 * 使用全局配置的超时时间 监听本事件的发送者<br/>
	 * <strong>超时时间只会在该监听器第一次被触发时才开始倒计时</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param next 监听器本体
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNext(T next) {
		EventHandlerManager.getInstance().onNext(getSenderId(), next);
	}

	/**
	 * <h2>注册一个上下文监听器</h2>
	 * 监听一次 监听本事件的发送者<br/>
	 * <strong>超时时间只会在该监听器第一次被触发时才开始倒计时</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param next 监听器本体
	 * @param timeOut 超时时间
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNext(T next, long timeOut) {
		EventHandlerManager.getInstance().onNext(getSenderId(), next, timeOut);
	}

	/**
	 * <h2>注册一个上下文监听器</h2>
	 * 使用全局配置的超时时间 监听本事件的发送者<br/>
	 * <strong>超时时间只会在该监听器第一次被触发时才开始倒计时</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param next 监听器本体
	 * @param triggerCount 最高触发次数
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNext(T next, int triggerCount) {
		EventHandlerManager.getInstance().onNext(getSenderId(), next, triggerCount);
	}

	/**
	 * <h2>注册一个上下文监听器</h2>
	 * 监听本事件的发送者<br/>
	 * <strong>超时时间只会在该监听器第一次被触发时才开始倒计时</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param next 监听器本体
	 * @param  timeOut 超时时间
	 * @param triggerCount 最高触发次数
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNext(T next, long timeOut, int triggerCount) {
		EventHandlerManager.getInstance().onNext(getSenderId(), next, timeOut, triggerCount);
	}

	/**
	 * <h2>注册一个上下文监听器</h2>
	 * 监听指定的qq号事件<br/>
	 * <strong>超时时间只会在该监听器第一次被触发时才开始倒计时</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param target 监听目标qq号
	 * @param next 监听器本体
	 * @param  timeOut 超时时间
	 * @param triggerCount 最高触发次数
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNext(long target, T next, long timeOut, int triggerCount) {
		EventHandlerManager.getInstance().onNext(target, next, timeOut, triggerCount);
	}

	/**
	 * <h2>注册一个上下文监听器并开始倒计时</h2>
	 * 使用全局配置的超时时间 只监听一次 监听本事件的发送者<br/>
	 * <strong>倒计时将立即开始</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param next 监听器本体
	 * @param data 当前PreProcessorData 防止监听器没被触发时给onDestroy之类的用
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNextNow(T next, PreProcessorData data) {
		EventHandlerManager.getInstance().onNextNow(getSenderId(), next, -1, -1, this, data);
	}

	/**
	 * <h2>注册一个上下文监听器并开始倒计时</h2>
	 * 监听本事件的发送者<br/>
	 * <strong>倒计时将立即开始</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param next 监听器本体
	 * @param data 当前PreProcessorData 防止监听器没被触发时给onDestroy之类的用
	 * @param timeOut 超时时间
	 * @param triggerCount 监听次数
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNextNow(T next, PreProcessorData data, long timeOut, int triggerCount) {
		EventHandlerManager.getInstance().onNextNow(getSenderId(), next, timeOut, triggerCount, this, data);
	}

	/**
	 * <h2>注册一个上下文监听器并开始倒计时</h2>
	 * 监听指定的qq号事件<br/>
	 * <strong>倒计时将立即开始</strong>
	 * EventHandlerNext的具体用法请参照{@link EventHandlerNext}
	 * @param target 监听目标qq号
	 * @param next 监听器本体
	 * @param data 当前PreProcessorData 防止监听器没被触发时给onDestroy之类的用
	 * @param timeOut 超时时间
	 * @param triggerCount 监听次数
	 * @see EventHandlerManager
	 * @see EventHandlerNext
	 */
	public <T extends EventHandlerNext> void onNextNow(long target, T next, PreProcessorData data, long timeOut, int triggerCount) {
		EventHandlerManager.getInstance().onNextNow(target, next, timeOut, triggerCount, this, data);
	}

	/**
	 * <h2>根据提供的消息类型过滤消息链</h2>
	 * <strong>推荐使用@MessageFilter</strong>
	 * @param target 需要过滤出的消息类型
	 * @return 过滤出的列表
	 */
	public <T extends SingleMessage> List<T> getMessageByType(Class<T> target) {
		return event.getMessage().stream().filter(item -> target.isAssignableFrom(item.getClass())).map(item -> (T) item).collect(Collectors.toList());
	}

	/**
	 * <h2>判断消息是否来源于群聊</h2>
	 * @return 消息是否来源于群聊 true: 消息来源于群聊
	 */
	public boolean isGroup() {
		return eventType == EventType.GROUP_MESSAGE_EVENT;
	}

	/**
	 * <h2>判断消息是否来源于好友</h2>
	 * @return 消息是否来源于好友 true: 消息来源于好友
	 */
	public boolean isFriend() {
		return eventType == EventType.FRIEND_MESSAGE_EVENT;
	}

	/**
	 * <h2>判断消息是否来源于群临时聊天</h2>
	 * @return 消息是否来源于群临时聊天 true: 消息来源于群临时聊天
	 */
	public boolean isGroupTmp() {
		return eventType == EventType.GROUP_TMP_MESSAGE_EVENT;
	}

	/**
	 * <h2>获取群消息发送者的权限</h2>
	 * @return 群权限 当消息不是群消息时返回普通群员权限
	 */
	public MemberPermission getSenderPermission() {
		if (!isGroup()) return MemberPermission.MEMBER;
		return ((GroupMessageEvent) event).getPermission();
	}

	/**
	 * <h2>获取发送者的昵称</h2>
	 * 发送人名称. 由群员发送时为群员名片, 由好友发送时为好友昵称.
	 * @return 发送者的昵称
	 */
	public String getSenderNick() {
		return getSender().getNick();
	}

	/**
	 * <h2>获取收到该消息的bot的qq号</h2>
	 * @return bot的qq号
	 */
	public long getBotId() {
		return getBot().getId();
	}

	/**
	 * <h2>获取收到消息的时间戳</h2>
	 * 消息发送时间戳, 单位为秒. 由服务器提供, 可能与本地有时差
	 * @return 收到消息的时间
	 */
	public int getTime() {
		return event.getTime();
	}

	/**
	 * <h2>获取消息的来源信息</h2>
	 * 详细请看 https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/MessageSource.kt
	 * @return 消息源
	 */
	public Incoming getSource() {
		return event.getSource();
	}

	/**
	 * <h2>获取发送者的名片</h2>
	 * 同 getSenderNick
	 * @return 发送者的名片
	 */
	public String getSenderName() {
		return event.getSenderName();
	}

	/**
	 * <h2>获取发送者</h2>
	 * @return 发送者
	 */
	public User getSender() {
		return event.getSender();
	}

	/**
	 * <h2>获取收到该事件的bot</h2>
	 * @return 收到该事件的bot
	 */
	public Bot getBot() {
		return event.getBot();
	}

	/**
	 * <h2>获取事件主体</h2>
	 * <pre>
	 * - 对于私聊会话, 这个属性与 [sender] 相同;
	 * - 对于群消息, 这个属性为 [Group] 的实例, 与 [GroupMessageEvent.group] 相同.
	 *
	 * 如果在 [GroupMessageEvent] 对 [sender] 发送消息, 将会通过私聊发送给群员, 而不会发送在群内.
	 * 使用 [subject] 作为消息目标则可以确保消息发送到用户所在的场景.
	 *
	 * 在回复消息时, 可通过 [subject] 作为回复对象.
	 * </pre>
	 */
	public Contact getSubject() {
		return event.getSubject();
	}

	/**
	 * 返回的消息链中一定包含 [MessageSource], 存储此消息的发送人, 发送时间, 收信人, 消息 ids 等数据. 随后的元素为拥有顺序的真实消息内容.
	 * 详细查看
	 * <p>https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/MessageChain.kt</p>
	 * @return 消息链
	 */
	public MessageChain getMessage() {
		return event.getMessage();
	}

	public Group getGroup() {
		if (!isGroup()) return null;
		return ((GroupMessageEvent) event).getGroup();
	}

	/**
	 * <h2>由群号获取群对象</h2>
	 * @param groupId 群号
	 * @return 群对象
	 */
	public Group getGroup(long groupId) {
		return getBot().getGroup(groupId);
	}

	/**
	 * <h2>由qq号获取好友对象</h2>
	 * @param friendId 好友qq号
	 * @return 好友对象
	 */
	public Friend getFriend(long friendId) {
		return getBot().getFriend(friendId);
	}

	/**
	 * <h2>获取发送者的qq号</h2>
	 * @return 发送者的qq号
	 */
	public long	getSenderId() {
		return getSender().getId();
	}

	/**
	 * <h2>获取消息事件类型</h2>
	 * 具体类型看{@link EventType}
	 * @return 消息事件类型
	 */
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * <h2>获取消息事件本身</h2>
	 * @return 消息事件本身
	 */
	public MessageEvent getEvent() {
		return event;
	}

	public void setEvent(MessageEvent event) {
		this.event = event;
	}
}
