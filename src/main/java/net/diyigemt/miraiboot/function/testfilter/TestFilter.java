package net.diyigemt.miraiboot.function.testfilter;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.entity.MessageEventPack;

public class TestFilter {

  @EventHandler(isAny = true)
  @MessageFilter(filter = TestFilterA.class)
  public void testFilter(MessageEventPack eventPack) {
    eventPack.reply("通过");
  }
}
