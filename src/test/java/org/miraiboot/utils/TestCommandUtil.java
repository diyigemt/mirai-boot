package org.miraiboot.utils;

import net.mamoe.mirai.event.events.MessageEvent;
import org.junit.jupiter.api.Test;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.function.TestFunction;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestCommandUtil {
  @Test
  public void testParseArgs() throws NoSuchMethodException {
    Method testReply = TestFunction.class.getMethod("testReply", MessageEvent.class);
    CommandUtil.getInstance().parseArgs("reply 123 456 789", "reply", testReply, new PreProcessorData());
  }
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
}
