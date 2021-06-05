package net.diyigemt.miraiboot.function.console;

import net.diyigemt.miraiboot.annotation.ConsoleCommand;
import net.diyigemt.miraiboot.annotation.MiraiBootComponent;
import net.diyigemt.miraiboot.utils.BotManager;
import net.diyigemt.miraiboot.utils.EventHandlerManager;

@MiraiBootComponent
public class ConsoleExit {

	@ConsoleCommand("exit")
	public void exit() {
		EventHandlerManager.getInstance().cancelAll();
		BotManager.getInstance().logoutAll();
		System.exit(0);
	}
}
