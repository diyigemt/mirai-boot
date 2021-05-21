package org.miraiboot.entity;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
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
		if (event instanceof FriendMessageEvent) this.eventType = EventType.FRIEND_MESSAGE_EVENT;
		this.event = event;
	}

	public void reply(String... s) {
		MiraiMain.getInstance().reply(this, s);
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
	public void onNext(EventHandlerNext next) {
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
	public void onNext(EventHandlerNext next, long timeOut) {
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
	public void onNext(EventHandlerNext next, int triggerCount) {
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
	public void onNext(EventHandlerNext next, long timeOut, int triggerCount) {
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
	public void onNext(long target, EventHandlerNext next, long timeOut, int triggerCount) {
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
	public void onNextNow(EventHandlerNext next, PreProcessorData data) {
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
	public void onNextNow(EventHandlerNext next, PreProcessorData data, long timeOut, int triggerCount) {
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
	public void onNextNow(long target, EventHandlerNext next, PreProcessorData data, long timeOut, int triggerCount) {
		EventHandlerManager.getInstance().onNextNow(target, next, timeOut, triggerCount, this, data);
	}

	/**
	 * <h2>根据提供的消息类型过滤消息链</h2>
	 * <strong>推荐使用@MessageFilter</strong>
	 * @param target 需要过滤出的消息类型
	 * @return 过滤出的列表
	 */
	public <T extends SingleMessage> List<SingleMessage> getMessageByType(Class<T> target) {
		return event.getMessage().stream().filter(item -> target.isAssignableFrom(item.getClass())).collect(Collectors.toList());
	}

	/**
	 * <h2>获取群消息发送者的权限</h2>
	 * @return 群权限 当消息不是群消息时返回普通群员权限
	 */
	public MemberPermission getSenderPermission() {
		if (!(eventType == EventType.GROUP_MESSAGE_EVENT)) return MemberPermission.MEMBER;
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
	public Incoming Incoming() {
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
		if (eventType != EventType.GROUP_MESSAGE_EVENT) return null;
		return  ((GroupMessageEvent) event).getGroup();
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
