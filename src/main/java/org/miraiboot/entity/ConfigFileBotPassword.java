package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigFileBotPassword {
  private PasswordKind kind;
  private String value;

  public ConfigFileBotPassword() {
    this.kind = PasswordKind.PLAIN;
  }
  public enum PasswordKind {
    PLAIN, MD5;
  }
}
