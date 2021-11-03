package net.diyigemt.miraiboot.utils;

import net.diyigemt.miraiboot.annotation.AutoInit;
import net.diyigemt.miraiboot.annotation.TimerHandler;
import net.diyigemt.miraiboot.entity.*;
import net.diyigemt.miraiboot.interfaces.UnloadHandler;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <h2>周期事件总处理器</h2>
 * 管理着所有注册的周期事件<br/>
 * 内部使用Quartz来完成
 * @author diyigemt
 * @author Heythem723
 * @since 1.0.7
 */
@AutoInit
public class TimerHandlerManager implements UnloadHandler {

  /**
   * 单列
   */
  private static final TimerHandlerManager INSTANCE = new TimerHandlerManager();
  /**
   * <h2>存储所有@EventHandler注册的指令</h2>
   * String: 用户定义的name<br/>
   * EventHandlerItem: 存储Handler的信息类
   */
  private static final Map<String, TimerItem> STORE = new HashMap<>();

  private static Scheduler schedule;

  public static void init() {
    try {
      schedule = new StdSchedulerFactory().getScheduler();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }
  /**
   * <h2>获取单列对象</h2>
   * @return EventHandlerManager
   */
  public static TimerHandlerManager getInstance() {
    return INSTANCE;
  }

  /**
   * 根据全类名移除
   * @param name
   * @return
   */
  public TimerItem remove(String name) {
    return STORE.remove(name);
  }

  /**
   * 卸载插件
   * @param pluginItems PluginMgr提供的类清单
   */
  @Override
  public void onUnload(List<PluginItem> pluginItems) {
    for (PluginItem pluginItem : pluginItems) {
      Annotation annotationData = pluginItem.getAnnotationData();
      if (!(annotationData instanceof TimerHandler)) {
        continue;
      }
      TimerHandler handler = (TimerHandler) annotationData;
      remove(handler.name());
    }
  }
}
