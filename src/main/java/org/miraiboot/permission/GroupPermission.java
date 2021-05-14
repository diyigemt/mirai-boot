package org.miraiboot.permission;

import java.lang.annotation.*;

/**
 * @author diyigemt
 * 群消息权限规定
 * 优先级 at-> permission table -> blocks -> allows -> isGroupOwnerOnly -> isAdminOnly
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GroupPermission {

  //允许列表
  String[] allows() default {};

  //禁止列表
  String[] blocks() default {};

  //是否只允许群主
  boolean isGroupOwnerOnly() default false;

  //是否允许管理员(包括群主)
  boolean isAdminOnly() default false;

  //是否At才动作
  boolean isAt() default true;
}
