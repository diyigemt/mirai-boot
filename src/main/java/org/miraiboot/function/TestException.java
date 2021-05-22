package org.miraiboot.function;

import net.mamoe.mirai.event.ListeningStatus;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.ExceptionHandler;
import org.miraiboot.annotation.ExceptionHandlerComponent;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.interfaces.EventHandlerNext;
import org.miraiboot.utils.BotManager;

@ExceptionHandlerComponent
@EventHandlerComponent
public class TestException {
  @ExceptionHandler(targets = IllegalArgumentException.class)
  public void testException(Throwable e) {
    BotManager.getInstance().get(1741557205L).getGroup(1002484182L).sendMessage("error: " + e.getMessage());
  }
  @EventHandler(target = "error")
  public void testSendError() {
    throw new IllegalArgumentException("测试error");
  }

  @EventHandler(target = "error2")
  public void testSendError(MessageEventPack eventPack) {
    eventPack.onNext(new EventHandlerNext() {
      @Override
      public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData data) {
        if (data.getText().contains("error2")) {
          throw new IllegalArgumentException("测试error");
        }
        return ListeningStatus.STOPPED;
      }
    });
  }
}
