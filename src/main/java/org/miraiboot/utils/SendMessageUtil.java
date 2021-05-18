package org.miraiboot.utils;

import com.j256.ormlite.stmt.query.In;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import org.miraiboot.mirai.MiraiMain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SendMessageUtil {

    private static ExternalResource resource = null;

    public static void ImageMessageSender(MessageEvent event, String ImgPath){
        Image image = ImgMsgBuilder(event,ImgPath);
        event.getSubject().sendMessage(image);
        Close();
    }

    public static void ImageMessageSender(MessageEvent event, MessageChain messages, String ImgPath){
        Image image = ImgMsgBuilder(event,ImgPath);
        MessageChain messageChain = messages;
        messageChain = messageChain.plus(image);
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    public static void ImageMessageSender_asc(MessageEvent event, String messages, String ImgPath){
        Image image = ImgMsgBuilder(event,ImgPath);
        MessageChain messageChain = new MessageChainBuilder()
                .append(messages)
                .append(image)
                .build();
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    public static void ImageMessageSender_desc(MessageEvent event, String messages, String ImgPath){
        Image image = ImgMsgBuilder(event,ImgPath);
        MessageChain messageChain = new MessageChainBuilder()
                .append(image)
                .append(messages)
                .build();
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    public static void ImageMessageSender_surround(MessageEvent event, String message_before, String message_after, String ImgPath){
        Image image = ImgMsgBuilder(event,ImgPath);
        MessageChain messageChain = new MessageChainBuilder()
                .append(message_before)
                .append(image)
                .append(message_after)
                .build();
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    public static void ImageMessageSender_multiImg(MessageEvent event, String[] ImgPath){
        MessageChain messageChain = new MessageChainBuilder()
                .append(MultiImgMsgBuilder(event, ImgPath))
                .build();
        event.getSubject().sendMessage(messageChain);
        int i = 0;
        Close();
    }

    public static void ImageMessageSender_multiImg_msgAsc(MessageEvent event, String messages, String[] ImgPath){
        MessageChain messageChain = new MessageChainBuilder()
                .append(messages)
                .append(MultiImgMsgBuilder(event, ImgPath))
                .build();
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    public static void ImageMessageSender_multiImg_msgDesc(MessageEvent event, String messages, String[] ImgPath){
        MessageChain messageChain = new MessageChainBuilder()
                .append(MultiImgMsgBuilder(event, ImgPath))
                .append(messages)
                .build();
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    public static void ImageMessageSender_multiImg_msgSurround(MessageEvent event, String messages_before, String messages_after, String[] ImgPath){
        MessageChain messageChain = new MessageChainBuilder()
                .append(messages_before)
                .append(MultiImgMsgBuilder(event, ImgPath))
                .append(messages_after)
                .build();
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    public static void ImageMessageSender_multiImgMsgList(MessageEvent event, String[] messages, String[] ImgPath){
        MessageChain messageChain = new MessageChainBuilder().build();
        for(int i = 0; i < messages.length; i++){
            messageChain = messageChain.plus(Img_MsgBuilder(event, ImgPath[i], messages[i]));
        }
        event.getSubject().sendMessage(messageChain);
        Close();
    }

    private static Image ImgMsgBuilder(MessageEvent event, String ImgPath){
        File file = new File(ImgPath);
        resource = ExternalResource.create(file);
        Image image = event.getSubject().uploadImage(resource);
        return image;
    }

    private static MessageChain Img_MsgBuilder(MessageEvent event, String ImgPath, String messages){
        MessageChain messageChain = new MessageChainBuilder().build();
        File file = new File(ImgPath);
        resource = ExternalResource.create(file);
        Image image = event.getSubject().uploadImage(resource);
        messageChain = messageChain.plus(image).plus(messages);
        return messageChain;
    }

    private static MessageChain MultiImgMsgBuilder(MessageEvent event, String[] ImgPath){
        MessageChain messageChain = new MessageChainBuilder().build();
        Image image = null;
        for(String s : ImgPath){
            image = ImgMsgBuilder(event, s);
            messageChain = messageChain.plus(image);
        }
        return messageChain;
    }

    public static void Close(){
        try{
            resource.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void VoiceMsgSender(MessageEvent event, String path){
        String name = "sounds";
        try{
            if(path.contains("https")){
                InputStream inputStream = HttpUtil.getInputStream_https(path);
                resource = InpStreamReceiver(path);

            }else if(path.contains("http")){
                InputStream inputStream = HttpUtil.getInputStream_https(path);
                resource = InpStreamReceiver(path);
            }else {
                File file = new File(path);
                resource = ExternalResource.create(file);
            }
        }catch (IOException e){
            MiraiMain.getInstance().quickReply(event, "联网获取语音失败");
            e.printStackTrace();
            return;
        }
//        File file = new File(path);
//        resource = ExternalResource.create(file);
        GroupMessageEvent groupMessageEvent = (GroupMessageEvent) event;
        Voice voice = ExternalResource.Companion.uploadAsVoice(resource, groupMessageEvent.getGroup());
        event.getSubject().sendMessage(voice);
        Close();
    }

    private static ExternalResource InpStreamReceiver(String path) throws IOException {
        String name = "sounds";
        ExternalResource externalResource = null;
        InputStream inputStream = HttpUtil.getInputStream(path);
        externalResource = Mirai.getInstance().getFileCacheStrategy().newCache(inputStream, name);
        return externalResource;
    }
}
