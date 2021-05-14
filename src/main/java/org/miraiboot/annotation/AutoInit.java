package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * 对当前类启用自动初始化
 * @author diyigemt
 * @since 3.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoInit {
}
