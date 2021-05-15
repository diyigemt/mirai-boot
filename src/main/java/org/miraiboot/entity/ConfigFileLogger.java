package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ConfigFileLogger {
  private boolean network;
  public ConfigFileLogger() {
    this.network = false;
  }
}
