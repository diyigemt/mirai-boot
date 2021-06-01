package net.diyigemt.miraiboot.interfaces;

import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;

public interface MessageProcessor<T> {
	PreProcessorData<?> parseMessage(String source, MessageEventPack eventPack, PreProcessorData<T> data);
}
