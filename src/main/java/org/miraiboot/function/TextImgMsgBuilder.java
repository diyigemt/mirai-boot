package org.miraiboot.function;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.EnhancedMessageChain;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.utils.builder.ImageMessageBuilder;
import org.miraiboot.utils.builder.VoiceMessageBuilder;

import java.io.File;

@EventHandlerComponent
public class TextImgMsgBuilder {

    private static final String LocalPath = "";//TODO: 图片素材本地路径,自己填
    private static final String URLPath = "https://i0.hdslb.com/bfs/article/0a53e07dd26d946adfe9fe843263bc12ef441bf8.jpg@860w_482h.jpg";//素材URL
    private static final String VoiceLocalPath = "";//TODO: 语音素材本地路径,自己填
    private static final MessageChain messageChain = new MessageChainBuilder().append("fff").build();//已经构造好需要接龙的普通消息链
    private static final File file = new File(LocalPath);

    @EventHandler(target = "ImgMsg")
    @CheckPermission(isAdminOnly = true)
    public static void textImgBuilder(MessageEventPack eventPack, PreProcessorData data){
        EnhancedMessageChain chains = new ImageMessageBuilder(eventPack)
                .add("1234")//纯文本消息
                .add("1234","5678")//支持多个字符串参数输入
                .add(LocalPath)
                .add(URLPath)
                .add(messageChain)
                .add(file)
                .build();//构造但不发送

        EnhancedMessageChain enhancedMessageChain = new VoiceMessageBuilder(eventPack)//接龙
                .add(VoiceLocalPath)
                .add(chains)
                .send();//构造并发送

//        EnhancedMessageChain messageChain = new ImageMessageBuilder(eventPack)//接自己
//                .add("Start")
//                .add(chains)
//                .add("end")
//                .send();
    }
}
