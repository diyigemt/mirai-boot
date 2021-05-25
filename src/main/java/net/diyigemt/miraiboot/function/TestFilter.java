package net.diyigemt.miraiboot.function;


import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.constant.MessageFilterMatchType;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.permission.CheckPermission;

@EventHandlerComponent
public class TestFilter {

  @EventHandler(target = "filter1")
  @CheckPermission(isAdminOnly = true, functionId = 3)
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
