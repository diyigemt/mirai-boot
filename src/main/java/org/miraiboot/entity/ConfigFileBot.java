package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigFileBot {
  private long account;
  private ConfigFileBotPassword password;
  private ConfigFileBotConfiguration configuration;

  public ConfigFileBot() {
    this.password = new ConfigFileBotPassword();
    this.configuration = new ConfigFileBotConfiguration();
  }
}
