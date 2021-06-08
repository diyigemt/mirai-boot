package net.diyigemt.miraiboot.core;

import net.diyigemt.miraiboot.annotation.ConsoleCommand;
import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.entity.ConsoleHandlerItem;
import net.diyigemt.miraiboot.entity.PluginItem;
import net.diyigemt.miraiboot.interfaces.UnloadHandler;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.CommandUtil;
import net.diyigemt.miraiboot.utils.ExceptionHandlerManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>MiraiBoot控制台</h2>
 *
 * @author diyigemt
 * @author Haythem
 */
public final class MiraiBootConsole implements UnloadHandler {

	private static final MiraiBootConsole INSTANCE = new MiraiBootConsole();

	private static final Map<String, ConsoleHandlerItem> store = new HashMap<>();

	private static final Pattern EMPTY_SOURCE = Pattern.compile("^\\s+$");

	public static MiraiBootConsole getInstance() {
		return INSTANCE;
	}

	/**
	 * <h2>注册一条控制台指令</h2>
	 *
	 * @param target  指令名
	 * @param invoker 操作类
	 * @param handler 操作方法
	 * @return 是否注册成功, 当存在同名指令时返回false
	 */
	public boolean on(String target, Class<?> invoker, Method handler) {
		ConsoleHandlerItem one = store.get(target);
		if (one != null) return false;
		String name = CommandUtil.getInstance().parseHandlerBaseName(invoker);
		ConsoleHandlerItem item = new ConsoleHandlerItem(name, invoker, handler);
		store.put(target, item);
		return true;
	}

	/**
	 * <h2>根据指令执行对应的方法</h2>
	 * @param target 指令名
	 * @param args 指令的参数
	 * @return 有对应的处理方法且执行成功返回true
	 */
	public boolean emit(String target, String... args) {
		List<String> arg = Arrays.asList(args);
		ConsoleHandlerItem item = store.get(target);
		if (item == null) return false;
		Method method = item.getHandler();
		Class<?> invoker = item.getInvoker();
		int count = method.getParameterCount();
		Object[] param = null;
		if (count != 0) {
			param = new Object[count];
			param[0] = arg;
		}
		try {
			if (count != 0) {
				method.invoke(invoker.getDeclaredConstructor().newInstance(), param);
			} else {
				method.invoke(invoker.getDeclaredConstructor().newInstance());
			}
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
			boolean res = ExceptionHandlerManager.getInstance().emit(e, null, null);
			if (!res) e.printStackTrace();
			return false;
		}
		return true;
	}

	private String[] parsArgs(String source) {
		Matcher matcher = EMPTY_SOURCE.matcher(source);
		if (source.equals("") || matcher.matches()) return null;
		return source.split("\\s+");
	}


	/**
	 * <h2>控制台输入监听</h2>
	 */
	public void listenLoop() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String source = scanner.nextLine();
			String[] strings = parsArgs(source);
			if (strings == null) continue;
			boolean emit = emit(strings[0], Arrays.copyOfRange(strings, 1, strings.length, String[].class));
			if (!emit) {
				MiraiMain.logger.error("命令：" + "\"" + source + "\"" + " 执行失败或不是有效的MiraiBoot命令");
			}
		}
	}

	/**
	 * <h2>根据指令移除一个指令</h2>
	 * @param command 指令
	 * @return 被移除的指令
	 */
	public ConsoleHandlerItem removeByCommand(String command) {
		return store.remove(command);
	}

	/**
	 * <h2>根据名字移除一个控制台指令</h2>
	 * @param name 指令名
	 * @return 被移除的指令
	 */
	public ConsoleHandlerItem removeByClassName(String name) {
		if (store.isEmpty()) return null;
		String t = null;
		for (Map.Entry<String, ConsoleHandlerItem> next : store.entrySet()) {
			if (next.getValue().getName().equals(name)) {
				t = next.getKey();
				break;
			}
		}
		if (t == null) return null;
		return store.remove(t);
	}

	@Override
	public void onUnload(List<PluginItem> pluginItems) {
		for (PluginItem item : pluginItems) {
			Annotation annotationData = item.getAnnotationData();
			if (!(annotationData instanceof ConsoleCommand)) {
				continue;
			}
			ConsoleCommand annotation = (ConsoleCommand) annotationData;
			String value = annotation.value();
			removeByCommand(value);
		}
	}
}
