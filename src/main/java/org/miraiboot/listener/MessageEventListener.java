package org.miraiboot.listener;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.CommandUtil;
import org.miraiboot.utils.EventHandlerManager;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MessageEventListener implements Consumer<MessageEvent> {
	public static boolean eventLoggerEnable = true;
	@Override
	public void accept(MessageEvent messageEvent) {
		// 提取纯文本
		List<SingleMessage> collect = messageEvent.getMessage().stream().filter(message -> message instanceof PlainText).collect(Collectors.toList());
		StringBuffer sb = new StringBuffer();
		collect.forEach(item -> {
			sb.append(item.contentToString());
		});
		String source = sb.toString();
		// 执行监听中的特定对话
		EventHandlerManager.getInstance().emitNext(messageEvent.getSender().getId(), messageEvent, source);
		// 执行强制触发EventHandler
		String res = EventHandlerManager.getInstance().emit("", messageEvent, source);
		if (res != null) {
			MiraiMain.logger.error(res);
		}
		// 获取指令
		String command = CommandUtil.getInstance().parseCommand(source);
		if (command.equals("")) return;
		// 执行指令对应的EventHandler
		res = EventHandlerManager.getInstance().emit(command, messageEvent, source);
		if (res != null) {
			MiraiMain.logger.error(res);
		}
	}
}
