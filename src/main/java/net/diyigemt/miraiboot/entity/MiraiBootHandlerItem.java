package net.diyigemt.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * <h2>handler的基类</h2>
 * @author diyigemt
 */
@Data
@AllArgsConstructor
public class MiraiBootHandlerItem {
	protected final String name;
	protected final Class<?> invoker;
	protected final Method handler;
}
