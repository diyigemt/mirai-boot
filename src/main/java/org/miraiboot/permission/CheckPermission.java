package org.miraiboot.permission;

import java.lang.annotation.*;

import static org.miraiboot.constant.FunctionId.DEFAULT_INDEX;

/**
 * <h2>群消息权限规定</h2>
 * <p>将此注解加在有@EventHandler注解的方法上，将对此方法加入权限管理属性</p>
 * <p>优先级 at-> permission table -> blocks -> allows -> isStrictRestricted -> isGroupOwnerOnly -> isAdminOnly</p>
 * <p>allows: 允许某人使用，其余的成员均无法使用</p>
 * <p>blocks: 禁止某人使用，其余的成员均可以使用</p>
 * <p>isStrictRestricted: 严格管理模式，开启时将严格划分权限级别，详情看【1】</p>
 * <p>isGroupOwnerOnly: 群主独占功能，开启后此功能仅限群主使用</p>
 * <p>isAdminOnly: 管理层独占功能，开启后此功能仅限群主和管理员使用</p>
 * <p></p>
 * <p>注：</p>
 * <p>【1】 严格管理模式：不允许越级操作（包括同级别权限）</p>
 * <p>权限划分：</p>
 * <p>群主 -> 管理员 -> 普通群员</p>
 * <p>2. 严格管理模式对permit指令强制打开，无法修改</p>
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
  boolean isStrictRestricted() default false;

  //是否只允许群主
  boolean isGroupOwnerOnly() default false;

  //是否允许管理员(包括群主)
  boolean isAdminOnly() default false;

  //与数据库权限对应的id号
  int permissionIndex() default DEFAULT_INDEX;
}
