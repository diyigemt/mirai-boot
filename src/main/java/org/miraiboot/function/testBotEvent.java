package org.miraiboot.function;

import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.BotNickChangedEvent;
import net.mamoe.mirai.event.events.MemberCardChangeEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.constant.EventHandlerType;
import org.miraiboot.entity.BotEventPack;

@EventHandlerComponent
public class testBotEvent {

  @EventHandler(type = EventHandlerType.OTHER_HANDLER)
  public void testBotEvent(BotEventPack eventPack) {
    BotEvent event = eventPack.getEvent();
    if (event instanceof MemberCardChangeEvent) {
      String aNew = ((MemberCardChangeEvent) event).getNew();
      event.getBot().getGroup(1002484182).sendMessage("change to: " + aNew);
    }
  }
}
