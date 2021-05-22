package org.miraiboot.annotation;


import java.lang.annotation.*;

/**
 * <h2>在受到注解的类中扫描异常handler</h2>
 * <h2>未受到该注解的类 扫描时将会被忽略</h2>
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandlerComponent {
}
