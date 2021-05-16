package org.miraiboot.function;


import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.permission.CheckPermission;

@EventHandlerComponent
public class TestFunction{

  @EventHandler(target = "reply")
  @CheckPermission(isAdminOnly = true, permissionIndex = FunctionId.reply)
  public void testReply(MessageEvent event, PreProcessorData data) {
    event.getSubject().sendMessage(event.getMessage());
  }
}
