package net.diyigemt.miraiboot.entity;

import net.diyigemt.miraiboot.interfaces.IMessageFilter;

public class MessageFilterImp implements IMessageFilter {
  @Override
  public boolean check(String source, MessageEventPack eventPack, MessageFilterItem item) {
    return item.check(eventPack, source);
  }
}
