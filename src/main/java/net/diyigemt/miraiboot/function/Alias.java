package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.utils.EventHandlerManager;
import net.diyigemt.miraiboot.entity.MessageEventPack;

import java.util.List;

/**
 *
 */
@EventHandlerComponent
public class Alias {

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
