package org.miraiboot.utils.builder;

import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.miraiboot.annotation.HttpsProperties;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>自定义图片消息构造器</h2>
 * <p>样例：</p>
 * <p>MessageChain chain = new ImageMessageBuilder(MessageEventPack eventPack).build();</p>
 * <p></p>
 * <p><b>注：请不要用此类创建变量</b></p>
 * @author Haythem
 * @since 1.0.0
 */
public class ImageMessageBuilder {

    private final Pattern windowsPattern = Pattern.compile("[A-z]:\\\\([A-Za-z0-9_\u4e00-\u9fa5]+\\\\)*");

    private final Pattern linuxPattern = Pattern.compile("/([A-Za-z0-9_\u4e00-\u9fa5]+/?)+");

    private MessageEvent event = null;

    private MessageEventPack messageEventPack = null;

    private MessageChain chain = new MessageChainBuilder().build();

    private boolean isUTTPRequestSuccess = true;

    /**
     * <h2>自定义图片消息构造器</h2>
     * <p>可以自定义图文消息构成</p>
     * <p>样例:</p>
     * <p>MessageChain chain = new ImageMessageBuilder(MessageEventPack)</p>
     * <p>&nbsp;&nbsp;.add(messageChain)</p>
     * <p>&nbsp;&nbsp;.add("1234\n")</p>
     * <p>&nbsp;&nbsp;.add("1234\n", "5678\n")</p>
     * <p>&nbsp;&nbsp;.add(LocalFilePath)</p>
     * <p>&nbsp;&nbsp;.add(urlPath)</p>
     * <p>&nbsp;&nbsp;.add(file)</p>
     * <p>&nbsp;&nbsp;.send();（或.build();）</p>
     *<p>}</p>
     * @param eventPack 事件封装
     * @author Haythem
     * @since 1.0.0
     */
    public ImageMessageBuilder(MessageEventPack eventPack){
        this.messageEventPack = eventPack;
        this.event = eventPack.getEvent();
    }

    /**
     * <h2>添加图文消息方法</h2>
     * <p>支持以下类型输入:</p>
     * <p></p>
     * <p>1: MessageChain消息链</p>
     * <p>2: String...可变长字符串，字符串支持本地路径、URL和文字消息</p>
     * <p>3: File 打开的文件类</p>
     * <p></p>
     * <p>注:</p>
     * <p>1: 请不要插入和图片无关的素材，如有需求，请使用与素材类型对应的其它Builder</p>
     * <p>2: 当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>&nbsp;&nbsp;"联网获取素材失败"</p>
     * @param messages 当前类型: 消息链
     */
    public ImageMessageBuilder add(MessageChain messages){
        chain = chain.plus(messages);
        return this;
    }

    /**
     * <h2>添加图文消息方法</h2>
     * <p>支持以下类型输入:</p>
     * <p></p>
     * <p>1: MessageChain消息链</p>
     * <p>2: String...可变长字符串，字符串支持本地路径、URL和文字消息</p>
     * <p>3: File 打开的文件类</p>
     * <p></p>
     * <p>注:</p>
     * <p>1: 请不要插入和图片无关的素材，如有需求，请使用与素材类型对应的其它Builder</p>
     * <p>2: 当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>&nbsp;&nbsp;"联网获取素材失败"</p>
     * @param s 当前类型: String... 可变长字符串
     */
    public ImageMessageBuilder add(String... s){
        for(String i : s){
            Matcher windowsMatcher = windowsPattern.matcher(i);
            Matcher linuxMatcher = linuxPattern.matcher(i);
            if(!(windowsMatcher.find() || linuxMatcher.find()) && !i.contains("http")){
                chain = chain.plus(i);
            }else {
                ExternalResource resource = ExtResBuilder(i);
                if(isUTTPRequestSuccess){
                    chain = chain.plus(ExternalResource.Companion.uploadAsImage(resource, event.getSubject()));
                }else {
                    chain = chain.plus("联网获取数据失败");
                }
            }
        }

        return this;
    }

    /**
     * <h2>添加图文消息方法</h2>
     * <p>支持以下类型输入:</p>
     * <p></p>
     * <p>1: MessageChain消息链</p>
     * <p>2: String...可变长字符串，字符串支持本地路径和URL和文字消息</p>
     * <p>3: File 打开的文件类</p>
     * <p></p>
     * <p>注:</p>
     * <p>1: 请不要插入和图片无关的素材，如有需求，请使用与素材类型对应的其它Builder</p>
     * <p>2: 当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>&nbsp;&nbsp;"联网获取素材失败"</p>
     * @param file 当前类型: 文件类
     */
    public ImageMessageBuilder add(File file){
        chain = chain.plus(ExternalResource.Companion.uploadAsImage(file, event.getSubject()));
        return this;
    }

    /**
     * <h2>构造器结尾</h2>
     * <p>该方法返回构造完成的消息链</p>
     * @return MessageChain 消息链
     */
    public MessageChain build(){
        return chain;
    }

    /**
     * <h2>构造器结尾</h2>
     * <p>该方法返回并自动发送构造完成的消息链</p>
     * <p>注：当使用URL素材时，如果网络不佳未能获得素材会发送以下纯文本消息:</p>
     * <p>"联网获取素材失败"</p>
     * @return MessageChain 消息链
     */
    public MessageChain send(){
        event.getSubject().sendMessage(chain);
        return chain;
    }

    private ExternalResource ExtResBuilder(String path){
        ExternalResource externalResource = null;
        try{
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            String className = Thread.currentThread().getStackTrace()[3].getClassName();
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
            isUTTPRequestSuccess = false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return externalResource;
    }
}
