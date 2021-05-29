package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ConfigFileLogger {
  private boolean network;
  private boolean eventStatus;
  private boolean debug;
  public ConfigFileLogger() {
    this.network = false;
    this.eventStatus = true;
    this.debug = false;
  }
}
