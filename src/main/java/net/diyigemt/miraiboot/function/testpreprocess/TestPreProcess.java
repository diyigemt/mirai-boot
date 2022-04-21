package net.diyigemt.miraiboot.function.testpreprocess;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessagePreProcessor;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;

public class TestPreProcess {

  @EventHandler(target = "预处理测试")
  @MessagePreProcessor(filter = TestPreProcessA.class)
  public void testPreProcess1(MessageEventPack eventPack, PreProcessorData<TestDataA> data) {
    eventPack.reply("自定义预处理器数据: " + data.getData().getSource());
  }
}
