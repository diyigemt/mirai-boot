package net.diyigemt.miraiboot.function.testpreprocess;


import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.interfaces.MessageProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPreProcessA implements MessageProcessor<TestDataA> {
  @Override
  public PreProcessorData<?> parseMessage(String source, MessageEventPack eventPack, PreProcessorData<TestDataA> data) {
    TestDataA dataA = new TestDataA();
    dataA.setSource(source);
    data.setData(dataA);
    return data;
  }
}
