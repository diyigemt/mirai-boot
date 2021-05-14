package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.miraiboot.constant.EventHandlerType;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class EventHandlerItem {
  private String target;
  private Class<?> invoker;
  private Method handler;
  private EventHandlerType[] type;
}
