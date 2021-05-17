package org.miraiboot.interfaces;

import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.entity.PreProcessorData;

public abstract class EventHandlerNext {

  public abstract ListeningStatus onNext(MessageEvent event, PreProcessorData data);

  public void onDestroy(MessageEvent event, PreProcessorData data) {

  }

  public void onTimeOut(MessageEvent event, PreProcessorData data) {

  }

  public void onTriggerOut(MessageEvent event, PreProcessorData data) {

  }
}
