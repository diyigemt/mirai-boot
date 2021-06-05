package net.diyigemt.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * <h2>将一个类标注为miraiboot组件</h2>
 * @author diyigemt
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiraiBootComponent {
}
