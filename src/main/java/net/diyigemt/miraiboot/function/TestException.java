package net.diyigemt.miraiboot.function;

import net.mamoe.mirai.event.ListeningStatus;
import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.ExceptionHandler;
import net.diyigemt.miraiboot.annotation.ExceptionHandlerComponent;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.interfaces.EventHandlerNext;
import net.diyigemt.miraiboot.utils.BotManager;

public class TestException {
  @EventHandler(target = "error1")
  public void testSendError1(MessageEventPack eventPack) {
    throw new RuntimeException("测试");
  }

  @EventHandler(target = "error2")
  public void testSendError2(MessageEventPack eventPack, PreProcessorData<Object> data) {
    eventPack.onNext(new EventHandlerNext<Object>() {
      @Override
      public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData<Object> data) {
        if (data.getText().contains("error2")) {
          throw new IllegalArgumentException("测试error");
        }
        return ListeningStatus.STOPPED;
      }

      @Override
      public ListeningStatus onException(Throwable e, MessageEventPack eventPack, PreProcessorData<Object> data) {
        eventPack.reply("事件处理器收到error:" + e.getMessage());
        return ListeningStatus.STOPPED;
      }

      @Override
      public void onDestroy(MessageEventPack eventPack, PreProcessorData<Object> data) {
        eventPack.reply("停止监听");
      }
    }, data);
  }

  @EventHandler(target = "error3")
  public void testSendError3(MessageEventPack eventPack, PreProcessorData<Object> data) {
    eventPack.onNext(new EventHandlerNext<Object>() {
      @Override
      public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData<Object> data) {
        if (data.getText().contains("error2")) {
          throw new IllegalArgumentException("测试error");
        }
        return ListeningStatus.STOPPED;
      }

      @Override
      public ListeningStatus onException(Throwable e, MessageEventPack eventPack, PreProcessorData<Object> data) {
        eventPack.reply("事件处理器收到error:" + e.getMessage());
        return ListeningStatus.LISTENING;
      }
    }, 1000L, data);
  }

  @ExceptionHandler(RuntimeException.class)
  public void handlerError(RuntimeException e, MessageEventPack eventPack) {
    eventPack.reply("收到error: " + e.getMessage());
  }
}
