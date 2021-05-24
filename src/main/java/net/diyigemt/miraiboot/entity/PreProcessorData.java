package net.diyigemt.miraiboot.entity;

import lombok.Data;
import net.diyigemt.miraiboot.annotation.MessagePreProcessor;
import net.mamoe.mirai.message.data.SingleMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h2>用于存放参数之类的预处理信息</h2>
 * @see MessagePreProcessor
 * @author diyigemt
 * @since 1.0.0
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
  /**
   * 消息纯文本
   */
  private String text;
  /**
   * 由@MessagePreProcessor过滤出的消息内容
   * @see MessagePreProcessor
   */
  private List<SingleMessage> classified;

  /**
   * 上下文触发剩余次数
   */
  private int triggerCount;

  public PreProcessorData() {
    this.args = new ArrayList<String>();
    this.classified = new ArrayList<SingleMessage>();
  }

  public void addArgs(String[] args) {
    this.args.addAll(Arrays.asList(args));
  }

  public void addClassified(List<SingleMessage> messages) {
    this.classified.addAll(messages);
  }
}
