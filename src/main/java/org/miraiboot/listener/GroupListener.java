package org.miraiboot.listener;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.miraiboot.entity.Command;
import org.miraiboot.utils.CommonUtil;
import org.miraiboot.utils.EventHandlerUtil;

import java.util.function.Consumer;

public class GroupListener implements Consumer<GroupMessageEvent> {
	@Override
	public void accept(GroupMessageEvent groupMessageEvent) {
		// 若没有@Bot则不做回应
		if (!groupMessageEvent.getMessage().contains(new At(groupMessageEvent.getBot().getId()))) return;
		// 复读
		String content = groupMessageEvent.getMessage().serializeToMiraiCode();
		Command command = CommonUtil.parseCommandAndArgs(content);
		String name = command.getName();
		EventHandlerUtil.getInstance().emit(name, groupMessageEvent);
	}
}
