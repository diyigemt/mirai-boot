package org.miraiboot.listener;

import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.BotManager;
import org.miraiboot.utils.CommandUtil;
import org.miraiboot.utils.EventHandlerManager;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <h2>处理消息事件的监听器 目前只能处理群消息和好友消息</h2>
 * 其他消息处理在做了
 * @author diyigemt
 * @since 1.0.0
 */
public class MessageEventListener implements Consumer<MessageEvent> {
	public static boolean eventLoggerEnable = true;
	@Override
	public void accept(MessageEvent messageEvent) {
		// 目前只能处理群消息 好友消息 以及通过群发送的临时会话
		if (!(messageEvent instanceof GroupMessageEvent || messageEvent instanceof FriendMessageEvent || messageEvent instanceof GroupTempMessageEvent)) return;
		// 如果是机器人自己的消息事件 忽略
		if (BotManager.getInstance().get(messageEvent.getSender().getId()) != null) return;
		// 封装
		MessageEventPack eventPack = new MessageEventPack(messageEvent);
		// 提取纯文本

		List<PlainText> collect = eventPack.getMessageByType(PlainText.class);
		StringBuffer sb = new StringBuffer();
		collect.forEach(item -> {
			sb.append(item.contentToString());
		});
		String source = sb.toString();
		// 执行监听中的特定对话
		EventHandlerManager.getInstance().emitNext(eventPack.getSenderId(), eventPack, source);
		// 执行强制触发EventHandler
		String res = null;
		res = EventHandlerManager.getInstance().emit("", eventPack, source);
		if (res != null) {
			MiraiMain.logger.error(res);
		}
		// 获取指令
		String command = CommandUtil.getInstance().parseCommand(source);
		if (command.equals("")) return;
		// 执行指令对应的EventHandler
		res = EventHandlerManager.getInstance().emit(command, eventPack, source);
		if (res != null && eventLoggerEnable) {
			MiraiMain.logger.error(res);
		}
	}
}
