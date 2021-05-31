package net.diyigemt.miraiboot.annotation;


import java.lang.annotation.*;

/**
 * <h2>将收到注解的方法注册为异常Handler</h2>
 * <strong>受到注解的方法至多有一个参数 即[Throwable]本身 多余的参数将会被传入null</strong><br/>
 * 方法返回值可以为void 也可以为boolean 返回boolean且为<strong>true</strong>时将会<strong>阻止</strong>低优先级的异常处理器的触发<br/>
 * 返回值不正确的方法将不会被注册成异常处理器!!<br/>
 * 注解的value为空也不会被注册为异常处理器!!<br/>
 * <pre>
 * {@code
 * @ExceptionHandler
 * public void testHandler1(Throwable e) {your code}
 *
 * @ExceptionHandler(priority = 1)
 * public boolean testHandler2(Throwable e) {your code}
 *
 * @ExceptionHandler(priority = 1)
 * public boolean testHandler3() {your code}
 * }
 * </pre>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandler {
  /**
   * <h2>处理器名</h2>
   * 可根据该名字主动移除一个异常处理器
   */
  String name() default "";
  /**
   * <h2>要处理的异常列表</h2>
   */
  Class<? extends Exception> value();

  /**
   * <h2>处理优先级</h2>
   */
  int priority() default 0;
}
