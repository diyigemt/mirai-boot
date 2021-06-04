package net.diyigemt.miraiboot.utils;

import net.diyigemt.miraiboot.constant.ConstantGlobal;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>全局配置</h2>
 * 保存着默认配置和从配置文件中config项读取到的配置<br/>
 * 单例
 * @author diyigemt
 * @since 1.0.0
 */
public class GlobalConfig {
  /**
   * 唯一实例
   */
  private static final GlobalConfig INSTANCE = new GlobalConfig();
  /**
   * 存储所有配置的Hash表
   */
  private static final Map<String, Object> STORE = new HashMap<String, Object>();

  /**
   * <h2>获取单例</h2>
   * @return 单例
   */
  public static GlobalConfig getInstance() { return INSTANCE; }

  /**
   * <h2>根据key获取内容</h2>
   * @param key 键
   * @return 值
   */
  public Object get(String key) { return STORE.get(key); }

  /**
   * <h2>根据key获取内容</h2>
   * @param key 键
   * @return 值
   */
  public <T> T get(String key, T defaultValue) {
    Object o = STORE.get(key);
    T res  = defaultValue;
    try {
       res = (T) o;
    } catch (ClassCastException e) {
      ExceptionHandlerManager.getInstance().emit(e);
    }
    return o == null ? defaultValue : res;
  }

  /**
   * <h2>存入一个配置</h2>
   * @param key 键
   * @param o 值
   */
  public void put(String key, Object o) { STORE.put(key, o); }

  /**
   * <h2>根据key获取boolean内容</h2>
   * @param key 键
   * @param defaultValue 默认值 获取失败时返回
   * @return 值
   */
  public boolean getBoolean(String key, boolean defaultValue) {
    Object o = STORE.get(key);
    return o == null ? defaultValue : Boolean.parseBoolean(o.toString());
  }

  /**
   * <h2>批量存入键值对</h2>
   * @param map 键值对
   */
  public void putAll(Map<String, Object> map) { STORE.putAll(map); }

  /**
   * <h2>读取配置文件后调用</h2>
   * 目前只是拿来注册全局指令开头的
   */
  public void init() {
    // 注册全局指令头
    Object o = get(ConstantGlobal.DEFAULT_COMMAND_START);
    if (o != null && !o.toString().equals("")) CommandUtil.getInstance().registerCommandStart(o.toString());
  }
}
