package org.miraiboot.permission;

import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import org.miraiboot.entity.EventHandlerItem;
import org.miraiboot.entity.PermissionItem;
import org.miraiboot.utils.PermissionUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class CheckPermission {

  public static boolean checkGroupPermission(EventHandlerItem item, GroupMessageEvent event) {
    Method handler = item.getHandler();
    Class<?> aClass = item.getInvoker();
    GroupPermission classAnnotation = aClass.getAnnotation(GroupPermission.class);
    GroupPermission methodAnnotation = null;
    if (handler != null) methodAnnotation = handler.getAnnotation(GroupPermission.class);
    Permission permission = CheckPermission.getGroupPermission(methodAnnotation, classAnnotation);
    long botId = event.getBot().getId();
    // 判断是否为At
    if (permission.isAt() && !event.getMessage().contains(new At(botId))) return false;
    // 判断数据库中的权限
//    PermissionItem permissionItem = PermissionUtil.getInstance().getPermissionItem(event.getSender().getId(), command.getType().getIndex());
//    if (permissionItem != null) {
//      return Boolean.parseBoolean(permissionItem.isPermit());
//    }
    // 判断数据库中的权限和@Annotation写死的权限
//    if (!checkList(permission, event, command)) return false;
    MemberPermission memberPermission = event.getSender().getPermission();
    if (permission.isGroupOwnerOnly()) return memberPermission == MemberPermission.OWNER;
    if (permission.isAdminOnly()) return memberPermission == MemberPermission.OWNER || memberPermission == MemberPermission.ADMINISTRATOR;
    return true;
  }

//  public static boolean checkFriendPermission(CommandInvoker invoker, FriendMessageEvent event, Command command) {
//    Method method = null;
//    Class<? extends CommandInvoker> aClass = invoker.getClass();
//    FriendPermission classAnnotation = aClass.getAnnotation(FriendPermission.class);
//    try {
//      method = aClass.getMethod("invoke", MessageEvent.class, Command.class);
//    } catch (NoSuchMethodException e) {
//      e.printStackTrace();
//    }
//    FriendPermission methodAnnotation = null;
//    if (method != null) methodAnnotation = method.getAnnotation(FriendPermission.class);
//    Permission permission = CheckPermission.getFriendPermission(methodAnnotation, classAnnotation);
//    // 判断数据库中的权限
//    PermissionItem item = PermissionUtil.getInstance().getPermissionItem(event.getSender().getId(), command.getType().getIndex());
//    if (item != null) {
//      return Boolean.getBoolean(item.isPermit());
//    }
//    return checkList(permission, event, command);
//  }
//
//  private static boolean checkList(Permission permission, MessageEvent event, Command command) {
//    Set<String> blocks = permission.getBlocks();
//    String senderId = String.valueOf(event.getSender().getId());
//    if (!blocks.isEmpty() && blocks.contains(senderId)) return false;
//    Set<String> allows = permission.getAllows();
//    if (allows.isEmpty()) return true;
//    return allows.contains(senderId);
//  }

  public static Permission getGroupPermission(GroupPermission methodAnnotation, GroupPermission classAnnotation) {
    Permission permission = new Permission();
    GroupPermission check = methodAnnotation != null ? methodAnnotation : classAnnotation;
    if (check != null) {
      permission.getAllows().addAll(Arrays.asList(check.allows()));
      permission.getBlocks().addAll(Arrays.asList(check.blocks()));
      permission.setAdminOnly(check.isAdminOnly());
      permission.setGroupOwnerOnly(check.isGroupOwnerOnly());
    }
    return permission;
  }

  public static Permission getFriendPermission(FriendPermission methodAnnotation, FriendPermission classAnnotation) {
    Permission permission = new Permission();
    FriendPermission check = methodAnnotation != null ? methodAnnotation : classAnnotation;
    if (check != null) {
      permission.getAllows().addAll(Arrays.asList(check.allows()));
      permission.getBlocks().addAll(Arrays.asList(check.blocks()));
    }
    return permission;
  }
}
