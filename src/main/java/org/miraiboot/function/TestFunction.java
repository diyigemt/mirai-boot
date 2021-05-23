package org.miraiboot.function;

import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;

@EventHandlerComponent
public class TestFunction{

  @EventHandler(target = "reply")
  @CheckPermission(isAdminOnly = true, FunctionID = FunctionId.reply)
  public void testReply(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getSubject().sendMessage(eventPack.getMessage());
  }
}
