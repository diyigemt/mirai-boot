package net.diyigemt.miraiboot.entity;

import lombok.Data;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <h2>加强消息链</h2>
 * <p>相比MessageChain，该消息链可以添加多个语音，群文件等特殊Message</p>
 * @author Haythem
 * @since 1.0.0
 */
@Data
public class EnhancedMessageChain implements Iterable<MessageChain>{

    private List<MessageChain> EnhancedMsgChain = new ArrayList<>();

    /**
     * <h2>加强消息链添加消息方法</h2>
     * <p>支持以下类型输入:</p>
     * <p></p>
     * <p>1: MessageChain消息链</p>
     * <p>2: EnhancedMessageChain加强的消息链</p>
     *
     * @param messageChain 当前类型: MessageChain 消息链
     */
    public void append(MessageChain messageChain){
        this.EnhancedMsgChain.add(messageChain);
    }

    /**
     * <h2>加强消息链添加消息方法</h2>
     * <p>支持以下类型输入:</p>
     * <p></p>
     * <p>1: MessageChain消息链</p>
     * <p>2: EnhancedMessageChain加强的消息链</p>
     *
     * @param messageChain 当前类型: EnhancedMessageChain 特殊消息链
     */
    public void append(EnhancedMessageChain messageChain){
        this.EnhancedMsgChain.addAll(messageChain.getEnhancedMsgChain());
    }

    /**
     * 迭代器，用于支持foreach循环
     */
    @NotNull
    @Override
    public Iterator<MessageChain> iterator() {
        return new MsgChainIterator();
    }

    private class MsgChainIterator implements Iterator<MessageChain>{
        int current;

        @Override
        public boolean hasNext() {
            return current != EnhancedMsgChain.size();
        }

        @Override
        public MessageChain next() {
            return EnhancedMsgChain.get(current++);
        }
    }

    public int size() { return EnhancedMsgChain.size(); }
}
