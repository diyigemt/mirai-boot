package org.miraiboot.permission;

import java.lang.annotation.*;

/**
 * @author diyigemt
 * 群消息权限规定
 * 优先级 at-> permission table -> blocks -> allows -> isStrictRestricted -> isGroupOwnerOnly -> isAdminOnly
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {

  //允许列表
  String[] allows() default {};

  //禁止列表
  String[] blocks() default {};

  //是否严格检查身份（群主和管理员之间的权限将严格划分）
  boolean isStrictRestricted() default false;

  //是否只允许群主
  boolean isGroupOwnerOnly() default false;

  //是否允许管理员(包括群主)
  boolean isAdminOnly() default false;

  //与数据库权限对应的id号
  int permissionIndex() default 0;
}
