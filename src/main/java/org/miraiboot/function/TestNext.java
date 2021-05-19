package org.miraiboot.function;


import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MessagePreProcessor;
import org.miraiboot.constant.MessagePreProcessorMessageType;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.interfaces.EventHandlerNext;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerManager;

import java.util.List;

@EventHandlerComponent
public class TestNext {

  @EventHandler(target = "搜图")
  @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
  public void testTimeOut(MessageEvent event, PreProcessorData data) {
    List<SingleMessage> filter = data.getClassified();
    if (filter.isEmpty()) {
      MiraiMain.getInstance().quickReply(event, "图呢");
      EventHandlerManager.getInstance().onNext(event.getSender().getId(), new EventHandlerNext() {
        @Override
        @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
        public ListeningStatus onNext(MessageEvent nextEvent, PreProcessorData nextData) {
          if (nextData.getText().contains("没有")) {
            MiraiMain.getInstance().quickReply(nextEvent, "停止监听");
            return ListeningStatus.STOPPED;
          }
          List<SingleMessage> filter = nextData.getClassified();
          if (filter.isEmpty()) {
            MiraiMain.getInstance().quickReply(nextEvent, "等着呢 还是没有");
            return ListeningStatus.LISTENING;
          }
          SingleMessage image = filter.get(0);
          MiraiMain.getInstance().quickReply(event, new PlainText("接收到图片\n"), image);
          return ListeningStatus.STOPPED;
        }

        @Override
        public void onTimeOut(MessageEvent nextEvent, PreProcessorData nextData) {
          MiraiMain.getInstance().quickReply(nextEvent, "已经超时 停止监听");
        }
      }, 5 * 1000L, -1, event, data);
      return;
    }
    SingleMessage image = filter.get(0);
    MiraiMain.getInstance().quickReply(event, new PlainText("接收到图片\n"), image);
  }

  @EventHandler(target = "trigger")
  public void testTriggerOut(MessageEvent event, PreProcessorData data) {
    MiraiMain.getInstance().quickReply(event, "开始监听");
    EventHandlerManager.getInstance().onNext(event.getSender().getId(), new EventHandlerNext() {
      @Override
      public ListeningStatus onNext(MessageEvent event, PreProcessorData data) {
        MiraiMain.getInstance().quickReply(event, "剩余次数: " + data.getTriggerCount());
        if (data.getText().contains("没有")) {
          MiraiMain.getInstance().quickReply(event, "主动停止监听");
          return ListeningStatus.STOPPED;
        }
        return ListeningStatus.LISTENING;
      }
      @Override
      public void onTriggerOut(MessageEvent event, PreProcessorData data) {
        MiraiMain.getInstance().quickReply(event, "次数耗尽 停止监听");
      }

      @Override
      public void onTimeOut(MessageEvent event, PreProcessorData data) {
        MiraiMain.getInstance().quickReply(event, "已经超时 停止监听");
      }
    }, 10 * 1000L, 3);
  }
}
