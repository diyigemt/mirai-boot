package org.miraiboot.utils;

import net.mamoe.mirai.event.ListeningStatus;
import org.miraiboot.annotation.*;
import org.miraiboot.constant.EventHandlerType;
import org.miraiboot.constant.EventType;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.*;
import org.miraiboot.interfaces.EventHandlerNext;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.permission.PermissionCheck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.miraiboot.constant.ConstantGlobal.DEFAULT_EVENT_NET_TIMEOUT;
import static org.miraiboot.constant.ConstantGlobal.DEFAULT_EVENT_NET_TIMEOUT_TIME;

/**
 * <h2>事件总处理器</h2>
 * 管理着所有注册的事件 包括消息事件和上下文监听事件和强制触发事件<be/>
 * @author diyigemt
 * @author Heythem723
 * @since 1.0.0
 */
public class EventHandlerManager {
  /**
   * 单列
   */
  private static final EventHandlerManager INSTANCE = new EventHandlerManager();
  /**
   * <h2>存储所有@EventHandler注册的指令</h2>
   * String: 指令开头与指令名<br/>
   * EventHandlerItem: 存储Handler的信息类
   */
  private static final Map<String, List<EventHandlerItem>> STORE = new HashMap<String, List<EventHandlerItem>>();
  /**
   * <h2>存储所有上下文监听注册的指令</h2>
   * Long: 监听的qq号<br/>
   * EventHandlerNextItem: 存储Handler的信息类
   */
  private static final Map<Long, List<EventHandlerNextItem<? extends EventHandlerNext>>> LISTENING_STORE = new HashMap<Long, List<EventHandlerNextItem<? extends EventHandlerNext>>>();

  /**
   * <h2>存储所有除了消息事件以外的事件</h2>
   * String: 有关的内容 暂时没用
   * EventHandlerItem: 存储Handler的信息类
   */
  private static final Map<String, List<EventHandlerItem>> OTHER_EVENT_STORE = new HashMap<String, List<EventHandlerItem>>();

  private static final String HANDLER_ANY_NAME = "HANDLER_ANY";

  /**
   * <h2>获取单列对象</h2>
   * @return EventHandlerManager
   */
  public static EventHandlerManager getInstance() {
    return INSTANCE;
  }

  public static boolean SQLNonTempAuth = false;

  /**
   * <h2>注册非消息事件Handler</h2>
   * @param target 消息事件对应名
   * @param invoker Handler所在的类
   * @param handler Handler
   */
  public void onOther(String target, Class<?> invoker, Method handler) {
    // 检查类型
    EventHandler annotation = handler.getAnnotation(EventHandler.class);
    EventHandlerType[] type = annotation.type();
    boolean b = false;
    for (EventHandlerType aType : type) {
      if (aType == EventHandlerType.OTHER_HANDLER) {
        b = true;
        break;
      }
    }
    if (!b) return;
    List<EventHandlerItem> eventHandlerItems = OTHER_EVENT_STORE.get(target);
    if (eventHandlerItems == null) eventHandlerItems = new ArrayList<EventHandlerItem>();

    EventHandlerItem eventHandlerItem = new EventHandlerItem(target, invoker, handler, type);
    if (!eventHandlerItems.contains(eventHandlerItem)) eventHandlerItems.add(eventHandlerItem);
    OTHER_EVENT_STORE.put(target, eventHandlerItems);
  }

  /**
   * <h2>注册强制执行Handler</h2>
   * @param invoker Handler所在的类
   * @param handler Handler
   */
  public void onAny(Class<?> invoker, Method handler) {
    on(HANDLER_ANY_NAME, invoker ,handler);
  }

  /**
   * <h2>注册指令名和与之对应的Handler</h2>
   * @param target 指令
   * @param invoker Handler所在的类
   * @param handler Handler
   */
  public void on(String target, Class<?> invoker, Method handler) {
    // 检查类型
    EventHandler annotation = handler.getAnnotation(EventHandler.class);
    EventHandlerType[] type = annotation.type();
    boolean b = false;
    for (EventHandlerType aType : type) {
      if (aType == EventHandlerType.OTHER_HANDLER) {
        b = true;
        break;
      }
    }
    if (b) return;
    List<EventHandlerItem> eventHandlerItems = STORE.get(target);
    if (eventHandlerItems == null) eventHandlerItems = new ArrayList<EventHandlerItem>();
    EventHandlerItem eventHandlerItem = new EventHandlerItem(target, invoker, handler, type);
    if (!eventHandlerItems.contains(eventHandlerItem)) eventHandlerItems.add(eventHandlerItem);
    STORE.put(target, eventHandlerItems);
  }

  /**
   * <h2>根据指令移除MessageHandler</h2>
   * @param target 指令
   * @return 被移除的Handler列表
   */
  public List<EventHandlerItem> remove(String target) {
    return STORE.remove(target);
  }

  /**
   * <h2>根据指令移除OtherHandler</h2>
   * @param target 指令
   * @return 被移除的Handler列表
   */
  public List<EventHandlerItem> removeOther(String target) {
    return OTHER_EVENT_STORE.remove(target);
  }

  /**
   * <h2>执行非消息事件Handler</h2>
   * @param eventPack 封装事件
   * @param plainText 内容纯文本
   * @return 结果 为null为无事发生
   */
  public String emitAny(MessageEventPack eventPack, String plainText) {
    return emit(HANDLER_ANY_NAME, eventPack, plainText);
  }

  /**
   * <h2>执行非消息事件Handler</h2>
   * @param target 指令
   * @param eventPack 封装事件
   * @return 结果 为null为无事发生
   */
  public String emitOther(String target, BotEventPack eventPack) {
    List<EventHandlerItem> eventHandlerItems = OTHER_EVENT_STORE.get(target);
    if (eventHandlerItems == null) return null;
    for (EventHandlerItem handler: eventHandlerItems) {
      Method method = handler.getHandler();
      Class<?> invoker = handler.getInvoker();
      int count = method.getParameterCount();
      Object[] param = null;
      if (count != 0) {
        param = new Object[count];
        param[0] = eventPack;
      }
      try {
        if (count != 0) {
          method.invoke(invoker.getDeclaredConstructor().newInstance(), param);
        } else {
          method.invoke(invoker.getDeclaredConstructor().newInstance());
        }
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
        handlerException(e);
        return "其他事件执行失败: " + target;
      }
    }
    return null;
  }

  /**
   * <h2>根据指令触发EventHandler</h2>
   * @param target 指令(包含指令开头)
   * @param eventPack 封装事件
   * @param plainText 内容纯文本
   * @return 结果 为null为无事发生
   */
  public String emit(String target, MessageEventPack eventPack, String plainText) {
    List<EventHandlerItem> eventHandlerItems = STORE.get(target);
    if (eventHandlerItems == null) return null;
    for (EventHandlerItem handler : eventHandlerItems) {
      EventHandlerType[] type = handler.getType();
      if (!checkEventType(type, eventPack)) return null;
      Method method = handler.getHandler();
      // TODO 处理权限
      if (handler.getHandler().isAnnotationPresent(CheckPermission.class)) {
        boolean flag = true;
        CheckPermission annotation = method.getAnnotation(CheckPermission.class);
        //获取权限ID
        if(annotation.isStrictRestricted()){
          if(!PermissionCheck.strictRestrictedCheck(eventPack)){
            MiraiMain.getInstance().reply(eventPack, "您当前的权限不足以对目标用户操作");
            return "您当前的权限不足以对目标用户操作";
          }
        }
        // 改成从FunctionId中获取以适应全局permissionIndex的设置
        int commandId = FunctionId.getMap(target);
        if(PermissionCheck.checkGroupPermission(eventPack, commandId)){
          if(SQLNonTempAuth){
            return "您的管理员已禁止您使用该功能";
          }
          flag = false;
        }
        if(!PermissionCheck.individualAuthCheck(handler, eventPack) && flag){
          MiraiMain.getInstance().reply(eventPack, "您没有权限使用该功能");
          return "您没有权限使用该功能";
        }
        else {
          if(!PermissionCheck.identityCheck(handler, eventPack) && flag){
            MiraiMain.getInstance().reply(eventPack, "权限不足");
            return "权限不足";
          }
        }
      }
      // 开始处理@Filter 和 @PreProcessor
      // 判断Filter
      if (method.isAnnotationPresent(MessageFilter.class) || method.isAnnotationPresent(MessageFilters.class)) {
        if (!CommandUtil.getInstance().checkFilter(eventPack, method, plainText)) return "filter 未通过";
      }
      int parameterCount = method.getParameterCount();
      Object[] parameters = null;
      PreProcessorData processorData = new PreProcessorData();
      // 如果是多参数handler
      if (parameterCount > 1) {
        parameters = new Object[parameterCount];
        parameters[0] = eventPack;
        processorData = CommandUtil.getInstance().parseArgs(plainText, target.equals(HANDLER_ANY_NAME) ? "" : target, method, processorData);
        processorData.setText(plainText);
        parameters[1] = processorData;
        // 开始预处理 分离参数之类的
        if (method.isAnnotationPresent(MessagePreProcessor.class) || method.isAnnotationPresent(MessagePreProcessors.class)) {
          processorData = CommandUtil.getInstance().parsePreProcessor(eventPack, method, processorData);
          parameters[1] = processorData;
        }
      }
      Class<?> invoker = handler.getInvoker();
      try {
        if (parameterCount == 0) {
          method.invoke(invoker.getDeclaredConstructor().newInstance());
        } else if (parameterCount > 1) {
          method.invoke(invoker.getDeclaredConstructor().newInstance(), parameters);
        } else {
          method.invoke(invoker.getDeclaredConstructor().newInstance(), eventPack);
        }
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
        handlerException(e);
        return "事件执行出错";
      }
    }
    return null;
  }

  private boolean checkEventType(EventHandlerType[] types, MessageEventPack eventPack) {
    for (EventHandlerType type : types) {
      if (type == EventHandlerType.MESSAGE_HANDLER_ALL) return true;
      EventType eventType = eventPack.getEventType();
      if (eventType == EventType.FRIEND_MESSAGE_EVENT && type == EventHandlerType.MESSAGE_HANDLER_FRIEND) return true;
      if (eventType == EventType.GROUP_MESSAGE_EVENT && type == EventHandlerType.MESSAGE_HANDLER_GROUP) return true;
      if (eventType == EventType.GROUP_TMP_MESSAGE_EVENT && type == EventHandlerType.MESSAGE_HANDLER_TEMP) return true;
      if (eventType == EventType.OTHER_EVENT && type == EventHandlerType.OTHER_HANDLER) return false;
    }
    return false;
  }

  /**
   * <h2>注册一个上下文事件监听器</h2>
   * @param target 监听目标qq号
   * @param onNext 事件handler
   */
  public <T extends EventHandlerNext> void onNext(long target, T onNext) {
    onNext(target, onNext, -2, -1);
  }

  /**
   * <h2>注册一个上下文事件监听器</h2>
   * @param target 监听目标qq号
   * @param onNext 事件handler
   * @param timeOut 超时时间
   */
  public <T extends EventHandlerNext> void onNext(long target, T onNext, long timeOut) {
    onNext(target, onNext, timeOut, -1);
  }

  /**
   * <h2>注册一个上下文事件监听器</h2>
   * @param target 监听目标qq号
   * @param onNext 事件handler
   * @param triggerCount 监听次数
   */
  public <T extends EventHandlerNext> void onNext(long target, T onNext, int triggerCount) {
    Long time = (Long) GlobalConfig.getInstance().get(DEFAULT_EVENT_NET_TIMEOUT);
    if (time == null) time = DEFAULT_EVENT_NET_TIMEOUT_TIME;
    onNext(target, onNext, time, triggerCount);
  }

  /**
   * <h2>注册一个上下文事件监听器</h2>
   * <strong>注意 超时时间将会在该事件被触发后才开始计时 也就是注册监听器时不会计时 如果需要计时请使用onNextNow方法</strong>
   * <strong>每次监听器被触发时倒计时将重置</strong>
   * @param target 监听目标qq号
   * @param onNext 事件handler
   * @param timeOut 超时时间 不合法将使用配置文件中的超时时间或者默认的5min
   * @param triggerCount 监听次数 不合法将设置为-1 表示无限监听
   */
  public <T extends EventHandlerNext> void onNext(long target, T onNext, long timeOut, int triggerCount) {
    List<EventHandlerNextItem<? extends EventHandlerNext>> events = LISTENING_STORE.get(target);
    if (events == null) events = new ArrayList<EventHandlerNextItem<? extends EventHandlerNext>>();
    EventHandlerNextItem<? extends EventHandlerNext> eventHandlerNextItem = createCheckItem(onNext, timeOut, triggerCount);
    events.add(eventHandlerNextItem);
    LISTENING_STORE.put(target, events);
  }

  /**
   * <h2>注册一个上下文事件监听器并开始超时时间计时</h2>
   * <strong>每次监听器被触发时倒计时将重置</strong>
   * @param target 监听目标qq号
   * @param onNext 事件handler
   * @param timeOut 超时时间 不合法将使用配置文件中的超时时间或者默认的5min
   * @param triggerCount 监听次数 不合法将设置为-1 表示无限监听
   * @param eventPack 当前MessageEvent用于给onDestroy之类的用
   * @param data 当前PreProcessorData用于给onDestroy之类的用
   */
  public <T extends EventHandlerNext> void onNextNow(long target, T onNext, long timeOut, int triggerCount, MessageEventPack eventPack, PreProcessorData data) {
    List<EventHandlerNextItem<? extends EventHandlerNext>> events = LISTENING_STORE.get(target);
    if (events == null) events = new ArrayList<EventHandlerNextItem<? extends EventHandlerNext>>();
    EventHandlerNextItem<? extends EventHandlerNext> eventHandlerNextItem = createCheckItem(onNext, timeOut, triggerCount);
    List<EventHandlerNextItem<? extends EventHandlerNext>> finalEvents = events;
    eventHandlerNextItem.setLastEventPack(eventPack);
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
    events.add(eventHandlerNextItem);
    LISTENING_STORE.put(target, events);
  }

  /**
   * <h2>触发上下文监听事件</h2>
   * 一般由MessageEventListener调用
   * @param target 被监听的qq号
   * @param eventPack 触发事件
   * @param plainText 事件内容纯文本
   * @return 结果 为null表示一切正常
   */
  public String emitNext(long target, MessageEventPack eventPack, String plainText) {
    List<EventHandlerNextItem<? extends EventHandlerNext>> events = LISTENING_STORE.get(target);
    if (events == null) return null;
    for (int i = 0; i < events.size(); i++) {
      EventHandlerNextItem<? extends EventHandlerNext> next = events.get(i);
      EventHandlerNext handler = next.getHandler();
      PreProcessorData data = new PreProcessorData();
      data.setText(plainText);
      try {
        Method onNext = handler.getClass().getMethod("onNext", MessageEventPack.class, PreProcessorData.class);
        data = CommandUtil.getInstance().parsePreProcessor(eventPack, onNext, data);
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
        return "没有找到该方法!";
      }
      if (next.check()) {
        ListeningStatus status = next.onNext(eventPack, data);
        if (status == ListeningStatus.STOPPED) {
          handlerNextEnd(events, next);
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
        if (next.getTriggerCount() == 0) {
          next.onNext(eventPack, data);
          next.onTriggerOut();
        }
        handlerNextEnd(events, next);
        i--;
      }
    }
    if (events.isEmpty()) LISTENING_STORE.remove(target);
    return null;
  }

  /**
   * <h2>取消所有正在监听的上下文事件</h2>
   * 退出是调用
   */
  public void cancelAll() {
    if (LISTENING_STORE.isEmpty()) return;;
    for (List<EventHandlerNextItem<? extends EventHandlerNext>> listeners : LISTENING_STORE.values()) {
      if (listeners.isEmpty()) continue;
      for (EventHandlerNextItem<? extends EventHandlerNext> item : listeners) {
        item.cancel();
      }
    }
  }

  /**
   * <h2>为一个指令注册别名</h2>
   * <strong>由于多指令开头的存在 注册时请为目标指令加上开头 别名可以不带开头</strong><br/>
   * <strong>如果别名带了开头 请将开头限定在已经注册了的指令开头中</strong><br/>
   * 例如: 假设全局开头为"/"
   * <per>
   * {@code
   *    /alias /搜图 search
   * }
   * </per>
   * @param target 要注册别名的指令
   * @param alias 要注册的别名
   */
  public void registerAlias(String target, String alias) {
    // 将别名与方法对应起来
    List<EventHandlerItem> eventHandlerItems = STORE.get(target);
    if (eventHandlerItems != null) STORE.put(alias, eventHandlerItems);
    // 将别名与权限对应起来
    FunctionId.registerAlias(target, alias);
  }

  /**
   * <h2>统一注册别名</h2>
   * 读取配置初始化时调用
   * @param alias 所有别名
   */
  public void registerAlias(Map<String, String> alias) {
    if (alias == null) return;
    alias.forEach(this::registerAlias);
  }

  /**
   * <h2>将监听超时时间和次数合法化</h2>
   * @param onNext 事件handler
   * @param timeOut 超时时间 不合法将使用配置文件中的超时时间或者默认的5min
   * @param triggerCount 监听次数 不合法将设置为-1 表示无限监听
   * @return 合法的监听信息存储类
   */
  private <T extends EventHandlerNext> EventHandlerNextItem<? extends EventHandlerNext> createCheckItem(T onNext, long timeOut, int triggerCount) {
    if (timeOut < -1) {
      Long time = (Long) GlobalConfig.getInstance().get(DEFAULT_EVENT_NET_TIMEOUT);
      timeOut = time == null ? DEFAULT_EVENT_NET_TIMEOUT_TIME : time;
    }
    if (triggerCount < 1 ) triggerCount = -1;
    return new EventHandlerNextItem<T>(onNext, timeOut, triggerCount);
  }

  /**
   * <h2>上下文监听收尾工作</h2>
   * @param events 监听事件列表
   * @param next 事件本身
   */
  private void handlerNextEnd(List<EventHandlerNextItem<? extends EventHandlerNext>> events, EventHandlerNextItem next) {
    next.cancel();
    next.onDestroy();
    events.remove(next);
  }

  private void handlerException(Throwable e) {
    String res = "";
    if (e instanceof InvocationTargetException) {
      Throwable ex = ((InvocationTargetException) e).getTargetException();
      res = ExceptionHandlerManager.getInstance().emit(ex.getClass().getCanonicalName(), ex);
    } else {
      res = ExceptionHandlerManager.getInstance().emit(e.getClass().getCanonicalName(), e);
    }
    if (res == null) {
      e.printStackTrace();
    }
  }
}
