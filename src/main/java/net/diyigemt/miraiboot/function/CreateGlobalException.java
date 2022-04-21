package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;

public class CreateGlobalException {
  @EventHandler(target = "create")
  public void createException() {
    throw new ExceptionA("create");
  }
}
