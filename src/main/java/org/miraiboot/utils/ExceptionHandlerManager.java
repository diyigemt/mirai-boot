package org.miraiboot.utils;


import org.miraiboot.entity.ExceptionHandlerItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <h2>异常总处理器</h2>
 * 管理着所有通过@ExceptionHandler注册的方法
 * @author diyigemt
 * @since 1.0.0
 */
public class ExceptionHandlerManager {
  /**
   * 全局唯一实例
   */
  private static final ExceptionHandlerManager INSTANCE = new ExceptionHandlerManager();
  /**
   * 存储所有通过@ExceptionHandler注册的方法
   */
  private static final Map<String, List<ExceptionHandlerItem>> STORE = new HashMap<String, List<ExceptionHandlerItem>>();

  /**
   * 获取全局唯一实例
   * @return 全局唯一实例
   */
  public static ExceptionHandlerManager getInstance() { return INSTANCE; }

  /**
   * 注册一个异常监听器
   * @param target 监听的异常的全类名
   * @param invoker 执行的类
   * @param handler 执行的方法
   * @param priority 执行优先级
   */
  public void on(String target, Class<?> invoker, Method handler, int priority) {
    List<ExceptionHandlerItem> exceptionHandlerItems = STORE.get(target);
    if (exceptionHandlerItems == null) exceptionHandlerItems = new ArrayList<ExceptionHandlerItem>();
    ExceptionHandlerItem item = new ExceptionHandlerItem(target, invoker, handler, priority);
    if (!exceptionHandlerItems.contains(item)) {
      exceptionHandlerItems.add(item);
      Collections.sort(exceptionHandlerItems);
    }
    STORE.put(target, exceptionHandlerItems);
  }

  /**
   * 触发异常类对应的异常处理器
   * @param target 全类名
   * @param e 异常本体
   * @param <T> 异常泛型
   * @return 处理结果 正常返回null 异常返回信息字符串
   */
  public <T extends Throwable> String emit(String target, T e) {
    List<ExceptionHandlerItem> exceptionHandlerItems = STORE.get(target);
    if (exceptionHandlerItems == null) return null;
    int priority = 0;
    boolean isBlock = false;
    boolean lock = true;
    for (ExceptionHandlerItem item : exceptionHandlerItems) {
      if (isBlock && item.getPriority() < priority) continue;
      Class<?> invoker = item.getInvoker();
      Method handler = item.getHandler();
      priority = item.getPriority();
      try {
        Object o = handler.invoke(invoker.getDeclaredConstructor().newInstance(), e);
        if (o != null && lock) {
          isBlock = Boolean.parseBoolean(o.toString());
          if (isBlock) {
            lock = false;
          }
        }
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException ex) {
        ex.printStackTrace();
        return "执行exception handler: " + target + " 时出错!";
      }
    }
    return null;
  }

  /**
   * 根据异常类移除对应的异常监听器组
   * @param target
   * @return
   */
  public List<ExceptionHandlerItem> remove(Class<? extends Exception> target) {
    String s = target.getCanonicalName();
    return STORE.remove(s);
  }
}
