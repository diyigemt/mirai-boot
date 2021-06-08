package net.diyigemt.miraiboot.utils;

import net.diyigemt.miraiboot.annotation.ExceptionHandler;
import net.diyigemt.miraiboot.entity.*;
import net.diyigemt.miraiboot.interfaces.UnloadHandler;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.mamoe.mirai.event.events.BotEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static net.diyigemt.miraiboot.constant.ConstantException.MAX_PARAM_COUNT;

/**
 * <h2>异常总处理器</h2>
 * 管理着所有通过@ExceptionHandler注册的方法
 *
 * @author diyigemt
 * @since 1.0.0
 */
public class ExceptionHandlerManager implements UnloadHandler {
  @Override
  public void onUnload(List<PluginItem> pluginItems) {
    for(PluginItem pluginItem : pluginItems){
      if(pluginItem.getAnnotationData() instanceof ExceptionHandler){//过滤掉其它Manager的数据
        //do something...
      }
    }
  }

  /**
   * 全局唯一实例
   */
  private static final ExceptionHandlerManager INSTANCE = new ExceptionHandlerManager();
  /**
   * 存储所有通过@ExceptionHandler注册的方法
   */
  private static final List<ExceptionHandlerItem> STORE = new ArrayList<>();

  /**
   * 获取全局唯一实例
   *
   * @return 全局唯一实例
   */
  public static ExceptionHandlerManager getInstance() {
    return INSTANCE;
  }

  /**
   * 注册一个异常监听器
   * @param item 监听器本体
   */
  public void on(ExceptionHandlerItem item) {
    STORE.add(item);
  }

  /**
   * 触发异常类对应的异常处理器
   * @param e         异常本体
   * @param <T>       异常泛型
   * @return 处理结果 正常返回null 异常返回信息字符串
   */
  public <T extends Throwable> boolean emit(T e) {
    return emit(e, null, null);
  }

  /**
   * 触发异常类对应的异常处理器
   *
   * @param e         异常本体
   * @param eventPack 引发的事件 可以为null
   * @param data      引发事件的数据 可以为null
   * @param <T>       异常泛型
   * @return 处理结果 正常返回null 异常返回信息字符串
   */
  public <T extends Throwable, K extends BaseEventPack> boolean emit(T e, K eventPack, PreProcessorData<?> data) {
    return handleException(e, eventPack, data, STORE);
  }

  /**
   * 根据异常处理器的名称移除一个异常处理器
   * @param name 要移除的异常处理器的名称
   * @return 被移除的异常处理器
   */
  public ExceptionHandlerItem remove(String name) {
    for (int i = 0; i < STORE.size(); i++) {
      ExceptionHandlerItem item = STORE.get(i);
      if (item.getName().equals(name)) {
        return STORE.remove(i);
      }
    }
    return null;
  }

  /**
   * 处理异常
   * @param e 异常
   * @param eventPack 发生异常的事件
   * @param data 发生异常的预处理数据
   * @param handlers 存储处理该异常handler的list
   * @param <T> 异常类
   * @param <K> 异常事件类型
   * @return 有对应的异常处理类并且执行成功返回true
   */
  public <T extends Throwable, K extends BaseEventPack> boolean handleException(T e, K eventPack, PreProcessorData<?> data, List<ExceptionHandlerItem> handlers) {
    if (handlers == null || handlers.isEmpty()) return false;
    List<ExceptionHandlerItem> collect = handlers.stream().filter(item -> item.check(e.getClass())).collect(Collectors.toList());
    if (collect.isEmpty()) return false;
    // 排序
    Collections.sort(collect);
    int priority = 0;
    boolean isBlock = false;
    boolean lock = true;
    for (ExceptionHandlerItem item : collect) {
      if (isBlock && item.getPriority() < priority) continue;
      Class<?> invoker = item.getInvoker();
      Method handler = item.getHandler();
      int parameterCount = handler.getParameterCount();
      Object[] param = new Object[parameterCount];
      Object[] pre = new Object[]{e, eventPack, data};
      for (int i = 0; i < parameterCount; i++) {
        if (i >= MAX_PARAM_COUNT) {
          param[i] = null;
          continue;
        }
        param[i] = pre[i];
      }
      priority = item.getPriority();
      try {
        Object o = handler.invoke(invoker.getDeclaredConstructor().newInstance(), param);
        if (o != null && lock) {
          isBlock = Boolean.parseBoolean(o.toString());
          if (isBlock) {
            lock = false;
          }
        }
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException ex) {
        ex.printStackTrace();
        return false;
      }
    }
    return true;
  }
}
