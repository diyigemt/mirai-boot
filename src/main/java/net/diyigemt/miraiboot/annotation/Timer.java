package net.diyigemt.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * 定时执行任务<br/>
 * 内部使用Quartz来完成
 * @author diyigemt
 * @since 1.0.7
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Timer {
  /**
   * 符合Quartz定义的周期<br/>
   * 准确来说应该是一个规范的Cron表达式
   */
  String value() default "";

  /**
   * 激活标志, 如果GlobalConfig中该标志为true则激活这个定时器<br/>
   * 为空时默认激活
   */
  String activeTag() default "";
}
