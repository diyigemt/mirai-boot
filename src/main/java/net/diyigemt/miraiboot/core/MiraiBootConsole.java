package net.diyigemt.miraiboot.core;

import net.diyigemt.miraiboot.entity.ConsoleHandlerItem;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.BotManager;
import net.diyigemt.miraiboot.utils.EventHandlerManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * <h2>MiraiBoot控制台</h2>
 *
 * @author diyigemt
 * @author Haythem
 */
public final class MiraiBootConsole {

	private static final MiraiBootConsole INSTANCE = new MiraiBootConsole();

	private static final Map<String, ConsoleHandlerItem> store = new HashMap<>();

	public static MiraiBootConsole getInstance() { return INSTANCE; }

	/**
	 * <h2>注册一条控制台指令</h2>
	 * @param target 指令名
	 * @param invoker 操作类
	 * @param handler 操作方法
	 * @return 是否注册成功, 当存在同名指令时返回false
	 */
	public boolean on(String target, Class<?> invoker, Method handler) {
		ConsoleHandlerItem one = store.get(target);
		if (one != null) return false;
		ConsoleHandlerItem item = new ConsoleHandlerItem(target, invoker, handler);
		store.put(target, item);
		return true;
	}


	/**
	 * <h2>控制台输入监听</h2>
	 */
	public void listenLoop() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String command = scanner.next();
			//退出命令
			if (command.equals("exit")) {
				EventHandlerManager.getInstance().cancelAll();
				BotManager.getInstance().logoutAll();
				break;
			}
			//热卸载插件命令
//            else if(command.matches("^unload")){
//                String PluginName = scanner.next();
//                if(PluginName.matches("^.+\\.jar")){
//                    PluginMgr.unLoadPlugin(PluginName);
//                }else MiraiMain.logger.error("非法的MiraiBoot插件名称");
//            }
			//已加载插件查询
			else if (command.equals("plugin")) {
				List<String> res = PluginMgr.getPluginConnectionList();
				MiraiMain.logger.info("已加载插件：");
				for (String s : res) {
					MiraiMain.logger.info(s);
				}
			}
			//根本匹配不上
			else {
				MiraiMain.logger.error("命令：" + "\"" + command + "\"" + " 不是有效的MiraiBoot命令");
			}
		}
	}
}
