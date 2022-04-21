package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.annotation.ExceptionHandler;
import net.diyigemt.miraiboot.annotation.ExceptionHandlerComponent;
import net.diyigemt.miraiboot.entity.MessageEventPack;

public class TestExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public void testException(RuntimeException e, MessageEventPack eventPack) {
    eventPack.reply("处理自定义RuntimeException:" + e.getMessage());
  }
}
