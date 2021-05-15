package org.miraiboot.function;


import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;

@EventHandlerComponent
public class TestFunction {

  @EventHandler(target = "reply")
  public void testReply(MessageEvent event) {
    event.getSubject().sendMessage(event.getMessage());
  }
}
