package net.diyigemt.miraiboot.interfaces;

import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;

public interface MessageProcessor<T> {
	PreProcessorData<T> parseMessage(String source, MessageEventPack eventPack, PreProcessorData<?> data);
}
