package org.miraiboot.utils;


import org.miraiboot.entity.ExceptionHandlerItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class ExceptionHandlerManager {
  private static final ExceptionHandlerManager INSTANCE = new ExceptionHandlerManager();
  private static final Map<String, List<ExceptionHandlerItem>> STORE = new HashMap<String, List<ExceptionHandlerItem>>();

  public static ExceptionHandlerManager getInstance() { return INSTANCE; }

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

  public <T extends Throwable> String emit(String target, T e) {
    List<ExceptionHandlerItem> exceptionHandlerItems = STORE.get(target);
    if (exceptionHandlerItems == null) return null;
    for (ExceptionHandlerItem item : exceptionHandlerItems) {
      Class<?> invoker = item.getInvoker();
      Method handler = item.getHandler();
      try {
        handler.invoke(invoker.getDeclaredConstructor().newInstance(), e);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException ex) {
        ex.printStackTrace();
        return "执行exception handler: " + target + " 时出错!";
      }
    }
    return null;
  }

  public List<ExceptionHandlerItem> remove(Class<? extends Exception> target) {
    String s = target.getCanonicalName();
    return STORE.remove(s);
  }
}
