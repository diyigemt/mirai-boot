package org.miraiboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.message.data.MessageContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于存放参数之类的预处理信息
 */
@Data
public class PreProcessorData {
  /**
   * 匹配到的包含指令开头所有内容<br/>
   * 例如: start: "/" target: "help"<br/>
   * 对于输入 "一些乱七八糟的/help 123 sss  ddd"<br/>
   * commandText = "/help 123 sss  ddd"
   */
  private String commandText;
  /**
   * 匹配到的指令
   */
  private String command;
  /**
   * 匹配到的参数
   */
  private List<String> args;
  private String text;
  private List<MessageContent> classified;

  public PreProcessorData() {
    this.args = new ArrayList<String>();
    this.classified = new ArrayList<MessageContent>();
  }

  public void addArgs(String[] args) {
    this.args.addAll(Arrays.asList(args));
  }
}
