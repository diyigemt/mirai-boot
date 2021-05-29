package net.diyigemt.miraiboot.annotation;


import java.lang.annotation.*;

/**
 * <h2>为指令添加帮助信息</h2>
 * <p>例如:</p>
<pre>
 {@code
 @EventHandler(target = "乐", start = "/")
 @EventHandlerHelp(value = 1, description = "发送一个乐子", detail = {/乐 随机发送一个乐子", "/乐 1  发送编号为1的乐子"})
 public void happy(MessageEventPack eventPack, PreProcessData data) { your code }
 }
</pre>
 * 以上将会在用户发送"帮助"的时候回复:"可用指令:1./乐"<br/>
 * 在用户发送"帮助 1"的时候回复:"1./乐 随机发送一个乐子/n2./乐 1 发送编号为1的乐子"
 * @author diyigemt
 * @since 1.1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandlerHelp {
  /**
   * <h2>是否录入帮助信息</h2>
   * 为false时不会将被注解的方法录入帮助信息中
   */
  boolean enable() default true;
  /**
   * <h2>指令对应的帮助id</h2>
   * 会在用户输入 帮助 帮助id时，将id对应的指令具体触发方式描述打印
   */
  int value();
  /**
   * <h2>指令功能描述</h2>
   */
  String description() default "";

  /**
   * <h2>指令的具体触发方式描述</h2>
   */
  String[] detail() default {};
}
