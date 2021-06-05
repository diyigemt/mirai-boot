package net.diyigemt.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * <h2>注册一个控制台指令</h2>
 * @author diyigemt
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConsoleCommand {
	/**
	 * 指令名称
	 */
	String value();
}
