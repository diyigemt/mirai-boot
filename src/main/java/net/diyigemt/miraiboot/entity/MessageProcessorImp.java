package net.diyigemt.miraiboot.entity;

import lombok.Data;
import net.diyigemt.miraiboot.annotation.MessagePreProcessor;
import net.diyigemt.miraiboot.constant.MessagePreProcessorMessageType;
import net.diyigemt.miraiboot.interfaces.IMessagePreProcessor;
import net.mamoe.mirai.message.data.*;

import java.util.*;

/**
 * <h2>默认的消息预处理器</h2>
 * @see MessagePreProcessor
 * @author diyiegmt
 * @since 1.0.0
 */
@Data
public class MessageProcessorImp implements IMessagePreProcessor<Object> {
  private final boolean isTextProcessor = true;
  private final Set<MessagePreProcessorMessageType> filterType;

  public MessageProcessorImp() {
    this.filterType = new HashSet<>();
  }

  public void addFilterType(MessagePreProcessorMessageType[] type) {
    this.filterType.addAll(Arrays.asList(type));
  }

  public List<SingleMessage> parseMessage(MessageEventPack eventPack) {
    if (this.filterType.isEmpty()) return eventPack.getMessage();
    List<Class<? extends SingleMessage>> classes = new ArrayList<>();
    for (MessagePreProcessorMessageType type : filterType) {
      switch (type) {
        case PlainText:
          classes.add(PlainText.class);
          break;
        case Image:
          classes.add(Image.class);
          break;
        case At:
          classes.add(At.class);
          break;
        case AtAll:
          classes.add(AtAll.class);
          break;
        case Face:
          classes.add(Face.class);
          break;
        case FlashImage:
          classes.add(FlashImage.class);
          break;
        case PokeMessage:
          classes.add(PokeMessage.class);
          break;
        case VipFace:
          classes.add(VipFace.class);
          break;
        case LightApp:
          classes.add(LightApp.class);
          break;
        case Audio:
          classes.add(Voice.class);
          break;
        case MarketFace:
          classes.add(MarketFace.class);
          break;
        case ForwardMessage:
          classes.add(ForwardMessage.class);
          break;
        case SimpleServiceMessage:
          classes.add(SimpleServiceMessage.class);
          break;
        case MusicShare:
          classes.add(MusicShare.class);
          break;
        case Dice:
          classes.add(Dice.class);
          break;
        case FileMessage:
          classes.add(FileMessage.class);
          break;
      }
    }
    List<SingleMessage> res = new ArrayList<>();
    for (SingleMessage message : eventPack.getMessage()) {
      Class<?> clazz = message.getClass();
      for (Class<? extends SingleMessage> messageClass : classes) {
        if (messageClass.isAssignableFrom(clazz)) res.add(message);
      }
    }
    return res;
  }

  @Override
  public PreProcessorData<?> parseMessage(String source, MessageEventPack eventPack, PreProcessorData<Object> data) {
    PreProcessorData<Object> res = new PreProcessorData<>();
    res.setClassified(parseMessage(eventPack));
    return res;
  }
}
