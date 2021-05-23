package org.miraiboot.function;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.utils.SendMessageLib;

import java.io.File;

@EventHandlerComponent
public class TextSendMsgLibrary {
    private static final String ImageLocalPath = "";//TODO: 图片素材本地路径,自己填
    private static final String URLPath = "https://i0.hdslb.com/bfs/article/0a53e07dd26d946adfe9fe843263bc12ef441bf8.jpg@860w_482h.jpg";//素材URL
    private static final MessageChain messageChain = new MessageChainBuilder().append("fff").build();//已经构造好需要接龙的普通消息链
    private static final File file = new File(ImageLocalPath);

    @EventHandler(target = "MsgLib")
    public static void textSendMsgLib(MessageEventPack eventPack, PreProcessorData data){
        SendMessageLib.ImageMessageSender(eventPack, ImageLocalPath);//
        SendMessageLib.ImageMessageSender_asc(eventPack, "message", URLPath);
        SendMessageLib.ImageMessageSender_multiImg(eventPack, new String[]{"Path1", "Path2", "..."});
        //TODO:还有更多，道理同上。
    }
}
