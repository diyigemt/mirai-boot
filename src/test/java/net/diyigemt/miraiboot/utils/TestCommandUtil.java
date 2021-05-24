package net.diyigemt.miraiboot.utils;

import org.junit.jupiter.api.Test;

public class TestCommandUtil {
  @Test
  public void testCompilePattern() {
    CommandUtil.getInstance().registerCommandStart("启动");
    CommandUtil.getInstance().registerCommandStart("*");
    CommandUtil.getInstance().registerCommandStart("/");
    CommandUtil.getInstance().compileCommandPattern();
    String s = CommandUtil.getInstance().parseCommand("这是测试用启动a 代码");
    String s1 = CommandUtil.getInstance().parseCommand("这是测试用*a 代码");
    String s2 = CommandUtil.getInstance().parseCommand("这是测试用/a 代码");
  }
  @Test
  public void testCompilePattern2() {
    CommandUtil.getInstance().registerCommandStart(";");
    CommandUtil.getInstance().registerCommandStart("/");
    CommandUtil.getInstance().compileCommandPattern();
    String s2 = CommandUtil.getInstance().parseCommand("/aaa");
  }
}
