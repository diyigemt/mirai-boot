package org.miraiboot.entity;

import lombok.Data;
import org.miraiboot.constant.MessagePreProcessorMessageType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
}
