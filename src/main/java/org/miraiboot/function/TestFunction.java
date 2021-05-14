package org.miraiboot.function;


import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.constant.EventHandlerType;

@EventHandlerComponent
public class TestFunction {

  @EventHandler(target = "reply", type = EventHandlerType.GROUP_MESSAGE_HANDLER)
  public void testReply(MessageEvent event) {
    event.getSubject().sendMessage(event.getMessage());
  }
}
