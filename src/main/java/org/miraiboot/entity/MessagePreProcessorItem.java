package org.miraiboot.entity;

import lombok.Data;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.miraiboot.constant.MessagePreProcessorMessageType;

import java.util.*;

/**
 * <h2>用于保存MessagePreProcessor的参数</h2>
 * @see org.miraiboot.annotation.MessagePreProcessor
 * @author diyiegmt
 * @since 1.0.0
 */
@Data
public class MessagePreProcessorItem {
  private boolean isTextProcessor;
  private Set<MessagePreProcessorMessageType> filterType;

  public MessagePreProcessorItem() {
    this.filterType = new HashSet<MessagePreProcessorMessageType>();
  }

  public void addFilterType(MessagePreProcessorMessageType[] type) {
    this.filterType.addAll(Arrays.asList(type));
  }

  public List<SingleMessage> parseMessage(MessageEvent event) {
    if (this.filterType.isEmpty()) return event.getMessage();
    List<Class<? extends SingleMessage>> classes = new ArrayList<Class<? extends SingleMessage>>();
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
        case Voice:
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
    List<SingleMessage> res = new ArrayList<SingleMessage>();
    for (SingleMessage message : event.getMessage()) {
      Class<?> clazz = message.getClass();
      for (Class<? extends SingleMessage> messageClass : classes) {
        if (messageClass.isAssignableFrom(clazz)) res.add(message);
      }
    }
    return res;
  }
}
