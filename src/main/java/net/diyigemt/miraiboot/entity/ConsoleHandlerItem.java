package net.diyigemt.miraiboot.entity;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public final class ConsoleHandlerItem extends MiraiBootHandlerItem{
	public ConsoleHandlerItem(String name, Class<?> invoker, Method handler) {
		super(name, invoker, handler);
	}
}
