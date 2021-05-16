package org.miraiboot.utils;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.*;
import org.miraiboot.constant.EventHandlerType;
import org.miraiboot.entity.EventHandlerItem;
import org.miraiboot.entity.PreProcessorData;
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

  public String emit(String target, MessageEvent event, String plainText) {
    List<EventHandlerItem> eventHandlerItems = STORE.get(target);
    if (eventHandlerItems == null) return null;
    for (EventHandlerItem handler : eventHandlerItems) {
      EventHandlerType[] type = handler.getType();
      if (!checkEventType(type, event)) return null;
      Method method = handler.getHandler();

      // TODO 处理权限
      if(handler.getHandler().isAnnotationPresent(CheckPermission.class)){
        //获取权限ID
        int commandid = method.getAnnotation(CheckPermission.class).permissionIndex();
        if(!PermissionCheck.checkGroupPermission(handler, (GroupMessageEvent) event, commandid)){
          MiraiMain.getInstance().quickReply(event, "您的管理员已禁止您使用该功能");
          return "您的管理员已禁止您使用该功能";
        }
        else {
          if(!PermissionCheck.idenitityCheck(handler, (GroupMessageEvent) event)){
            MiraiMain.getInstance().quickReply(event, "权限不足");
            return "权限不足";
          }
        }
      }

      // 开始处理@Filter 和 @PreProcessor
      int parameterCount = method.getParameterCount();
      Object[] parameters = null;
      PreProcessorData processorData = null;
      // 如果是多参数handler
      if (parameterCount != 1) {
        parameters = new Object[parameterCount];
        parameters[0] = event;
        parameters[1] = null;
        // 开始预处理 分离参数之类的
        processorData = new PreProcessorData();
        if (method.isAnnotationPresent(MessageFilter.class) || method.isAnnotationPresent(MessageFilters.class)) {
          processorData.setText(plainText);
          processorData.setCommand(target);
          processorData = CommandUtil.getInstance().parseArgs(plainText, method, processorData);
          parameters[1] = processorData;
        }
        if (method.isAnnotationPresent(MessagePreProcessor.class) || method.isAnnotationPresent(MessagePreProcessors.class)) {
          processorData = CommandUtil.getInstance().parsePreProcessor(method, processorData);
          parameters[1] = processorData;
        }
      }
      Class<?> invoker = handler.getInvoker();
      try {
        if (parameterCount != 1) {
          method.invoke(invoker.getDeclaredConstructor().newInstance(), parameters);
        } else {
          method.invoke(invoker.getDeclaredConstructor().newInstance(), event);
        }
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
        e.printStackTrace();
        return "事件执行出错";
      }
    }
    return null;
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
