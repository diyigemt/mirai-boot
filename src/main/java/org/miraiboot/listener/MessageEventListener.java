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
	@Override
	public void accept(MessageEvent messageEvent) {
		// 执行强制触发EventHandler
		String res = EventHandlerManager.getInstance().emit("", messageEvent);
		if (res != null) {
			MiraiMain.logger.error(res);
		}
		// 提取纯文本
		List<SingleMessage> collect = messageEvent.getMessage().stream().filter(message -> message instanceof PlainText).collect(Collectors.toList());
		StringBuffer sb = new StringBuffer();
		collect.forEach(item -> {
			sb.append(item.contentToString());
		});
		// 获取指令
		String content = messageEvent.getMessage().serializeToMiraiCode();
		String command = CommandUtil.getInstance().parseCommand(content);
		// 执行指令对应的EventHandler
		res = EventHandlerManager.getInstance().emit(command, messageEvent);
		if (res != null) {
			MiraiMain.logger.error(res);
		}
	}
}
