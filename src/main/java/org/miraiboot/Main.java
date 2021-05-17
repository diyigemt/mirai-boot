package org.miraiboot;

import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.autoconfig.MiraiApplication;

@MiraiBootApplication(description = "测试项目")
public class Main {
  public static void main(String[] args) {
    MiraiApplication.run(Main.class, args);
  }
}
