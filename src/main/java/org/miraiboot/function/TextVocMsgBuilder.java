package org.miraiboot.function;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.utils.builder.VoiceMessageBuilder;

import java.io.File;
import java.util.List;

@EventHandlerComponent
public class TextVocMsgBuilder {
    private static final String LocalPath = "";//素材本地路径
    private static final String URLPath = "https://meamea.moe/voices/01-1.mp3";//素材URL
    private static final MessageChain messageChain = new MessageChainBuilder().build();//已经构造好需要接龙的消息链
    private static final File file = new File(LocalPath);

    @EventHandler(target = "VoiceMsg")
    @CheckPermission(isAdminOnly = true)
    public static void textVoiceBuilder(MessageEventPack eventPack, PreProcessorData data){
        List<MessageChain> chain = new VoiceMessageBuilder(eventPack)
                .add("1234")//纯文本消息
                .add("1234","5678")//支持多个字符串参数输入
                .add(LocalPath)
                .add(URLPath)
                .add(messageChain)
                .add(file)
                .build();//构造并发送，或者build();，构造但不发送
    }
}
