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
    CommandUtil.getInstance().parseArgs("reply 123 456 789", testReply, new PreProcessorData());
  }
}
