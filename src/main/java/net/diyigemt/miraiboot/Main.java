package net.diyigemt.miraiboot;

import net.diyigemt.miraiboot.annotation.MiraiBootApplication;
import net.diyigemt.miraiboot.autoconfig.MiraiApplication;

@MiraiBootApplication(description = "测试项目")
public class Main {
  public static void main(String[] args) {
    MiraiApplication.run(Main.class, args);
  }
}
