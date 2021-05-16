package org.miraiboot.utils;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.constant.EventHandlerType;
import org.miraiboot.entity.EventHandlerItem;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.permission.PermissionCheck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventHandlerManager {
  private static final EventHandlerManager INSTANCE = new EventHandlerManager();
  private static final Map<String, List<EventHandlerItem>> STORE = new HashMap<String, List<EventHandlerItem>>();

  public static EventHandlerManager getInstance() { return INSTANCE; }

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
      if(handler.getHandler().isAnnotationPresent(CheckPermission.class)){//检查权限
        int commandid = handler.getHandler().getAnnotation(CheckPermission.class).permissionIndex();//获取权限ID
        if(!PermissionCheck.checkGroupPermission(handler, (GroupMessageEvent) event, commandid)){
          MiraiMain.getInstance().quickReply(event, "您的管理员已禁止您使用该功能");
          return true;
        }
        else {
          if(!PermissionCheck.idenitityCheck(handler, (GroupMessageEvent) event)){
            MiraiMain.getInstance().quickReply(event, "权限不足");
            return true;
          }
        }
      }
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
