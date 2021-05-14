package org.miraiboot.permission;

import java.lang.annotation.*;

/**
 * 好友消息权限规定
 * @author diyigemt
 * @since 3.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FriendPermission {

  String[] allows() default {};

  String[] blocks() default {};
}
