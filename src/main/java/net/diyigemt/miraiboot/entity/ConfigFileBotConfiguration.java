package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.mamoe.mirai.utils.BotConfiguration;

@Data
@AllArgsConstructor
public class ConfigFileBotConfiguration {
  private BotConfiguration.MiraiProtocol protocol;
  private String device;

  public ConfigFileBotConfiguration() {
    this.protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE;
    this.device = "device.json";
  }
}
