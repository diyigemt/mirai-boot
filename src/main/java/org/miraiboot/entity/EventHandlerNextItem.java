package org.miraiboot.entity;

import lombok.Data;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.interfaces.EventHandlerNext;

import java.util.Timer;
import java.util.TimerTask;

@Data
public class EventHandlerNextItem {
  private long timeOut;
  private int triggerCount;
  private EventHandlerNext handler;
  private TimerTask task;
  private Timer timer;
  private MessageEvent lastEvent;
  private PreProcessorData lastData;

  public EventHandlerNextItem() {
    this.timeOut = -1;
    this.triggerCount = -1;
    this.handler = null;
    this.task = null;
  }

  public EventHandlerNextItem(EventHandlerNext handler) {
    this.handler = handler;
  }

  public EventHandlerNextItem(EventHandlerNext handler, long timeOut, int triggerCount) {
    this(handler);
    this.timeOut = timeOut;
    this.triggerCount = triggerCount;
    if (timeOut != -1 || triggerCount != -1) this.timer = new Timer();
  }

  public ListeningStatus onNext(MessageEvent event, PreProcessorData data) {
    this.lastEvent = event;
    this.lastData = data;
    if (this.triggerCount != -1) data.setTriggerCount(this.triggerCount);
    return handler.onNext(event, data);
  }

  public void onTimeOut() {
    handler.onTimeOut(this.lastEvent, this.lastData);
  }

  public void onTriggerOut() {
    handler.onTriggerOut(this.lastEvent, this.lastData);
  }

  public void onDestroy() {
    handler.onDestroy(this.lastEvent, this.lastData);
  }

  public boolean check() {
    if (this.triggerCount == -1 && this.timeOut == -1) return true;
    if (this.triggerCount != -1) this.triggerCount--;
    if (this.triggerCount == 0) {
      return false;
    }
    if (this.timeOut != -1) {
      this.timer.cancel();
      this.timer = new Timer();
    }
    return true;
  }

  public void start(TimerTask task) {
    if (this.timeOut != -1) {
      this.task = task;
      this.timer.schedule(task, this.timeOut);
    }
  }

  public void cancel() {
    this.timer.cancel();
  }
}
