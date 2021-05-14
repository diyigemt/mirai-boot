package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * 将受到注解的类扫描事件handler
 * @author diyigemt
 * @since 3.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandlerComponent {
}
