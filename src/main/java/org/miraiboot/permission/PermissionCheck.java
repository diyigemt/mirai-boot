package org.miraiboot.permission;

import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
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

  public static boolean checkGroupPermission(GroupMessageEvent event, int commandId) {
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
    if(commandId == 0) return false;//默认值不查
    PermissionItem permissionItem;
    try{
      permissionItem = PermissionUtil.getInstance().getPermissionItem(event.getSender().getId(), String.valueOf(commandId));
      if(permissionItem.getPermits() > 0){//被授予临时权限
        return true;
      }
    }catch (NullPointerException e){//数据库没相关记录，说明没有任何禁用和授权
      return false;
    }
    if(permissionItem.getPermits() <= 0){
      MiraiMain.getInstance().quickReply(event, "您的管理员已禁止您使用该功能");
      return false;
    }
//    else if(permissionItem.getPermits() > 0){//被授予临时权限
//      return true;
//    }
//    PermissionItem permissionItem = PermissionUtil.getInstance().getPermissionItem(event.getSender().getId(), command.getType().getIndex());
//    if (permissionItem != null) {
//      return Boolean.parseBoolean(permissionItem.isPermit());
//    }
    // 判断数据库中的权限和@Annotation写死的权限
//    if (!checkList(permission, event, command)) return false;
    return false;
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

  public static boolean strictRestrictedCheck(GroupMessageEvent event){
    MemberPermission senderPermissions = event.getSender().getPermission();
    int senderAuthLevel = senderPermissions.ordinal();
    MessageChain content = event.getMessage();
    long botId = event.getBot().getId();
    long targetId = -1L;
    for(SingleMessage s : content){
      if(s instanceof At && ((At) s).getTarget() != botId){
        targetId = ((At) s).getTarget();
      }
    }
    MemberPermission targetPermission = Objects.requireNonNull(event.getGroup().get(targetId)).getPermission();
    int targetAuthLevel = targetPermission.ordinal();
    if(senderAuthLevel <= targetAuthLevel){
      return false;
    }
    return true;
  }

  public static boolean individualAuthCheck(EventHandlerItem item, GroupMessageEvent event){
    Method method = item.getHandler();
    String[] allows = method.getAnnotation(CheckPermission.class).allows();
    String[] blocks = method.getAnnotation(CheckPermission.class).blocks();
    long senderId = event.getSender().getId();
    if(allows.length != 0){
      for(String i : allows){
        if(Long.parseLong(i) == senderId){
          return true;
        }
      }
      return false;
    }
    if(blocks.length != 0){
      for(String i : blocks){
        if(Long.parseLong(i) == senderId){
          return false;
        }
      }
      return true;
    }

    return true;
  }

  public static boolean atValidationCheck(PreProcessorData data){
    List<SingleMessage> args = data.getClassified();
    if(args.size() == 0) return true;
    if(!args.contains("[Mirai:")){
      return false;
    }
    return true;
  }
}
