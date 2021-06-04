package net.diyigemt.miraiboot.interfaces;

import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.utils.EventHandlerManager;
import net.diyigemt.miraiboot.utils.ExceptionHandlerManager;
import net.mamoe.mirai.event.ListeningStatus;
import net.diyigemt.miraiboot.entity.MessageEventPack;

/**
 * <h2>用于上下文监听的类</h2>
 * <strong>使用时需要至少实现onNext方法</strong>
 * <T>用于指代预处理数据中用户自定义的数据类型
 * <pre>
 * {@code
 * onNext方法也可以使用@MessagePreProcessor注解
 * 销毁时调用顺序为 onTimeOut | onTriggerOut -> onDestroy
 * 传入的MessageEvent和PreProcessorData均为最后一次触发时的event和data
 * 默认创建时倒计时和剩余次数均不会动作 仅在监听器第一次触发后倒计时和剩余次数才起作用 例如:
 *
 * EventHandlerManager.getInstance().onNext(监听目标qq号, new EventHandlerNext(),
 * 超时时间 不合理时取全局配置或者默认5min, 剩余触发次数 不合理时取1)
 *
 * 除了以上四个参数, 还需要额外传入MessageEvent和PreProcessorData用于销毁和超时事件调用
 * 如果超时时间也对事件本身起作用 需要调用 EventHandlerManager.getInstance().onNextNow()
 * }
 * </pre>
 * <strong>详细用法请参考{@link EventHandlerManager#onNext}</strong><br/>
 * @author diyigemt
 * @since 1.0.0
 */
public abstract class EventHandlerNext<T> {

  /**
   * <h2>上下文监听器方法</h2>
   * 该方法可以使用@MessageEventFilter注解来过滤信息
   * @param eventPack 事件本身
   * @param data 预处理器
   * @return 返回是否继续监听事件 ListeningStatus.LISTENING表示继续监听 STOPPED表示停止监听
   */
  public abstract ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData<T> data);

  /**
   * <h2>监听器销毁时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onDestroy(MessageEventPack eventPack, PreProcessorData<T> data) { }

  /**
   * <h2>监听器超时时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onTimeOut(MessageEventPack eventPack, PreProcessorData<T> data) { }

  /**
   * <h2>监听器触发次数耗尽时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onTriggerOut(MessageEventPack eventPack, PreProcessorData<T> data) { }

  /**
   * <h2>在监听器处理过程中如果抛出异常由此方法处理</h2>
   * 默认直接交给全局异常处理器处理
   * @param e 异常
   * @param eventPack 触发的事件
   * @param data 触发事件的数据
   * @return 是否继续监听
   */
  public ListeningStatus onException(Throwable e, MessageEventPack eventPack, PreProcessorData<T> data) {
    ExceptionHandlerManager.getInstance().emit(e, eventPack, data);
    return ListeningStatus.STOPPED;
  }
}
