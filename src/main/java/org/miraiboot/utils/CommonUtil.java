package org.miraiboot.utils;

import org.miraiboot.entity.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author diyigemt
 * 通用工具类
 */
public class CommonUtil {
	private static final Pattern commandPattern = Pattern.compile("/([\\u4e00-\\u9fa5]+|[a-zA-Z]+) ?(.*)$");

	/**
	 * 将字符串转换成Integer 是吧捕获异常并返回null
	 *
	 * @param s 要转换的字符串
	 * @return 结果
	 */
	public static Integer parseInt(String s) {
		try {
			return Integer.valueOf(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 核心功能 将指令字符串通过正则分离 指令 和 参数
	 *
	 * @param source 原字符串
	 * @return 分离出的指令 和 参数列表
	 */
	public static Command parseCommandAndArgs(String source) {
		// 默认为空指令
		Command command = new Command();
		Matcher matcher = commandPattern.matcher(source);
		if (matcher.find()) {
			int count = matcher.groupCount();
			String commandName = matcher.group(1);
			command.setName(commandName);
			// 由于 (.*) 的存在 导致会多一个 "" 参数 需要过滤
			if (count == 2 && (!matcher.group(2).equals(""))) {
				String arg = matcher.group(2);
				String[] args = arg.split(" +");
				for (String s : args) {
					if (s.equals("")) continue;
					command.addArgs(s);
				}
			}
		}
		return command;
	}

	public static String getCommandFailInfo(Command command, Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append("在执行 ")
				.append(command.toString())
				.append(" 时失败\nexception")
				.append(e.getClass())
				.append(": ")
				.append(e.getMessage())
				.append("\n");
		return sb.toString();
	}

	public static String getCommandFailInfo(Command command, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("在执行 ")
				.append(command.toString())
				.append(" 时失败\nmsg: ")
				.append(msg)
				.append("\n");
		return sb.toString();
	}
}
