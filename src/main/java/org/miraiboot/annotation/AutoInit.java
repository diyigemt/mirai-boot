package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * <h3>对被注释类启用自动初始化 样例:{@link org.miraiboot.dao.PermissionDAO}</h3>
 * 自动初始化将会在程序启动完成(bot登录后执行)
 * <p>被注解的类中必须有一个静态方法</p>
 * *@param mainClass 被@MiraiBootApplication注解的主类<br/>
 *  public static void init(Class<?> mainClass) {//your code}<br/>
 * value指初始化的优先级 数字越高越先进行初始化
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoInit {
  int value() default -1;
}
