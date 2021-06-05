package net.diyigemt.miraiboot;


import org.junit.jupiter.api.Test;

public class TestSimple2 {
  @Test
  public void testCast() {
    Object a = "1";
    try {
      int i = (int) a;
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
  }
}
