package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.EnhancedMessageChain;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.utils.builder.ImageMessageBuilder;

import java.io.File;

@EventHandlerComponent
public class text {
    @EventHandler(target = "textSend")
    public void Text(MessageEventPack eventPack, PreProcessorData data){
        File resourceFile = new File("G:\\素材\\QQBot\\素材\\未标题-1.png");
        String path = resourceFile.getPath();//相对路径
        String AbsPath = resourceFile.getAbsolutePath();//绝对路径

//        SendMessageLib.ImageMessageSender(eventPack, path);
//        eventPack.reply("分割线");
//        SendMessageLib.ImageMessageSender(eventPack, AbsPath);
//        eventPack.reply("分割线");
        EnhancedMessageChain chain1 = new ImageMessageBuilder(eventPack)
                .add("G:\\素材\\QQBot\\素材\\未标题-1.png")
                .build();
        EnhancedMessageChain chain2 = new ImageMessageBuilder(eventPack)
                .add(chain1)
                .add("G:\\素材\\header.jpg")
                .build();
        EnhancedMessageChain chain3 = new ImageMessageBuilder(eventPack)
                .add(chain2)
                .add("G:\\素材\\QQBot\\素材\\未标题-1.png")
                .build();
        EnhancedMessageChain chain4 = new ImageMessageBuilder(eventPack)
                .add(chain3)
                .add("G:\\素材\\header.jpg")
                .send();
    }
}
