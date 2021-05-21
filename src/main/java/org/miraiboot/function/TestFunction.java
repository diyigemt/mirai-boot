package org.miraiboot.function;


import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.utils.builder.ImageMessageBuilder;

import java.io.File;

@EventHandlerComponent
public class TestFunction{

  @EventHandler(target = "reply")
  @CheckPermission(isAdminOnly = true, permissionIndex = FunctionId.reply)
  public void testReply(MessageEventPack eventPack, PreProcessorData data) {
//    eventPack.getSubject().sendMessage(eventPack.getMessage());
    String Path = "G:\\素材\\header.jpg";
    String soundPath = "C:\\Users\\Administrator\\Desktop\\01-1.mp3";
    String soundURL = "https://meamea.moe/voices/01-1.mp3";
    String urlPath = "https://i0.hdslb.com/bfs/article/0a53e07dd26d946adfe9fe843263bc12ef441bf8.jpg@860w_482h.jpg";
    String[] msg = {"ffa", "faf"};
    String[] Paths = {"G:\\素材\\header.jpg","https://i0.hdslb.com/bfs/article/0a53e07dd26d946adfe9fe843263bc12ef441bf8.jpg@860w_482h.jpg"};
    File file = new File("G:\\素材\\header.jpg");

    MessageChain messageChain = new MessageChainBuilder().append("start").build();
    MessageChain chain = new ImageMessageBuilder(eventPack)
            .add(messageChain)
            .add("1234\n")
            .add("12332\n", "131\n")
            .add(Path)
            .add(urlPath)
            .add(file)
            .add()
            .send();
  }
}
