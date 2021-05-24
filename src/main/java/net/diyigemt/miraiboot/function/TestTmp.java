package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.MessageEventPack;

@EventHandlerComponent
public class TestTmp {
  @EventHandler(target = "tmp")
  public void testTmp(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getGroup().get(1355247243L).sendMessage("heello");
  }
}
