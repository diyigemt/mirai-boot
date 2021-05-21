package org.miraiboot.function;


import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MessagePreProcessor;
import org.miraiboot.constant.MessagePreProcessorMessageType;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.interfaces.EventHandlerNext;

import java.util.List;

@EventHandlerComponent
public class TestNext {

  @EventHandler(target = "搜图")
  @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
  public void testTimeOut(MessageEventPack eventPack, PreProcessorData data) {
    List<SingleMessage> filter = data.getClassified();
    if (filter.isEmpty()) {
      eventPack.reply("图呢");
      eventPack.onNextNow(new EventHandlerNext() {
        @Override
        @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
        public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData nextData) {
          if (nextData.getText().contains("没有")) {
            eventPack.reply("停止监听");
            return ListeningStatus.STOPPED;
          }
          List<SingleMessage> filter = nextData.getClassified();
          if (filter.isEmpty()) {
            eventPack.reply("等着呢 还是没有");
            return ListeningStatus.LISTENING;
          }
          SingleMessage image = filter.get(0);
          eventPack.reply(new PlainText("接收到图片\n"), image);
          return ListeningStatus.STOPPED;
        }

        @Override
        public void onTimeOut(MessageEventPack eventPack, PreProcessorData nextData) {
          eventPack.reply("已经超时 停止监听");
        }
      }, data, 5* 1000L, -1);
      return;
    }
    SingleMessage image = filter.get(0);
    eventPack.reply(new PlainText("接收到图片\n"), image);
  }

  @EventHandler(target = "trigger")
  public void testTriggerOut(MessageEventPack eventPack, PreProcessorData data) {
    eventPack.reply("开始监听");
    eventPack.onNext(new EventHandlerNext() {
      @Override
      public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData data) {
        eventPack.reply("剩余次数: " + data.getTriggerCount());
        if (data.getText().contains("没有")) {
          eventPack.reply("主动停止监听");
          return ListeningStatus.STOPPED;
        }
        return ListeningStatus.LISTENING;
      }
      @Override
      public void onTriggerOut(MessageEventPack eventPack, PreProcessorData data) {
        eventPack.reply("次数耗尽 停止监听");
      }

      @Override
      public void onTimeOut(MessageEventPack eventPack, PreProcessorData data) {
        eventPack.reply("已经超时 停止监听");
      }
    }, -1, 3);
  }
}
