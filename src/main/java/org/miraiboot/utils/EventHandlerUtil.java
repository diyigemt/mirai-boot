package org.miraiboot.utils;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.constant.EventHandlerType;
import org.miraiboot.entity.EventHandlerItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventHandlerUtil {
  private static final EventHandlerUtil INSTANCE = new EventHandlerUtil();
  private static final Map<String, List<EventHandlerItem>> STORE = new HashMap<String, List<EventHandlerItem>>();

  public static EventHandlerUtil getInstance() { return INSTANCE; }

  public void on(String target, Class<?> invoker, Method handler) {
    List<EventHandlerItem> eventHandlerItems = STORE.get(target);
    if (eventHandlerItems == null) eventHandlerItems = new ArrayList<EventHandlerItem>();
    EventHandler annotation = handler.getAnnotation(EventHandler.class);
    EventHandlerType[] type = annotation.type();
    EventHandlerItem eventHandlerItem = new EventHandlerItem(target, invoker, handler, type);
    eventHandlerItems.add(eventHandlerItem);
    STORE.put(target, eventHandlerItems);
  }

  public List<EventHandlerItem> remove(String target) {
    return STORE.remove(target);
  }

  public boolean emit(String target, MessageEvent event) {
    List<EventHandlerItem> eventHandlerItems = STORE.get(target);
    if (eventHandlerItems == null) return false;
    for (EventHandlerItem handler : eventHandlerItems) {
      EventHandlerType[] type = handler.getType();
      if (!checkEventType(type, event)) return false;
      Method method = handler.getHandler();
      Class<?> invoker = handler.getInvoker();
      try {
        method.invoke(invoker.getDeclaredConstructor().newInstance(), event);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  private boolean checkEventType(EventHandlerType[] types, MessageEvent event) {
    for (EventHandlerType type : types) {
      if (type == EventHandlerType.MESSAGE_HANDLER_ALL) return true;
      if (event instanceof FriendMessageEvent && type == EventHandlerType.MESSAGE_HANDLER_FRIEND) return true;
      if (event instanceof GroupMessageEvent && type == EventHandlerType.MESSAGE_HANDLER_GROUP) return true;
    }
    return false;
  }
}
