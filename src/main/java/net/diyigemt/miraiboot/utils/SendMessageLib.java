package net.diyigemt.miraiboot.utils;

import net.diyigemt.miraiboot.entity.HttpProperties;
import net.diyigemt.miraiboot.exception.MultipleParameterException;
import net.diyigemt.miraiboot.utils.builder.ExternalResourceBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.Nullable;
import net.diyigemt.miraiboot.entity.MessageEventPack;

import java.io.IOException;

/**
 * <h2>图片和语音消息模板</h2>
 * <p>用来构造图片和语音消息，选择合适的方法引用并提供素材和文字即可。会自动构造和发送消息</p>
 * @author Haythem
 * @since 1.0.0
 */

public class SendMessageLib {

    private static ExternalResource resource = null;

    /**
     * <h2>单张图片消息构造器</h2>
     * <p>提供图片素材，自动构造和发送消息</p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     * @param eventPack 消息事件，群聊或私聊
     * @param ImgPath 图片来源
     */
    public static void ImageMessageSender(MessageEventPack eventPack, String ImgPath, HttpProperties... properties) {
        Image image = ImgMsgBuilder(eventPack, ImgPath, properties);
        eventPack.getSubject().sendMessage(image);
        Close();
    }

    /**
     * <h2>单张图片消息连接器</h2>
     * <p>提供图片素材和您写好的MessageChain，自动连接和发送消息</p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     * @param eventPack 消息事件，群聊或私聊
     * @param messages 消息链
     * @param ImgPath  图片来源
     */
    public static void ImageMessageSender(MessageEventPack eventPack, MessageChain messages, String ImgPath, HttpProperties... properties) {
        Image image = ImgMsgBuilder(eventPack, ImgPath, properties);
        MessageChain messageChain = messages;
        messageChain = messageChain.plus(image);
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    /**
     * <h2>带文字的单张图片消息构造器(正序)</h2>
     * <p>提供图片素材和文本消息，自动构造和发送消息</p>
     * <p>样例：</p>
     * <p></p>
     * <p>文字消息</p>
     * <p>图片</p>
     * <p></p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     * @param eventPack 消息事件，群聊或私聊
     * @param messages 纯文本消息
     * @param ImgPath  图片来源
     */
    public static void ImageMessageSender_asc(MessageEventPack eventPack, String messages, String ImgPath, HttpProperties... properties) {
        Image image = ImgMsgBuilder(eventPack, ImgPath, properties);
        MessageChain messageChain = new MessageChainBuilder()
                .append(messages)
                .append(image)
                .build();
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    /**
     * <h2>带文字的单张图片消息构造器(倒序)</h2>
     * <p>提供图片素材和文本消息，自动构造和发送消息</p>
     * <p>样例：</p>
     * <p></p>
     * <p>图片</p>
     * <p>文字消息</p>
     * <p></p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     * @param eventPack 消息事件，群聊或私聊
     * @param messages 纯文本消息
     * @param ImgPath  图片来源
     */
    public static void ImageMessageSender_desc(MessageEventPack eventPack, String messages, String ImgPath, HttpProperties properties) {
        Image image = ImgMsgBuilder(eventPack, ImgPath, properties);
        MessageChain messageChain = new MessageChainBuilder()
                .append(image)
                .append(messages)
                .build();
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    /**
     * <h2>带文字的单张图片消息构造器(文字环绕)</h2>
     * <p>提供图片素材和文本消息，自动构造和发送消息</p>
     * <p>样例：</p>
     * <p></p>
     * <p>文字消息</p>
     * <p>图片</p>
     * <p>文字消息</p>
     * <p></p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     *  @param eventPack          消息事件，群聊或私聊
     * @param message_before 前文
     * @param message_after  后文
     * @param ImgPath        图片来源
     */
    public static void ImageMessageSender_surround(MessageEventPack eventPack, String message_before, String message_after, String ImgPath, HttpProperties properties) {
        Image image = ImgMsgBuilder(eventPack, ImgPath, properties);
        MessageChain messageChain = new MessageChainBuilder()
                .append(message_before)
                .append(image)
                .append(message_after)
                .build();
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    /**
     * <h2>多张图片消息构造器</h2>
     * <p>提供图片素材，自动构造和发送消息</p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     *  @param eventPack   消息事件，群聊或私聊
     * @param ImgPath 图片来源数组
     */
    public static void ImageMessageSender_multiImg(MessageEventPack eventPack, String[] ImgPath, HttpProperties... properties) {
        MessageChain messageChain = new MessageChainBuilder()
                .append(MultiImgMsgBuilder(eventPack, ImgPath, properties))
                .build();
        eventPack.getSubject().sendMessage(messageChain);
        int i = 0;
        Close();
    }

    /**
     * <h2>带文字的多张图片消息构造器(正序)</h2>
     * <p>提供图片素材和文本消息，自动构造和发送消息</p>
     * <p>样例：</p>
     * <p></p>
     * <p>文字消息</p>
     * <p>图片1</p>
     * <p>图片2</p>
     * <p>图片3</p>
     * <p>...</p>
     * <p></p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     *  @param eventPack    消息事件，群聊或私聊
     * @param messages 纯文本消息
     * @param ImgPath  图片来源数组
     */
    public static void ImageMessageSender_multiImg_msgAsc(MessageEventPack eventPack, String messages, String[] ImgPath, HttpProperties properties) {
        MessageChain messageChain = new MessageChainBuilder()
                .append(messages)
                .append(MultiImgMsgBuilder(eventPack, ImgPath, properties))
                .build();
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    /**
     * <h2>带文字的多张图片消息构造器(倒序)</h2>
     * <p>提供图片素材和文本消息，自动构造和发送消息</p>
     * <p>样例：</p>
     * <p></p>
     * <p>图片1</p>
     * <p>图片2</p>
     * <p>图片3</p>
     * <p>文字消息</p>
     * <p>...</p>
     * <p></p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     *  @param eventPack    消息事件，群聊或私聊
     * @param messages 纯文本消息
     * @param ImgPath  图片来源数组
     */
    public static void ImageMessageSender_multiImg_msgDesc(MessageEventPack eventPack, String messages, String[] ImgPath, HttpProperties properties) {
        MessageChain messageChain = new MessageChainBuilder()
                .append(MultiImgMsgBuilder(eventPack, ImgPath, properties))
                .append(messages)
                .build();
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    /**
     * <h2>带文字的多张图片消息构造器(文字环绕)</h2>
     * <p>提供图片素材和文本消息，自动构造和发送消息</p>
     * <p>样例：</p>
     * <p></p>
     * <p>文字消息</p>
     * <p>图片</p>
     * <p>文字消息</p>
     * <p></p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     *  @param eventPack          消息事件，群聊或私聊
     * @param message_before 前文
     * @param message_after  后文
     * @param ImgPath        图片来源数组
     */
    public static void ImageMessageSender_multiImg_msgSurround(MessageEventPack eventPack, String message_before, String message_after, String[] ImgPath, HttpProperties properties) {
        MessageChain messageChain = new MessageChainBuilder()
                .append(message_before)
                .append(MultiImgMsgBuilder(eventPack, ImgPath, properties))
                .append(message_after)
                .build();
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    /**
     * <h2>带文字的多张图片消息构造器(文字环绕)</h2>
     * <p>提供图片素材和文本消息，自动构造和发送消息</p>
     * <p>样例：</p>
     * <p></p>
     * <p>图片</p>
     * <p>文字消息</p>
     * <p>图片</p>
     * <p>文字消息</p>
     * <p>...</p>
     * <p></p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     *  @param eventPack    消息事件，群聊或私聊
     * @param messages 文字消息数组
     * @param ImgPath  图片来源数组
     */
    public static void ImageMessageSender_multiImgMsgList(MessageEventPack eventPack, String[] messages, String[] ImgPath, HttpProperties properties) {
        MessageChain messageChain = new MessageChainBuilder().build();
        for (int i = 0; i < messages.length; i++) {
            messageChain = messageChain.plus(Img_MsgBuilder(eventPack, ImgPath[i], messages[i], properties));
        }
        eventPack.getSubject().sendMessage(messageChain);
        Close();
    }

    private static Image ImgMsgBuilder(MessageEventPack eventPack, String ImgPath, HttpProperties... properties) {
        resource = ExtResBuilder(eventPack, ImgPath, properties);
        return ExternalResource.Companion.uploadAsImage(resource, eventPack.getSubject());
    }

    private static MessageChain Img_MsgBuilder(MessageEventPack eventPack, String ImgPath, String messages, HttpProperties properties) {
        MessageChain messageChain = new MessageChainBuilder().build();
        messageChain = messageChain.plus(ImgMsgBuilder(eventPack, ImgPath, properties)).plus(messages);
        return messageChain;
    }

    private static MessageChain MultiImgMsgBuilder(MessageEventPack eventPack, String[] ImgPath, HttpProperties... properties) {
        MessageChain messageChain = new MessageChainBuilder().build();
        Image image = null;
        for (String s : ImgPath) {
            image = ImgMsgBuilder(eventPack, s, properties);
            messageChain = messageChain.plus(image);
        }
        return messageChain;
    }

    private static void Close() {
        try {
            resource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>语音消息构造器</h2>
     * <p>提供语音素材，自动构造和发送消息</p>
     * <p>本机路径和URL均可</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     *
     * @param eventPack 消息事件类
     * @param path      语音来源
     */
    public static void VoiceMsgSender(MessageEventPack eventPack, String path, HttpProperties... properties) {
        resource = ExtResBuilder(eventPack, path, properties);
        Voice voice = ExternalResource.Companion.uploadAsVoice(resource, eventPack.getSubject());
        eventPack.getSubject().sendMessage(voice);
        Close();
    }

    private static ExternalResource ExtResBuilder(MessageEventPack eventPack, String path, @Nullable HttpProperties... properties) {
        if(properties == null || properties.length == 0){
            resource = new ExternalResourceBuilder().ExtResourceBuilder(path, null);
            if (resource == null) {
                eventPack.reply("联网获取素材失败");
            }
        }else if(properties.length == 1){
            resource = new ExternalResourceBuilder().ExtResourceBuilder(path, properties[0]);
            if (resource == null) {
                eventPack.reply("联网获取素材失败");
            }
        }else {
            throw new MultipleParameterException("Parameter: \"HttpProperties\" need 1 but found " + properties.length + ".");
        }

        return resource;
    }
}
