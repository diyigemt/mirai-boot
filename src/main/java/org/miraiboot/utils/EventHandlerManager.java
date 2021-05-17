package org.miraiboot.utils;

import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.*;
import org.miraiboot.constant.EventHandlerType;
import org.miraiboot.entity.EventHandlerItem;
import org.miraiboot.entity.EventHandlerNextItem;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.interfaces.EventHandlerNext;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.permission.PermissionCheck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventHandlerManager {
  private static final EventHandlerManager INSTANCE = new EventHandlerManager();
  private static final Map<String, List<EventHandlerItem>> STORE = new HashMap<String, List<EventHandlerItem>>();
  private static final Map<Long, List<EventHandlerNextItem>> LISTENING_STORE = new HashMap<Long, List<EventHandlerNextItem>>();

  public static EventHandlerManager getInstance() {
    return INSTANCE;
  }

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
      if (handler.getHandler().isAnnotationPresent(CheckPermission.class)) {
        //获取权限ID
        if(method.getAnnotation(CheckPermission.class).isStrictRestricted()){
          CommandUtil commandUtil = new CommandUtil();
          PreProcessorData data = new PreProcessorData();
          String source = event.getMessage().serializeToMiraiCode();
          data = commandUtil.parseArgs(source, handler.getHandler(), data);
          if(!PermissionCheck.strictRestrictedCheck((GroupMessageEvent) event, data)){
            MiraiMain.getInstance().quickReply(event, "您当前的权限不足以对目标用户操作");
            return "您当前的权限不足以对目标用户操作";
          }
        }
        int commandId = method.getAnnotation(CheckPermission.class).permissionIndex();
        if(!PermissionCheck.checkGroupPermission(handler, (GroupMessageEvent) event, commandId)){
          MiraiMain.getInstance().quickReply(event, "您的管理员已禁止您使用该功能");
          return "您的管理员已禁止您使用该功能";
        }
        else {
          if(!PermissionCheck.identityCheck(handler, (GroupMessageEvent) event)){
            MiraiMain.getInstance().quickReply(event, "权限不足");
            return "权限不足";
          }
        }
      }

      // 开始处理@Filter 和 @PreProcessor
      // 判断Filter
      if (method.isAnnotationPresent(MessageFilter.class) || method.isAnnotationPresent(MessageFilters.class)) {
        if (!CommandUtil.getInstance().checkFilter(event, method, plainText)) return "filter 未通过";
      }
      int parameterCount = method.getParameterCount();
      Object[] parameters = null;
      PreProcessorData processorData = new PreProcessorData();
      // 如果是多参数handler
      if (parameterCount != 1) {
        parameters = new Object[parameterCount];
        parameters[0] = event;
        processorData = CommandUtil.getInstance().parseArgs(plainText, method, processorData);
        processorData.setText(plainText);
        parameters[1] = processorData;
        // 开始预处理 分离参数之类的
        if (method.isAnnotationPresent(MessagePreProcessor.class) || method.isAnnotationPresent(MessagePreProcessors.class)) {
          processorData = CommandUtil.getInstance().parsePreProcessor(event, method, processorData);
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

  public void onNext(long target, EventHandlerNext onNext) {
    onNext(target, onNext, -1L, -1);
  }

  public void onNext(long target, EventHandlerNext onNext, long timeOut) {
    onNext(target, onNext, timeOut, -1);
  }

  public void onNext(long target, EventHandlerNext onNext, int triggerCount) {
    onNext(target, onNext, -1L, triggerCount);
  }

  public void onNext(long target, EventHandlerNext onNext, long timeOut, int triggerCount) {
    List<EventHandlerNextItem> events = LISTENING_STORE.get(target);
    if (events == null) events = new ArrayList<EventHandlerNextItem>();
    EventHandlerNextItem eventHandlerNextItem = new EventHandlerNextItem(onNext, timeOut, triggerCount);
    events.add(eventHandlerNextItem);
    LISTENING_STORE.put(target, events);
  }

  public void onNext(long target, EventHandlerNext onNext, long timeOut, int triggerCount, MessageEvent event, PreProcessorData data) {
    List<EventHandlerNextItem> events = LISTENING_STORE.get(target);
    if (events == null) events = new ArrayList<EventHandlerNextItem>();
    EventHandlerNextItem eventHandlerNextItem = new EventHandlerNextItem(onNext, timeOut, triggerCount);
    if (timeOut != -1) {
      List<EventHandlerNextItem> finalEvents = events;
      eventHandlerNextItem.setLastEvent(event);
      eventHandlerNextItem.setLastData(data);
      eventHandlerNextItem.start(new TimerTask() {
        @Override
        public void run() {
          eventHandlerNextItem.onTimeOut();
          eventHandlerNextItem.onDestroy();
          finalEvents.remove(eventHandlerNextItem);
          if (finalEvents.isEmpty()) LISTENING_STORE.remove(target);
        }
      });
    }
    events.add(eventHandlerNextItem);
    LISTENING_STORE.put(target, events);
  }

  public String emitNext(long target, MessageEvent event, String plainText) {
    List<EventHandlerNextItem> events = LISTENING_STORE.get(target);
    if (events == null) return null;
    for (int i = 0; i < events.size(); i++) {
      EventHandlerNextItem next = events.get(i);
      EventHandlerNext handler = next.getHandler();
      PreProcessorData data = new PreProcessorData();
      data.setText(plainText);
      try {
        Method onNext = handler.getClass().getMethod("onNext", MessageEvent.class, PreProcessorData.class);
        data = CommandUtil.getInstance().parsePreProcessor(event, onNext, data);
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
        return "没有找到该方法!";
      }
      if (next.check()) {
        ListeningStatus status = next.onNext(event, data);
        if (status == ListeningStatus.STOPPED) {
          next.cancel();
          next.onDestroy();
          events.remove(next);
          i--;
        } else {
          next.start(new TimerTask() {
            @Override
            public void run() {
              next.onTimeOut();
              next.onDestroy();
              events.remove(next);
            }
          });
        }
      } else {
        next.cancel();
        next.onDestroy();
        events.remove(next);
        i--;
      }
    }
    if (events.isEmpty()) LISTENING_STORE.remove(target);
    return null;
  }

  public PreProcessorData parsePreProcessorData(MessageEvent event, Method handler, PreProcessorData data) {
    if (handler.isAnnotationPresent(MessagePreProcessor.class) || handler.isAnnotationPresent(MessagePreProcessors.class)) {
      data = CommandUtil.getInstance().parsePreProcessor(event, handler, data);
    }
    return data;
  }
}
