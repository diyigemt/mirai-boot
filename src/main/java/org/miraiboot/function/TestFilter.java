package org.miraiboot.function;


import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MessageFilter;
import org.miraiboot.constant.MessageFilterMatchType;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;

@EventHandlerComponent
public class TestFilter {

  @EventHandler(target = "filter1")
  @CheckPermission(isAdminOnly = true, FunctionID = 3)
  @MessageFilter(accounts = "1355247243")
  public void testFilter1(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getSubject().sendMessage("filter1");
  }

  @EventHandler(target = "filter2")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(isAt = true)
  public void testFilter2(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getSubject().sendMessage("filter2");
  }

  @EventHandler(target = "filter3")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(isAtAll = true)
  public void testFilter3(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getSubject().sendMessage("filter3");
  }

  @EventHandler(target = "filter4")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(isAtAny = true)
  public void testFilter4(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getSubject().sendMessage("filter4");
  }

  @EventHandler(target = "filter5")
  @CheckPermission(isAdminOnly = true)
  @MessageFilter(value = "filter5", matchType = MessageFilterMatchType.EQUALS)
  public void testFilter5(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.getSubject().sendMessage("filter5");
  }
}
