package org.miraiboot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author diyigemt
 * 通用工具类
 */
public class CommandUtil {
	private static final CommandUtil INSTANCE = new CommandUtil();
	private static final Set<String> COMMAND_START_SET = new HashSet<String>();
	private static final String BASE_PATTERN = "([\\u4e00-\\u9fa5]+|[a-zA-Z]+)";
	private static Pattern commandPattern;


	public static CommandUtil getInstance() { return INSTANCE; }

	public void registerCommandStart(String start) {
		COMMAND_START_SET.add(start);
	}

	public void compileCommandPattern() {
		StringBuilder sb = new StringBuilder();
		if (!COMMAND_START_SET.isEmpty()) {
			sb.append("[");
			COMMAND_START_SET.forEach(sb::append);
			sb.append("]");
		}
		sb.append(BASE_PATTERN);
		commandPattern = Pattern.compile(sb.toString());
	}

	/**
	 * 核心功能 将指令字符串通过正则分离出指令
	 * @param source 原字符串
	 * @return 分离出的指令
	 */
	public String parseCommand(String source) {
		// 默认为空指令
		String command = "";
		Matcher matcher = commandPattern.matcher(source);
		if (matcher.find()) {
			command = matcher.group(1);
		}
		return command;
	}

	public ArrayList<String> parseArgs(String source, String regexp) {
		ArrayList<String> res = new ArrayList<String>();
		return res;
	}
}
