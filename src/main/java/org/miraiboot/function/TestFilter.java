package org.miraiboot.function;


import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MessageFilter;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.constant.MessageFilterMatchType;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;

@EventHandlerComponent
public class TestFilter {

  @EventHandler(target = "filter1")
  @CheckPermission(isAdminOnly = true, permissionIndex = 3)
  @MessageFilter(accounts = "1355247243")
  public void testFilter1(MessageEvent event, PreProcessorData data) {
    event.getSubject().sendMessage("filter1");
  }

  @EventHandler(target = "filter2")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(isAt = true)
  public void testFilter2(MessageEvent event, PreProcessorData data) {
    event.getSubject().sendMessage("filter2");
  }

  @EventHandler(target = "filter3")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(isAtAll = true)
  public void testFilter3(MessageEvent event, PreProcessorData data) {
    event.getSubject().sendMessage("filter3");
  }

  @EventHandler(target = "filter4")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(isAtAny = true)
  public void testFilter4(MessageEvent event, PreProcessorData data) {
    event.getSubject().sendMessage("filter4");
  }

  @EventHandler(target = "filter5")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(value = "filter5", matchType = MessageFilterMatchType.EQUALS)
  public void testFilter5(MessageEvent event, PreProcessorData data) {
    event.getSubject().sendMessage("filter5");
  }
}
