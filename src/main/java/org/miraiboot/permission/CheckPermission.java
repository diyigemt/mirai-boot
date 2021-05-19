package org.miraiboot.permission;

import java.lang.annotation.*;

import static org.miraiboot.constant.FunctionId.DEFAULT_INDEX;

/**
 * <h2>消息权限规定</h2>
 * 优先级 at-> permission table -> blocks -> allows -> isStrictRestricted -> isGroupOwnerOnly -> isAdminOnly
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {

  //允许列表
  String[] allows() default {};

  //禁止列表
  String[] blocks() default {};

  //是否严格检查身份（群主和管理员之间的权限将严格划分）防止下克上
  boolean isStrictRestricted() default true;

  //是否只允许群主
  boolean isGroupOwnerOnly() default false;

  //是否允许管理员(包括群主)
  boolean isAdminOnly() default false;

  //与数据库权限对应的id号
  int permissionIndex() default DEFAULT_INDEX;
}
