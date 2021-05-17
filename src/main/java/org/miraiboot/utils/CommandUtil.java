package org.miraiboot.utils;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.MessageFilter;
import org.miraiboot.annotation.MessagePreProcessor;
import org.miraiboot.entity.MessageFilterItem;
import org.miraiboot.entity.MessagePreProcessorItem;
import org.miraiboot.entity.PreProcessorData;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
	private static final List<String> SPECIAL_CHARACTER = Arrays.asList("^", "$", "[", "(", ")", "{", "\\", "?", ".", "*", "|", "+");
	private static Pattern commandPattern;


	public static CommandUtil getInstance() { return INSTANCE; }

	public void registerCommandStart(String start) {
		COMMAND_START_SET.add(start);
	}

	public void compileCommandPattern() {
		StringBuilder sb = new StringBuilder();
		if (!COMMAND_START_SET.isEmpty()) {
			sb.append("(");
			COMMAND_START_SET.forEach(item -> {
				if (SPECIAL_CHARACTER.contains(item)) {
					sb.append("\\").append(item).append("|");
				} else {
					sb.append(item).append("|");
				}
			});
			sb.replace(sb.length() - 1, sb.length(), "");
			sb.append(")");
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

	public PreProcessorData parseArgs(String source, Method handler, PreProcessorData data) {
		EventHandler eventHandlerAnnotation = handler.getAnnotation(EventHandler.class);
		String target = eventHandlerAnnotation.target();
		String command = target.equals("") ? handler.getName() : target;
		String start = eventHandlerAnnotation.start();
		String remove = command + start;
		String s = source.substring(source.indexOf(remove) + remove.length()).trim();
		String split = eventHandlerAnnotation.split();
		String[] res = s.split(split);
		data.addArgs(res);
		data.setCommandText(s);
		return data;
	}

	public boolean checkFilter(MessageEvent event, Method handler, String source) {
		MessageFilter[] filters = handler.getDeclaredAnnotationsByType(MessageFilter.class);
		for (MessageFilter filter : filters) {
			MessageFilterItem item = new MessageFilterItem(filter);
			if (!item.check(event, source)) return false;
		}
		return true;
	}

	public PreProcessorData parsePreProcessor(MessageEvent event, Method handler, PreProcessorData data) {
		MessagePreProcessor[] annotations = handler.getDeclaredAnnotationsByType(MessagePreProcessor.class);
		MessagePreProcessorItem preProcessorItem = new MessagePreProcessorItem();
		for (MessagePreProcessor preProcessor : annotations) {
			preProcessorItem.addFilterType(preProcessor.filterType());
		}
		List<SingleMessage> singleMessages = preProcessorItem.parseMessage(event);
		data.addClassified(singleMessages);
		return data;
	}
}
