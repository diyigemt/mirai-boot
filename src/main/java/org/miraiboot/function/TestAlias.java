package org.miraiboot.function;

import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerManager;

import java.util.List;

/**
 *
 */
@EventHandlerComponent
public class TestAlias {

  @EventHandler(target = "alias")
  public void testAlias(MessageEventPack eventPack, PreProcessorData data) {
    List<String> args = data.getArgs();
    if (args.isEmpty()) {
      eventPack.reply("参数获取失败");
    }
    String target = args.get(0);
    String alias = args.get(1);
    EventHandlerManager.getInstance().registerAlias(target, alias);
    eventPack.reply("为指令:" + target + " 添加别名:" + alias);
  }
}
