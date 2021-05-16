package org.miraiboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.message.data.MessageContent;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于存放参数之类的预处理信息
 */
@Data
public class PreProcessorData {
  private String commandText;
  private String command;
  private List<String> args;
  private String text;
  private List<MessageContent> classified;

  public PreProcessorData() {
    this.classified = new ArrayList<MessageContent>();
  }
}
