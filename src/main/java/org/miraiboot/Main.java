package org.miraiboot;

import lombok.extern.slf4j.Slf4j;
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.autoconfig.MiraiApplication;

@Slf4j
@MiraiBootApplication(description = "测试项目")
public class Main {
  public static void main(String[] args) {
    MiraiApplication.run(Main.class, args);
  }
}
