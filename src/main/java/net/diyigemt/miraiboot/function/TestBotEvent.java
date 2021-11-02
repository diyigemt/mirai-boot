package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.entity.BotEventPack;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MemberCardChangeEvent;
import net.mamoe.mirai.message.data.Dice;
import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.MessageEventPack;

public class TestBotEvent {

  public void testBotEvent(BotEventPack eventPack) {
    BotEvent event = eventPack.getEvent();
    if (event instanceof MemberCardChangeEvent) {
      String aNew = ((MemberCardChangeEvent) event).getNew();
      event.getBot().getGroup(1002484182).sendMessage("change to: " + aNew);
    }
  }

  @EventHandler(target = "dice")
  public void testDice(MessageEventPack eventPack) {
    eventPack.reply(new Dice(4));
  }
}
