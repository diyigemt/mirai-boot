package net.diyigemt.miraiboot.listener;

import net.diyigemt.miraiboot.entity.BotEventPack;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.EventHandlerManager;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;

import java.util.function.Consumer;

/**
 * BotEvent监听器
 * @author diyigemt
 * @since 1.0.0
 */
public class BotEventListener implements Consumer<BotEvent> {
  public static boolean eventLoggerEnable = true;

  @Override
  public void accept(BotEvent botEvent) {
    if (botEvent instanceof GroupMessageEvent) return;
    if (botEvent instanceof FriendMessageEvent) return;
    if (botEvent instanceof GroupTempMessageEvent) return;
    BotEventPack eventPack = new BotEventPack(botEvent);
    String res = EventHandlerManager.getInstance().emitOther("", eventPack);
    if (res != null && eventLoggerEnable) {
      MiraiMain.logger.error(res);
    }
  }
}
