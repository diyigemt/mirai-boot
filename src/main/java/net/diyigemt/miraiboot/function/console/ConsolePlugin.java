package net.diyigemt.miraiboot.function.console;

import net.diyigemt.miraiboot.annotation.ConsoleCommand;
import net.diyigemt.miraiboot.annotation.MiraiBootComponent;
import net.diyigemt.miraiboot.core.PluginMgr;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.BotManager;
import net.diyigemt.miraiboot.utils.EventHandlerManager;

import java.util.List;

@MiraiBootComponent
public class ConsolePlugin {

	@ConsoleCommand("plugin")
	public void exit() {
		List<String> res = PluginMgr.getPluginConnectionList();
		MiraiMain.logger.info("已加载插件：");
		for (String s : res) {
			MiraiMain.logger.info(s);
		}
	}
}
