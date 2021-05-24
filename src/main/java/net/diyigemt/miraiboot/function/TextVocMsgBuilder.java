package net.diyigemt.miraiboot.function;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.EnhancedMessageChain;
import net.diyigemt.miraiboot.entity.HttpProperties;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.permission.CheckPermission;
import net.diyigemt.miraiboot.utils.builder.VoiceMessageBuilder;

import java.io.File;

@EventHandlerComponent
public class TextVocMsgBuilder {
    private static final String LocalPath = "fff";//TODO: 素材本地路径,自己填
    private static final String URLPath = "https://meamea.moe/voices/01-1.mp3";//素材URL
    private static final MessageChain messageChain = new MessageChainBuilder().append("fff").build();//已经构造好需要接龙的普通消息链
    private static final File file = new File(LocalPath);

    @EventHandler(target = "VoiceMsg")
    @CheckPermission(isAdminOnly = true)
    public static void textVoiceBuilder(MessageEventPack eventPack, PreProcessorData data){
        EnhancedMessageChain chain = new VoiceMessageBuilder(eventPack)
                .add("1234")//纯文本消息
                .add("1234","5678")//支持多个字符串参数输入
                .add(LocalPath)
                .add(URLPath)
                .add(messageChain)
                .add(file)
                .build();//构造但不发送

        HttpProperties properties = new HttpProperties();
        properties.setRequestProperties("Connection", "keep-alive");//TODO:添加属性，支持重写

        EnhancedMessageChain chains = new VoiceMessageBuilder(eventPack, properties)//接龙
                .add(chain)
                .add("end")
                .add(URLPath)
                .send();//构造并发送
    }
}
