package org.miraiboot.function;

import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerManager;

import java.util.List;

@EventHandlerComponent
public class TestAlias {

  @EventHandler(target = "alias")
  public void testAlias(MessageEvent event, PreProcessorData data) {
    List<String> args = data.getArgs();
    if (args.isEmpty()) MiraiMain.getInstance().quickReply(event, "参数获取失败");
    String target = args.get(0);
    String alias = args.get(1);
    EventHandlerManager.getInstance().registerAlias(target, alias);
  }
}
