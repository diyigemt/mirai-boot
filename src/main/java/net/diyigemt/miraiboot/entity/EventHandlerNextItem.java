package net.diyigemt.miraiboot.entity;

import lombok.Data;
import net.diyigemt.miraiboot.constant.ConstantGlobal;
import net.diyigemt.miraiboot.interfaces.EventHandlerNext;
import net.mamoe.mirai.event.ListeningStatus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <h2>上下文监听信息存储类</h2>
 * 在计时中如果被触发 将会重新开始计时
 * @author diyigemt
 * @since 1.0.0
 */
@Data
public class EventHandlerNextItem<T, K extends EventHandlerNext<T>> {
  /**
   * 超时时间
   * @see ConstantGlobal -> DEFAULT_EVENT_NET_TIMEOUT_TIME
   */
  private long timeOut;
  /**
   * 最高触发次数
   */
  private int triggerCount;
  /**
   * 存储信息的handler类
   * @see EventHandlerNext
   */
  private K handler;
  /**
   * timer用
   */
  private TimerTask task;
  /**
   * 计时的timer
   */
  private Timer timer;
  /**
   * 最后一次被触发时的event<br/>
   * 给超时函数 次数耗尽函数 和销毁函数用
   */
  private MessageEventPack lastEventPack;
  /**
   * 最后一次触发时的data内容<br/>
   * 给超时函数 次数耗尽函数 和销毁函数用
   */
  private PreProcessorData<T> lastData;

  public EventHandlerNextItem() {
    this.timeOut = -1;
    this.triggerCount = -1;
    this.handler = null;
    this.task = null;
  }

  public EventHandlerNextItem(K handler) {
    this.handler = handler;
  }

  public EventHandlerNextItem(K handler, long timeOut, int triggerCount) {
    this(handler);
    this.timeOut = timeOut;
    this.triggerCount = triggerCount;
    if (timeOut != -1 || triggerCount != -1) this.timer = new Timer();
  }

  public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData<?> data) {
    PreProcessorData<T> cast = (PreProcessorData<T>) data;
    this.lastEventPack = eventPack;
    this.lastData = cast;
    if (this.triggerCount != -1) data.setTriggerCount(this.triggerCount);
    return onNextSelf(eventPack, cast);
  }

  public ListeningStatus onNextSelf(MessageEventPack eventPack, PreProcessorData<T> data) {
    try {
      return handler.onNext(eventPack, data);
    } catch (Throwable e) {
      return handlerException(e);
    }
  }

  public void onTimeOut() {
    try {
      handler.onTimeOut(this.lastEventPack, this.lastData);
    } catch (Throwable e) {
      handlerException(e);
    }
  }

  public void onTriggerOut() {
    try {
      handler.onTriggerOut(this.lastEventPack, this.lastData);
    } catch (Throwable e) {
      handlerException(e);
    }
  }

  public void onDestroy() {
    try {
      handler.onDestroy(this.lastEventPack, this.lastData);
    } catch (Throwable e) {
      handlerException(e);
    }
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

  private ListeningStatus handlerException(Throwable e) {
    return handler.onException(e, lastEventPack, lastData);
  }
}
