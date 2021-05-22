package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * <h2>对被注释类启用自动初始化</h2>
 * 样例:{@link org.miraiboot.dao.PermissionDAO}<br/>
 * 自动初始化将会在程序启动完成(bot前后执行)
 * <p>被注解的类中必须有一个静态方法</p>
 * *@param config 配置文件<br/>
 *  public static void init(ConfigFile config) {//your code}<br/>
 * value指初始化的优先级 数字<strong>越大越先</strong>进行初始化
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoInit {
  int value() default 0;
}
