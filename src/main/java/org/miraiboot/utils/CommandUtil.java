package org.miraiboot.utils;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.MessageFilter;
import org.miraiboot.annotation.MessagePreProcessor;
import org.miraiboot.constant.ConstantGlobal;
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
 * <h2>用于处理指令和参数以及注解</h2>
 * @author diyigemt
 * @since 1.0.0
 */
public class CommandUtil {
	/**
	 * 全局唯一实例
	 */
	private static final CommandUtil INSTANCE = new CommandUtil();
	/**
	 * <h2>所有注册的指令开头的集合</h2>
	 * 静态的, 当配置文件载入后<strong>不再改变</strong> 当然如果运行时需要添加新的指令开头也可以<br/>
	 * 例如<br/>
	 *	CommandUtil.getInstance().registerCommandStart(String start);<br/>
	 *  注册完成后调用<br/>
	 *	CommandUtil.getInstance().compileCommandPattern();<br/>
	 *	即可完成新指令开头的注册
	 */
	private static final Set<String> COMMAND_START_SET = new HashSet<String>();
	/**
	 * <h2>基本正则 用于匹配指令本身</h2>
	 * 注意:<strong>指令仅支持纯中文或者纯英文与阿拉伯数字组合两种形式 不能中英混用</strong>
	 */
	private static final String BASE_PATTERN = "?([\\u4e00-\\u9fa5]+|[a-zA-Z0-9]+))";
	/**
	 * 正则中的特殊字符 正则编译时需要转义
	 */
	private static final List<String> SPECIAL_CHARACTER = Arrays.asList("^", "$", "[", "(", ")", "{", "\\", "?", ".", "*", "|", "+");
	/**
	 * <h2>匹配指令及其参数的正则</h2>
	 * 会在注册完成指令开头后调用compileCommandPattern()方法编译<br/>
	 * 因此 在运行时注册完指令开头后<strong>需要</strong>调用compileCommandPattern()进行重新编译
	 */
	private static Pattern commandPattern;


	/**
	 * 获取全局唯一实例
	 * @return CommandUtil的全局唯一实例
	 */
	public static CommandUtil getInstance() { return INSTANCE; }

	/**
	 * <h2>注册一个指令开头</h2>
	 * <p>什么是指令开头呢 就比如"/搜图" 那么指令本身是"搜图" 而"/"就是指令开头了</p>
	 * <p>支持同指令不同开头对应不同EventHandler 比如注册两种指令开头"/"和"."</p>
	 * <p>那么"/搜图"和".搜图"可以分别指向不同的EventHandler</p>
	 * @param start 要注册的指令开头
	 */
	public void registerCommandStart(String start) {
		COMMAND_START_SET.add(start);
	}

	/**
	 * <h2>将指令开头编译至指令分离正则中</h2>
	 * <h2>因此 在运行时注册完指令开头后务必重新调用compileCommandPattern()进行编译</h2>
	 */
	public void compileCommandPattern() {
		StringBuilder sb = new StringBuilder();
		sb.append("((");
		if (!COMMAND_START_SET.isEmpty()) {
			COMMAND_START_SET.forEach(item -> {
				//判断是否是特殊字符需要转义
				if (SPECIAL_CHARACTER.contains(item)) {
					sb.append("\\").append(item).append("|");
				} else {
					sb.append(item).append("|");
				}
			});
			sb.replace(sb.length() - 1, sb.length(), "");
		}
		sb.append(")");
		sb.append(BASE_PATTERN);
		commandPattern = Pattern.compile(sb.toString());
	}

	/**
	 * <h2>将指令字符串通过正则分离出指令</h2>>
	 * @param source 原字符串
	 * @return 分离出的指令
	 */
	public String parseCommand(String source) {
		// 默认为空指令
		String command = "";
		Matcher matcher = commandPattern.matcher(source);
		if (matcher.find() && matcher.groupCount() >= 3) {
			command = matcher.group(1);
		}
		return command;
	}

	/**
	 * <h2>从纯文本中分理出参数和被检测到属于指令的内容</h2>
	 * @param source 消息纯文本
	 * @param command 指令 由于指令别名的存在没有从方法的注解中获取 而是采用在parseCommand中获取的指令名
	 * @param handler 触发的方法
	 * @param data 存储处理结果
	 * @return 处理结果
	 */
	public PreProcessorData parseArgs(String source, String command, Method handler, PreProcessorData data) {
		EventHandler eventHandlerAnnotation = handler.getAnnotation(EventHandler.class);
		String start = eventHandlerAnnotation.start();
		if (start.equals("")) {
			Object o = GlobalConfig.getInstance().get(ConstantGlobal.DEFAULT_COMMAND_START);
			if (!o.toString().equals("")) start = o.toString();
		}
		String remove = start + command;
		String s = source.substring(source.indexOf(remove) + remove.length()).trim();
		String split = eventHandlerAnnotation.split();
		if (split.equals("")) split = "\\s+";
		String[] res = s.split(split);
		data.addArgs(res);
		data.setCommandText(s);
		return data;
	}

	/**
	 * <h2>检查@MessageFiler是否通过</h2>
	 * @param event 要检查的事件
	 * @param handler 要检查的事件Handler
	 * @param source 消息纯文本
	 * @return 是否通过
	 */
	public boolean checkFilter(MessageEvent event, Method handler, String source) {
		MessageFilter[] filters = handler.getDeclaredAnnotationsByType(MessageFilter.class);
		for (MessageFilter filter : filters) {
			MessageFilterItem item = new MessageFilterItem(filter);
			if (!item.check(event, source)) return false;
		}
		return true;
	}

	/**
	 * <h2>进行消息预处理</h2>
	 * @param event 要处理的事件
	 * @param handler 要处理的事件Handler
	 * @param data 存放结果
	 * @return 结果
	 */
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
