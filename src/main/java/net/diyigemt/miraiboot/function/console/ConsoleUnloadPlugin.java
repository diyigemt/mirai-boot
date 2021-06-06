package net.diyigemt.miraiboot.function.console;

import net.diyigemt.miraiboot.annotation.ConsoleCommand;
import net.diyigemt.miraiboot.annotation.MiraiBootComponent;
import net.diyigemt.miraiboot.core.PluginMgr;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.BotManager;
import net.diyigemt.miraiboot.utils.EventHandlerManager;

import java.util.List;
import java.util.Scanner;

@MiraiBootComponent
public class ConsoleUnloadPlugin {

	@ConsoleCommand("unload")
	public void unload(List<String> arg) {
		if (arg == null || arg.isEmpty()) {
			MiraiMain.logger.error("非法的MiraiBoot插件名称");
			return;
		}
		String PluginName = arg.get(0);
		if(PluginName.matches("^.+\\.jar")){
			PluginMgr.unLoadPlugin(PluginName);
		} else {
			MiraiMain.logger.error("非法的MiraiBoot插件名称");
		}
	}
}
