package net.diyigemt.miraiboot.function;


import net.diyigemt.miraiboot.annotation.MessagePreProcessor;
import net.diyigemt.miraiboot.constant.MessagePreProcessorMessageType;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.interfaces.EventHandlerNext;

import java.util.List;

@EventHandlerComponent
public class TestNext {

  @EventHandler(target = "搜图")
  @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
  public void testTimeOut(MessageEventPack eventPack, PreProcessorData data) {
    List<SingleMessage> filter = data.getClassified();
    if (filter.isEmpty()) {
      eventPack.reply("图呢");
      eventPack.onNext(new EventHandlerNext() {
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
      }, 5* 1000L, -1);
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

  @EventHandler(target = "myhandler")
  public void test(MessageEventPack eventPack) {
    eventPack.reply("开始了");
    eventPack.onNext(new MyEventHandlerNext());
  }

  class MyEventHandlerNext extends EventHandlerNext {
    private int index = 1;
    @Override
    public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData data) {
      eventPack.reply("这是自建的handler" + index);
      return ListeningStatus.STOPPED;
    }
  }
}
