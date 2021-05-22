package org.miraiboot.annotation;


import java.lang.annotation.*;

/**
 * <h2>将收到注解的方法注册为异常Handler</h2>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandler {
  /**
   * <h2>要处理的异常列表</h2>
   */
  Class<? extends Exception>[] targets() default {};

  /**
   * <h2>处理优先级</h2>
   */
  int priority() default 0;
}
