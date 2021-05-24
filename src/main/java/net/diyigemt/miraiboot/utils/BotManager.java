package net.diyigemt.miraiboot.utils;

import net.mamoe.mirai.Bot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <h2>用于bot的统一管理</h2>
 * STORE这个HashMap中存储着所有配置文件中注册的bot的实例<br/>
 * 对应关系为qq号(long) - Bot实例
 * @author diyigemt
 * @since 1.0.0
 */
public class BotManager {
  private static final BotManager INSTANCE = new BotManager();
  private static final Map<Long, Bot> STORE = new HashMap<Long, Bot>();

  /**
   * <h2>Manager获取单例对象</h2>
   * @return Manager实例
   */
  public static BotManager getInstance() { return INSTANCE; }

  /**
   * <h2>根据qq号获取Bot实例</h2>
   * @param key qq号
   * @return 对应的Bot实例 没有时为null
   */
  public Bot get(long key) {
    return STORE.get(key);
  }

  /**
   * <h2>得到所有注册的Bot实例</h2>
   * @return 所有注册的Bot实例
   */
  public Collection<Bot> getAllBot() {
    return STORE.values();
  }

  /**
   * <h2>注册一个Bot实例</h2>
   * @param key qq号
   * @param bot Bot实例
   */
  public void register(long key, Bot bot) {
    STORE.put(key, bot);
  }

  /**
   * <h2>根据qq号删除一个Bot实例</h2>
   * @param key qq号
   * @return 被删除的Bot实例 没有时为null
   */
  public Bot remove(long key) {
    return STORE.remove(key);
  }

  /**
   * <h2>将qq号对应的Bot实例登出</h2>
   * @param key qq号
   * @return 登出的Bot实例
   */
  public Bot logout(long key) {
    Bot remove = STORE.get(key);
    remove.close();
    return remove;
  }

  /**
   * <h2>将所有注册的Bot实例登出</h2>
   */
  public void logoutAll() {
    STORE.forEach((qq, bot) -> {
      bot.close();
    });
  }

  /**
   * <h2>将qq号对应的Bot实例登录</h2>
   * @param key qq号
   * @return 登录的Bot实例
   */
  public boolean login(long key) {
    Bot bot = STORE.get(key);
    if (bot == null) return false;
    bot.login();
    return true;
  }

  /**
   * <h2>将所有注册的Bot实例登入</h2>
   */
  public void loginAll() {
    STORE.forEach((qq, bot) -> {
      bot.login();
    });
  }
}
