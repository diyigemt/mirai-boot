package org.miraiboot.utils;

import org.miraiboot.constant.ConstantGlobal;

import java.util.HashMap;
import java.util.Map;

public class GlobalConfig {
  private static final GlobalConfig INSTANCE = new GlobalConfig();
  private static final Map<String, Object> STORE = new HashMap<String, Object>();

  public static GlobalConfig getInstance() { return INSTANCE; }

  public Object get(String key) { return STORE.get(key); }

  public void put(String key, Object o) { STORE.put(key, o); }

  public void putAll(Map<String, Object> map) { STORE.putAll(map); }

  public void init() {
    // 注册全局指令头
    Object o = get(ConstantGlobal.DEFAULT_COMMAND_START);
    if (o != null && !o.toString().equals("")) CommandUtil.getInstance().registerCommandStart(o.toString());
  }
}
