package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.constant.FunctionId;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.permission.CheckPermission;

public class TestFunction{

  @EventHandler(target = "reply")
  @CheckPermission(isAdminOnly = true, functionId = FunctionId.reply)
  public void testReply(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getSubject().sendMessage(eventPack.getMessage());
  }
}
