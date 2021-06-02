package net.diyigemt.miraiboot.interfaces;

import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;

/**
 * <h2>消息预处理器的接口</h2>
 * 所有实现了该接口的类均可以作为消息预处理器
 * @param <T> 处理结果的类
 * @since 1.0.5
 */
public interface IMessagePreProcessor<T> {
	/**
	 * 对消息进行预处理
	 * @param source 消息纯文本
	 * @param eventPack 消息事件的封装
	 * @param data 预处理结果存放类
	 * @return 预处理结果
	 */
	PreProcessorData<?> parseMessage(String source, MessageEventPack eventPack, PreProcessorData<T> data);
}
