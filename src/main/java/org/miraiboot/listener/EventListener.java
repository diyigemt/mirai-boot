package org.miraiboot.listener;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import org.miraiboot.entity.Command;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.CommonUtil;
import org.miraiboot.utils.EventHandlerManager;

import java.util.function.Consumer;

public class EventListener implements Consumer<MessageEvent> {
	@Override
	public void accept(MessageEvent messageEvent) {
		// 若没有@Bot则不做回应
		if (!messageEvent.getMessage().contains(new At(messageEvent.getBot().getId()))) return;
		// 复读
		String content = messageEvent.getMessage().serializeToMiraiCode();
		Command command = CommonUtil.parseCommandAndArgs(content);
		String name = command.getName();
		boolean emit = EventHandlerManager.getInstance().emit(name, messageEvent);
		if (!emit) {
			MiraiMain.logger.error("事件出错!");
		}
	}
}
