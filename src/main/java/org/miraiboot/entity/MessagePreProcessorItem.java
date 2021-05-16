package org.miraiboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.message.data.MessageContent;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class MessagePreProcessorItem {
  private String command;
  private ArrayList<String> args;
  private String text;
  private ArrayList<MessageContent> classified;
}
