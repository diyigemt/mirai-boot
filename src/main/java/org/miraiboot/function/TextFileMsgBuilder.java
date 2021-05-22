package org.miraiboot.function;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.EnhancedMessageChain;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.utils.builder.FileMessageBuilder;
import org.miraiboot.utils.builder.VoiceMessageBuilder;

import java.io.File;

@EventHandlerComponent
public class TextFileMsgBuilder {

    private static final String FileLocalPath = "";//TODO: 群文件素材本地路径,自己填
    private static final String VoiceLocalPath = "";//TODO: 语音素材本地路径,自己填
    private static final String URLPath = "https://api.btstu.cn/sjbz/?lx=dongman";//素材URL
    private static final MessageChain messageChain = new MessageChainBuilder().append("fff").build();//已经构造好需要接龙的普通消息链
    private static final File file = new File(FileLocalPath);

    @EventHandler(target = "FileMsg")
    @CheckPermission(isAdminOnly = true)
    public static void textVoiceBuilder(MessageEventPack eventPack, PreProcessorData data){
        EnhancedMessageChain chain = new FileMessageBuilder(eventPack)
                .add("1234")//纯文本消息
                .add("1234","5678")//支持多个字符串参数输入
                .add(FileLocalPath)
                .add(URLPath)//(目前只有此Builder支持重定向)
                .add(messageChain)
                .add(file)
                .build();//构造但不发送

        EnhancedMessageChain chains = new VoiceMessageBuilder(eventPack)
                .add(chain)
                .add(VoiceLocalPath)
                .send();//构造并发送
    }
}
