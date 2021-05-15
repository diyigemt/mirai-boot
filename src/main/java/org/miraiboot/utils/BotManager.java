package org.miraiboot.utils;

import net.mamoe.mirai.Bot;

import java.util.HashMap;
import java.util.Map;

public class BotManager {
  private static final BotManager INSTANCE = new BotManager();
  private static final Map<Long, Bot> STORE = new HashMap<Long, Bot>();

  public static BotManager getInstance() { return INSTANCE; }

  public Bot get(long key) {
    return STORE.get(key);
  }

  public void register(long key, Bot bot) {
    STORE.put(key, bot);
  }

  public Bot remove(long key) {
    return STORE.remove(key);
  }

  public Bot logout(long key) {
    Bot remove = STORE.remove(key);
    remove.close();
    return remove;
  }

  public void logoutAll() {
    STORE.forEach((qq, bot) -> {
      bot.close();
    });
    STORE.clear();
  }

  public boolean login(long key) {
    Bot bot = STORE.get(key);
    if (bot == null) return false;
    bot.login();
    return true;
  }

  public boolean loginAll() {
    STORE.forEach((qq, bot) -> {
      bot.login();
    });
    return true;
  }
}
