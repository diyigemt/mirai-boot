package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigFileBotConfiguration {
  private ProtocolKind protocol;
  private String device;

  public ConfigFileBotConfiguration() {
    this.protocol = ProtocolKind.ANDROID_PHONE;
    this.device = "device.json";
  }
  public enum ProtocolKind {
    ANDROID_PHONE, ANDROID_PAD, ANDROID_WATCH
  }
}
