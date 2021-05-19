package org.miraiboot.utils;

import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import org.miraiboot.annotation.HttpsProperties;
import org.miraiboot.mirai.MiraiMain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

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
        resource = ExtResBuilder(event, ImgPath);
        return ExternalResource.Companion.uploadAsImage(resource, event.getSubject());
    }

    private static MessageChain Img_MsgBuilder(MessageEvent event, String ImgPath, String messages){
        MessageChain messageChain = new MessageChainBuilder().build();
        messageChain = messageChain.plus(ImgMsgBuilder(event, ImgPath)).plus(messages);
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
        resource = ExtResBuilder(event, path);
        GroupMessageEvent groupMessageEvent = (GroupMessageEvent) event;
        Voice voice = ExternalResource.Companion.uploadAsVoice(resource, groupMessageEvent.getGroup());
        event.getSubject().sendMessage(voice);
        Close();
    }

    private static ExternalResource ExtResBuilder(MessageEvent event, String path){
        ExternalResource externalResource = null;
        try{
            String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
            String className = Thread.currentThread().getStackTrace()[4].getClassName();
            Class<?> aClass = Class.forName(className);
            Method[] methods = aClass.getDeclaredMethods();
            Method method = null;
            for(Method m : methods){
                if(m.getName().equals(methodName)){
                    method = m;
                }
            }
            if(path.contains("http")){//URL
                InputStream inputStream = null;
                if(method.isAnnotationPresent(HttpsProperties.class)){
                    HttpsProperties properties = method.getAnnotation(HttpsProperties.class);
                    inputStream = HttpUtil.getInputStream_advanced(path, properties);
                }else{
                    inputStream = HttpUtil.getInputStream(path);
                }
                externalResource = Mirai.getInstance().getFileCacheStrategy().newCache(inputStream);
            }else {//LOCAL
                File file = new File(path);
                externalResource = ExternalResource.create(file);
            }
        }catch (IOException e){
            MiraiMain.getInstance().quickReply(event, "联网获取语音失败");
        }catch (Exception e){
            e.printStackTrace();
        }
        return externalResource;
    }
}
