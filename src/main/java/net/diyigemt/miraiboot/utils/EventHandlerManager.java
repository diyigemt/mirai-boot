package net.diyigemt.miraiboot.utils;

import net.diyigemt.miraiboot.annotation.*;
import net.diyigemt.miraiboot.constant.EventHandlerType;
import net.diyigemt.miraiboot.constant.EventType;
import net.diyigemt.miraiboot.constant.FunctionId;
import net.diyigemt.miraiboot.entity.*;
import net.diyigemt.miraiboot.interfaces.EventHandlerNext;
import net.diyigemt.miraiboot.interfaces.UnloadHandler;
import net.diyigemt.miraiboot.permission.CheckPermission;
import net.diyigemt.miraiboot.permission.PermissionCheck;
import net.mamoe.mirai.event.ListeningStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static net.diyigemt.miraiboot.constant.ConstantGlobal.DEFAULT_EVENT_NET_TIMEOUT;
import static net.diyigemt.miraiboot.constant.ConstantGlobal.DEFAULT_EVENT_NET_TIMEOUT_TIME;

/**
 * <h2>事件总处理器</h2>
 * 管理着所有注册的事件 包括消息事件和上下文监听事件和强制触发事件<be/>
 * @author diyigemt
 * @author Heythem723
 * @since 1.0.0
 */
public class EventHandlerManager implements UnloadHandler {
  @Override
  public void onUnload(List<PluginItem> pluginItems) {
    for(PluginItem pluginItem : pluginItems){
      if(pluginItem.getAnnotationData() instanceof EventHandler){//过滤掉其它Manager的数据
        //do something...
      }
    }
  }

  /**
   * 单列
   */
  private static final EventHandlerManager INSTANCE = new EventHandlerManager();
  /**
   * <h2>存储所有@EventHandler注册的指令</h2>
   * String: 指令开头与指令名<br/>
   * EventHandlerItem: 存储Handler的信息类
   */
  private static final Map<String, List<EventHandlerItem>> STORE = new HashMap<>();
  /**
   * <h2>存储所有上下文监听注册的指令</h2>
   * Long: 监听的qq号<br/>
   * EventHandlerNextItem: 存储Handler的信息类
   */
  private static final Map<Long, List<EventHandlerNextItem<?, ? extends EventHandlerNext<?>>>> LISTENING_STORE = new HashMap<>();

  /**
   * <h2>存储所有除了消息事件以外的事件</h2>
   * String: 有关的内容 暂时没用
   * EventHandlerItem: 存储Handler的信息类
   */
  private static final Map<String, List<EventHandlerItem>> OTHER_EVENT_STORE = new HashMap<>();

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
  public void onOther(String target, Class<?> invoker, Method handler, List<ExceptionHandlerItem> exceptionHandlers) {
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
    if (eventHandlerItems == null) eventHandlerItems = new ArrayList<>();
    String name = CommandUtil.getInstance().parseHandlerBaseName(invoker, handler);
    EventHandlerItem eventHandlerItem = new EventHandlerItem(name, invoker, handler, type, exceptionHandlers);
    if (!eventHandlerItems.contains(eventHandlerItem)) eventHandlerItems.add(eventHandlerItem);
    OTHER_EVENT_STORE.put(target, eventHandlerItems);
  }

  /**
   * <h2>注册强制执行Handler</h2>
   * @param invoker Handler所在的类
   * @param handler Handler
   */
  public void onAny(Class<?> invoker, Method handler, List<ExceptionHandlerItem> exceptionHandlers) {
    on(HANDLER_ANY_NAME, invoker ,handler, exceptionHandlers);
  }

  /**
   * <h2>注册指令名和与之对应的Handler</h2>
   * @param target 指令
   * @param invoker Handler所在的类
   * @param handler Handler
   */
  public void on(String target, Class<?> invoker, Method handler, List<ExceptionHandlerItem> exceptionHandlers) {
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
    if (eventHandlerItems == null) eventHandlerItems = new ArrayList<>();
    String name = CommandUtil.getInstance().parseHandlerBaseName(invoker, handler);
    EventHandlerItem eventHandlerItem = new EventHandlerItem(name, invoker, handler, type, exceptionHandlers);
    if (!eventHandlerItems.contains(eventHandlerItem)) eventHandlerItems.add(eventHandlerItem);
    STORE.put(target, eventHandlerItems);
  }

  /**
   * <h2>根据指令移除MessageHandler</h2>
   * @param name 指令item的全名(包名+类名+方法名)
   * @return 被移除的Handler列表
   */
  public EventHandlerItem remove(String name) {
    List<EventHandlerItem> items = STORE.get(name);
    return remove(name, items);
  }

  /**
   * <h2>根据指令移除OtherHandler</h2>
   * @param name 指令item的全名(包名+类名+方法名)
   * @return 被移除的Handler列表
   */
  public EventHandlerItem removeOther(String name) {
    List<EventHandlerItem> items = OTHER_EVENT_STORE.get(name);
    return remove(name, items);
  }

  public EventHandlerItem remove(String name, List<EventHandlerItem> items) {
    if (items == null) return null;
    for (EventHandlerItem item : items) {
      if (item.getName().equals(name)) {
        items.remove(item);
        return item;
      }
    }
    return null;
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
        handlerException(e, eventPack, null, null);
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
      boolean isAny = target.equals(HANDLER_ANY_NAME);
      EventHandlerType[] type = handler.getType();
      if (!checkEventType(type, eventPack)) return null;
      Method method = handler.getHandler();
      // 判断Filter 用于过滤强制触发事件
      if (method.isAnnotationPresent(MessageFilter.class) || method.isAnnotationPresent(MessageFilters.class)) {
        if (!CommandUtil.getInstance().checkFilter(eventPack, method, plainText)) {
          continue;
        }
      }
      // 处理权限
      if (handler.getHandler().isAnnotationPresent(CheckPermission.class)) {
        boolean flag = true;
        CheckPermission annotation = method.getAnnotation(CheckPermission.class);
        //获取权限ID
        if(annotation.isStrictRestricted()){
          if(!PermissionCheck.strictRestrictedCheck(eventPack)){
            eventPack.reply("您当前的权限不足以对目标用户操作");
          }
        }
        // 从FunctionId中获取以适应全局permissionIndex的设置
        String permissionName = target;
        // 如果是强制触发处理器 改成由方法名获取
        if (isAny) {
          EventHandler eventHandlerAnnotation = method.getAnnotation(EventHandler.class);
          String s = eventHandlerAnnotation.target();
          String methodName = method.getName();
          String className = handler.getInvoker().getCanonicalName();
          permissionName = s.equals("") ? className + "." + methodName : s;
        }
        int commandId = FunctionId.getMap(permissionName);
        if(PermissionCheck.checkGroupPermission(eventPack, commandId)){
          if(SQLNonTempAuth){
            continue;
          }
          flag = false;
        }
        if(!PermissionCheck.individualAuthCheck(handler, eventPack) && flag){
          eventPack.reply("您没有权限使用该功能");
          continue;
        }
        else {
          if(!PermissionCheck.identityCheck(handler, eventPack) && flag){
            eventPack.reply("权限不足");
            continue;
          }
        }
      }
      // 开始处理@PreProcessor
      int parameterCount = method.getParameterCount();
      Object[] parameters = null;
      PreProcessorData<?> processorData = new PreProcessorData<>();
      // 如果是多参数handler
      if (parameterCount > 1) {
        parameters = new Object[parameterCount];
        parameters[0] = eventPack;
        processorData = CommandUtil.getInstance().parseArgs(plainText, isAny ? "" : target, method, processorData);
        processorData.setText(plainText);
        parameters[1] = processorData;
        // 开始预处理 分离参数之类的
        if (method.isAnnotationPresent(MessagePreProcessor.class)) {
          processorData = CommandUtil.getInstance().parsePreProcessor(eventPack, processorData, method, plainText);
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
        handlerException(e, eventPack, processorData, handler.getExceptionHandlers());
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
  public <T, K extends EventHandlerNext<T>> void onNext(long target, K onNext, MessageEventPack eventPack, PreProcessorData<T> data) {
    onNext(target, onNext, -2, -1, eventPack, data);
  }

  /**
   * <h2>注册一个上下文事件监听器</h2>
   * @param target 监听目标qq号
   * @param onNext 事件handler
   * @param timeOut 超时时间
   */
  public <T, K extends EventHandlerNext<T>> void onNext(long target, K onNext, long timeOut, MessageEventPack eventPack, PreProcessorData<T> data) {
    onNext(target, onNext, timeOut, -1, eventPack, data);
  }

  /**
   * <h2>注册一个上下文事件监听器</h2>
   * @param target 监听目标qq号
   * @param onNext 事件handler
   * @param triggerCount 监听次数
   */
  public <T, K extends EventHandlerNext<T>> void onNext(long target, K onNext, int triggerCount, MessageEventPack eventPack, PreProcessorData<T> data) {
    long time = checkTimeOut(-1L);
    onNext(target, onNext, time, triggerCount, eventPack, data);
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
  public <T, K extends EventHandlerNext<T>> void onNext(long target, K onNext, long timeOut, int triggerCount) {
    onNext(target, onNext, timeOut, triggerCount, null, null);
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
  public <T, K extends EventHandlerNext<T>> void onNext(long target, K onNext, long timeOut, int triggerCount, MessageEventPack eventPack, PreProcessorData<T> data) {
    List<EventHandlerNextItem<?, ? extends EventHandlerNext<?>>> events = LISTENING_STORE.get(target);
    if (events == null) events = new ArrayList<>();
    EventHandlerNextItem<T, K> eventHandlerNextItem = createNextItem(onNext, timeOut, triggerCount);
    final List<EventHandlerNextItem<?, ? extends EventHandlerNext<?>>> finalEvents = events;
    eventHandlerNextItem.setLastEventPack(eventPack);
    eventHandlerNextItem.setLastData(data);
    eventHandlerNextItem.start(new TimerTask() {
      @Override
      public void run() {
        eventHandlerNextItem.onTimeOut();
        handlerNextEnd(finalEvents, eventHandlerNextItem);
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
  public boolean emitNext(long target, MessageEventPack eventPack, String plainText) {
    List<EventHandlerNextItem<?, ? extends EventHandlerNext<?>>> events = LISTENING_STORE.get(target);
    if (events == null) return false;
    for (int i = 0; i < events.size(); i++) {
      EventHandlerNextItem<?, ? extends EventHandlerNext<?>> next = events.get(i);
      EventHandlerNext<?> handler = next.getHandler();
      PreProcessorData<?> data = new PreProcessorData<>();
      data.setText(plainText);
      try {
        Method onNext = handler.getClass().getMethod("onNext", MessageEventPack.class, PreProcessorData.class);
        if (onNext.isAnnotationPresent(MessagePreProcessor.class)) {
          data = CommandUtil.getInstance().parsePreProcessor(eventPack, data, onNext, plainText);
        }
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
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
              handlerNextEnd(events, next);
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
    return false;
  }

  /**
   * <h2>取消所有正在监听的上下文事件</h2>
   * 退出时调用
   */
  public void cancelAll() {
    if (LISTENING_STORE.isEmpty()) return;
    for (List<EventHandlerNextItem<?, ? extends EventHandlerNext<?>>> listeners : LISTENING_STORE.values()) {
      if (listeners.isEmpty()) continue;
      for (EventHandlerNextItem<?, ? extends EventHandlerNext<?>> item : listeners) {
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
  private <T, K extends EventHandlerNext<T>> EventHandlerNextItem<T, K> createNextItem(K onNext, long timeOut, int triggerCount) {
    timeOut = checkTimeOut(timeOut);
    if (triggerCount < 1 ) triggerCount = -1;
    return new EventHandlerNextItem<>(onNext, timeOut, triggerCount);
  }

  /**
   * <h2>将超时时间合法化</h2>
   * @param timeOut 超时时间
   * @return 合法化的超时时间
   */
  private long checkTimeOut(long timeOut) {
    long res = timeOut;
    if (timeOut < -1) {
      Long time = (Long) GlobalConfig.getInstance().get(DEFAULT_EVENT_NET_TIMEOUT);
      res = time == null ? DEFAULT_EVENT_NET_TIMEOUT_TIME : time;
    }
    return res;
  }

  /**
   * <h2>上下文监听收尾工作</h2>
   * @param events 监听事件列表
   * @param next 事件本身
   */
  private void handlerNextEnd(List<EventHandlerNextItem<?, ? extends EventHandlerNext<?>>> events, EventHandlerNextItem<?, ? extends EventHandlerNext<?>> next) {
    next.cancel();
    next.onDestroy();
    events.remove(next);
  }

  private <T extends BaseEventPack> void handlerException(Throwable e, T eventPack, PreProcessorData<?> data, List<ExceptionHandlerItem> handlers) {
    boolean res;
    if (e instanceof InvocationTargetException) {
      Throwable ex = ((InvocationTargetException) e).getTargetException();
      if (handlers == null || handlers.isEmpty()) {
        res = ExceptionHandlerManager.getInstance().emit(ex, eventPack, data);
      } else {
        res = ExceptionHandlerManager.getInstance().handleException(ex, eventPack, data, handlers);
      }
    } else {
      res = ExceptionHandlerManager.getInstance().emit(e, eventPack, data);
    }
    if (!res) {
      e.printStackTrace();
    }
  }
}
