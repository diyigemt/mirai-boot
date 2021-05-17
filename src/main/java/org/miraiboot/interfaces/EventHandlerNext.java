package org.miraiboot.interfaces;

import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.entity.PreProcessorData;

public interface EventHandlerNext {
  ListeningStatus onNext(MessageEvent event, PreProcessorData data);
}
