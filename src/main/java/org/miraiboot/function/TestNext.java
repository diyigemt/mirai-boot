package org.miraiboot.function;


import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerManager;

@EventHandlerComponent
public class TestNext {

  @EventHandler(target = "搜图")
  public void testReply(MessageEvent event, PreProcessorData data) {
    MiraiMain.getInstance().quickReply(event, "图呢");
    EventHandlerManager.getInstance().onNext(String.valueOf(event.getSender().getId()), (nextEvent, nextData) -> {
      if (nextData.getText().contains("没有")) {
        MiraiMain.getInstance().quickReply(nextEvent, "停止监听");
        return ListeningStatus.STOPPED;
      }
      MiraiMain.getInstance().quickReply(nextEvent, "等着呢 还是没有");
      return ListeningStatus.LISTENING;
    });
  }
}
