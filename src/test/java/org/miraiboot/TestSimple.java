package org.miraiboot;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSimple {
  @Test
  public void testMethodInvoke() {
    Method[] methods = TestSimple.class.getMethods();
    for (Method method : methods) {
      if (!method.getName().equals("testA")) continue;
      if (!method.getName().equals("testB")) continue;
      int parameterCount = method.getParameterCount();
      Object[] strings = new String[parameterCount];
      for (int i = 0; i < parameterCount; i++) {
        strings[i] = String.valueOf(i);
      }
      try {
        method.invoke(new TestSimple(), strings);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {

      }
    }
  }

  @Test
  public void testSet() {
    Set<String> set = new HashSet<>();
    set.add("/");
    set.add("*");
    set.add(".");
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    set.forEach(sb::append);
    sb.append("]").append("([\\u4e00-\\u9fa5]+|[a-zA-Z]+)");
    Pattern pattern = Pattern.compile(sb.toString());
    String s = "一堆乱弹琴发生的 /help asdasd asd";
    Matcher matcher = pattern.matcher(s);
    if (matcher.find()) {
      String group = matcher.group(1);
      s = s.replaceAll(group, "");
    }
  }

  @Test
  public void testTimer() {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("hello world");
      }
    }, 2000);
  }

  public void testA(String a) {
    System.out.println(a);
  }

  public void testB(String a, String b) {
    System.out.println(a + b);
  }

}