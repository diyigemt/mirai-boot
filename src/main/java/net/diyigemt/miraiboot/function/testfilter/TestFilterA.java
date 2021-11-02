package net.diyigemt.miraiboot.function.testfilter;

import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.MessageFilterItem;
import net.diyigemt.miraiboot.interfaces.IMessageFilter;

public class TestFilterA implements IMessageFilter {

  @Override
  public boolean check(String source, MessageEventPack eventPack, MessageFilterItem item) {
    if (!source.contains("测试")) throw new IllegalArgumentException("测试");
    return source.contains("测试");
  }
}
