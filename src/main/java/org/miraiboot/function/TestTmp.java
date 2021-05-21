package org.miraiboot.function;

import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;

@EventHandlerComponent
public class TestTmp {
  @EventHandler(target = "tmp")
  public void testTmp(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getGroup().get(1355247243L).sendMessage("heello");
  }
}
