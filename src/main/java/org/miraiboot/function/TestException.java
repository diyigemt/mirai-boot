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

@ExceptionHandlerComponent(value = -1)
@EventHandlerComponent
public class TestException {
  @ExceptionHandler(targets = IllegalArgumentException.class, priority = 0)
  public void testException6(Throwable e) {
    BotManager.getInstance().get(1741557205L).getGroup(1002484182L).sendMessage("error: " + e.getMessage() + " priority: -1");
  }
  @ExceptionHandler(targets = IllegalArgumentException.class, priority = 0)
  public void testException1(Throwable e) {
    BotManager.getInstance().get(1741557205L).getGroup(1002484182L).sendMessage("error: " + e.getMessage() + " priority: -1");
  }
  @ExceptionHandler(targets = IllegalArgumentException.class, priority = 1)
  public void testException2(Throwable e) {
    BotManager.getInstance().get(1741557205L).getGroup(1002484182L).sendMessage("error: " + e.getMessage() + " priority: 1.1");
  }
  @ExceptionHandler(targets = IllegalArgumentException.class, priority = 1)
  public boolean testException4(Throwable e) {
    BotManager.getInstance().get(1741557205L).getGroup(1002484182L).sendMessage("error: " + e.getMessage() + " priority: 1.2");
    return false;
  }
  @ExceptionHandler(targets = IllegalArgumentException.class, priority = 1)
  public void testException5(Throwable e) {
    BotManager.getInstance().get(1741557205L).getGroup(1002484182L).sendMessage("error: " + e.getMessage() + " priority: 1.3");
  }
  @ExceptionHandler(targets = IllegalArgumentException.class, priority = 2)
  public void testException3(Throwable e) {
    BotManager.getInstance().get(1741557205L).getGroup(1002484182L).sendMessage("error: " + e.getMessage() + " priority: 2");
  }
  @EventHandler(target = "error")
  public void testSendError() {
    throw new RuntimeException("RuntimeException");
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
