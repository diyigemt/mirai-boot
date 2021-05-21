package org.miraiboot.utils.builder;

import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.miraiboot.annotation.HttpsProperties;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>自定义群文件消息构造器</h2>
 * <p>样例：</p>
 * <p>List<MessageChain> = new FileMessageBuilder(MessageEventPack eventPack).build();</p>
 * <p></p>
 * <p><b>注：请不要用此类创建变量</b></p>
 *
 * @author Haythem
 * @since 1.0.0
 */

public class FileMessageBuilder {

	private final Pattern windowsPattern = Pattern.compile("[A-z]:\\\\([A-Za-z0-9_\u4e00-\u9fa5]+\\\\)*");

	private final Pattern linuxPattern = Pattern.compile("/([A-Za-z0-9_\u4e00-\u9fa5]+/?)+");

	private MessageEvent event = null;

	private MessageEventPack messageEventPack = null;

	private List<MessageChain> chains = new ArrayList<>();

	private boolean isUTTPRequestSuccess = true;

	public static String FileName = null;


	/**
	 * <h2>自定义语音消息构造器</h2>
	 * <p>可以自定义语音和文字消息构成</p>
	 * <p>样例:</p>
	 * <p>List<MessageChain> chains = new FileMessageBuilder(eventPack)</p>
	 * <p>&nbsp;&nbsp;.add("1234\n")</p>
	 * <p>&nbsp;&nbsp;.add("1234\n", "5678\n")</p>
	 * <p>&nbsp;&nbsp;.add(LocalFilePath)</p>
	 * <p>&nbsp;&nbsp;.add(urlPath)</p>
	 * <p>&nbsp;&nbsp;.add(file)</p>
	 * <p>&nbsp;&nbsp;.send();（或.build();）</p>
	 * <p>}</p>
	 *
	 * @param eventPack 事件封装
	 * @author Haythem
	 * @since 1.0.0
	 */
	public FileMessageBuilder(MessageEventPack eventPack) {
		this.messageEventPack = eventPack;
		this.event = eventPack.getEvent();
	}

	/**
	 * <h2>添加群文件消息方法</h2>
	 * <p>支持以下类型输入:</p>
	 * <p></p>
	 * <p>1: MessageChain消息链</p>
	 * <p>2: String...可变长字符串，字符串支持本地路径、URL和文字消息</p>
	 * <p>3: File 打开的文件类</p>
	 * <p></p>
	 * <p>注:</p>
	 * <p>1: 请勿上传大小为0字节的文件</p>
	 * <p>2: URL支持重定向</p>
	 *
	 * @param chain 当前类型: MessageChain 消息链
	 */
	public FileMessageBuilder add(MessageChain chain) {
		this.chains.add(chain);
		return this;
	}

	/**
	 * <h2>添加群文件消息方法</h2>
	 * <p>支持以下类型输入:</p>
	 * <p></p>
	 * <p>1: MessageChain消息链</p>
	 * <p>2: String...可变长字符串，字符串支持本地路径、URL和文字消息</p>
	 * <p>3: File 打开的文件类</p>
	 * <p></p>
	 * <p>注:</p>
	 * <p>1: 请勿上传大小为0字节的文件</p>
	 * <p>2: URL支持重定向</p>
	 *
	 * @param file 当前类型: File 打开的文件类
	 */
	public FileMessageBuilder add(File file) {
		ExternalResource resource = ExternalResource.create(file);
		MessageChain chain = new MessageChainBuilder().build();
		chain = chain.plus(ExternalResource.uploadAsFile(resource, messageEventPack.getGroup(), "/" + file.getName()));
		this.chains.add(chain);
		return this;
	}

	/**
	 * <h2>添加群文件消息方法</h2>
	 * <p>支持以下类型输入:</p>
	 * <p></p>
	 * <p>1: MessageChain消息链</p>
	 * <p>2: String...可变长字符串，字符串支持本地路径、URL和文字消息</p>
	 * <p>3: File 打开的文件类</p>
	 * <p></p>
	 * <p>注:</p>
	 * <p>1: 请勿上传大小为0字节的文件</p>
	 * <p>2: URL支持重定向</p>
	 * <p>3: 当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
	 * <p>&nbsp;&nbsp;"联网获取素材失败"</p>
	 *
	 * @param s 当前类型: String...可变长字符串
	 */
	public FileMessageBuilder add(String... s) {
		for (String i : s) {
			MessageChain chain = new MessageChainBuilder().build();
			Matcher windowsMatcher = windowsPattern.matcher(i);
			Matcher linuxMatcher = linuxPattern.matcher(i);
			if (!(windowsMatcher.find() || linuxMatcher.find()) && !i.contains("http")) {
				chain = chain.plus(i);
				this.chains.add(chain);
			} else {
				ExternalResource resource = ExtResBuilder(i);
				if (isUTTPRequestSuccess) {
					chain = chain.plus(ExternalResource.uploadAsFile(resource, messageEventPack.getGroup(), "/" + FileName));
					this.chains.add(chain);
				} else {
					chain = chain.plus("联网获取数据失败");
					this.chains.add(chain);
				}
			}
		}
		return this;
	}

	/**
	 * <h2>构造器结尾</h2>
	 * <p>该方法返回构造完成的消息链List</p>
	 *
	 * @return List<MessageChain> 消息链List
	 */
	public List<MessageChain> build() {
		return this.chains;
	}

	/**
	 * <h2>构造器结尾</h2>
	 * <p>该方法返回并自动发送构造完成的消息链List</p>
	 * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
	 * <p>"联网获取素材失败"</p>
	 *
	 * @return List<MessageChain> 消息链List
	 */
	public List<MessageChain> send() {
		for (MessageChain messageChain : chains) {
			event.getSubject().sendMessage(messageChain);
		}
		return this.chains;
	}

	private ExternalResource ExtResBuilder(String path) {
		ExternalResource externalResource = null;
		try {
			String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
			String className = Thread.currentThread().getStackTrace()[3].getClassName();
			Class<?> aClass = Class.forName(className);
			Method[] methods = aClass.getDeclaredMethods();
			Method method = null;
			for (Method m : methods) {
				if (m.getName().equals(methodName)) {
					method = m;
				}
			}
			if (path.contains("http")) {//URL
				InputStream inputStream = null;
				if (method.isAnnotationPresent(HttpsProperties.class)) {
					HttpsProperties properties = method.getAnnotation(HttpsProperties.class);
					inputStream = HttpUtil.getInputStream_advanced(path, properties);
				} else {
					inputStream = HttpUtil.getInputStream(path);
				}
				externalResource = Mirai.getInstance().getFileCacheStrategy().newCache(inputStream);
			} else {//LOCAL
				File file = new File(path);
				externalResource = ExternalResource.create(file);
				FileName = file.getName();
			}
		} catch (IOException e) {
			isUTTPRequestSuccess = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return externalResource;
	}
}