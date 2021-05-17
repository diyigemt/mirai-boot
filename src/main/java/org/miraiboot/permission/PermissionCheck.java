package org.miraiboot.permission;

import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.Nullable;
import org.miraiboot.annotation.MessagePreProcessor;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.constant.MessagePreProcessorMessageType;
import org.miraiboot.entity.EventHandlerItem;
import org.miraiboot.entity.PermissionItem;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.PermissionUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PermissionCheck {

  public static boolean checkGroupPermission(EventHandlerItem item, GroupMessageEvent event, int commandId) {
//    Method handler = item.getHandler();
//    Class<?> aClass = item.getInvoker();
//    CheckPermission classAnnotation = aClass.getAnnotation(CheckPermission.class);
//    CheckPermission methodAnnotation = null;
//    if (handler != null) methodAnnotation = handler.getAnnotation(CheckPermission.class);
//    Permission permission = PermissionCheck.getGroupPermission(methodAnnotation, classAnnotation);
//    long botId = event.getBot().getId();
    // 判断是否为At
//    if (permission.isAt() && !event.getMessage().contains(new At(botId))) return false;
    // 数据库动态权限检查
    if(commandId == 0) return true;//默认值不查
    try{
      PermissionItem permissionItem = PermissionUtil.getInstance().getPermissionItem(event.getSender().getId(), String.valueOf(commandId));
      if(permissionItem.isPermit().equals("false")){
        return false;
      }
    } catch (NullPointerException e){//数据库没相关记录，说明没有被禁过
      return true;
    }
//    PermissionItem permissionItem = PermissionUtil.getInstance().getPermissionItem(event.getSender().getId(), command.getType().getIndex());
//    if (permissionItem != null) {
//      return Boolean.parseBoolean(permissionItem.isPermit());
//    }
    // 判断数据库中的权限和@Annotation写死的权限
//    if (!checkList(permission, event, command)) return false;
    return true;
  }

  public static boolean identityCheck(EventHandlerItem item, GroupMessageEvent event){//群员身份检查，优先级低
    MemberPermission memberPermission = event.getSender().getPermission();
    Method handler = item.getHandler();
    Class<?> aClass = item.getInvoker();
    CheckPermission classAnnotation = aClass.getAnnotation(CheckPermission.class);
    CheckPermission methodAnnotation = null;
    if (handler != null) methodAnnotation = handler.getAnnotation(CheckPermission.class);
    Permission permission = PermissionCheck.getGroupPermission(methodAnnotation, classAnnotation);
    if (permission.isGroupOwnerOnly()) return memberPermission == MemberPermission.OWNER;
    if (permission.isAdminOnly()) return memberPermission == MemberPermission.OWNER || memberPermission == MemberPermission.ADMINISTRATOR;
    return true;
  }

  public static Permission getGroupPermission(CheckPermission methodAnnotation, CheckPermission classAnnotation) {
    Permission permission = new Permission();
    CheckPermission check = methodAnnotation != null ? methodAnnotation : classAnnotation;
    if (check != null) {
      permission.getAllows().addAll(Arrays.asList(check.allows()));
      permission.getBlocks().addAll(Arrays.asList(check.blocks()));
      permission.setAdminOnly(check.isAdminOnly());
      permission.setGroupOwnerOnly(check.isGroupOwnerOnly());
    }
    return permission;
  }

  public static boolean strictRestrictedCheck(GroupMessageEvent event, PreProcessorData data){
    MemberPermission senderPermissions = event.getSender().getPermission();
    int senderAuthLevel = senderPermissions.ordinal();
    List<String> args = data.getArgs();
    long targetId = -1L;
    String temp = args.get(2);
    try{
      temp = temp.substring(temp.lastIndexOf(":") + 1, temp.lastIndexOf("]"));
      targetId = Long.parseLong(temp);
    }catch (Exception e){
      MiraiMain.getInstance().quickReply(event, "目标成员不存在");
      return true;
    }
    MemberPermission targetPermission = Objects.requireNonNull(event.getGroup().get(targetId)).getPermission();
    int targetAuthLevel = targetPermission.ordinal();
    if(senderAuthLevel <= targetAuthLevel){
      return false;
    }
    return true;
  }
}
