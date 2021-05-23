package org.miraiboot.permission;

import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.constant.EventType;
import org.miraiboot.entity.EventHandlerItem;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PermissionItem;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.EventHandlerManager;
import org.miraiboot.utils.PermissionUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <h2>权限检查工具类</h2>
 * @author diyigemt, Haythem
 * @since 1.0.0
 */

public class PermissionCheck {

  /**
   * <h2>数据库权限检查</h2>
   * <p>此检查优先级最高，可以覆盖所有其它限制的检查(除了permit)</p>
   * @param eventPack 消息事件，私聊或群聊
   * @param commandId 命令ID
   * @return 查询结果 false为通过，true为拦截
   * @author Haythem
   * @since 1.0.0
   */
  public static boolean checkGroupPermission(MessageEventPack eventPack, int commandId) { ;
    // 数据库动态权限检查
    if(commandId == 0) return false;//默认值不查
    PermissionItem permissionItem;
    try{
      permissionItem = PermissionUtil.getInstance().getPermissionItem(eventPack.getSenderId(), String.valueOf(commandId));
      if(permissionItem.getPermits() > 0){//被授予临时权限
        if(permissionItem.getRemain() > 0){//存在次数限制
          int remain = permissionItem.getRemain();
          if(remain - 1 == 0){
            //次数没了
            PermissionUtil.getInstance().removePermissionItem(permissionItem.getSenderId(), permissionItem.getCommandId());
            permissionItem.setPermits(0);
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "提示：本次操作是最后一次, 使用完成后系统将回收您的使用权");
          }else {
            permissionItem.setRemain(permissionItem.getRemain() - 1);
          }
          PermissionUtil.getInstance().updatePermissionItem(permissionItem);
          return true;
        }
        return false;
      }
    }catch (NullPointerException e){//数据库没相关记录，说明没有任何禁用和授权
      return false;
    }
    if(permissionItem.getPermits() <= 0){
      EventHandlerManager.SQLNonTempAuth = true;
      MiraiMain.getInstance().quickReply(eventPack.getEvent(), "您的管理员已禁止您使用该功能");
      return true;
    }
    return false;
  }

  /**
   * <h2>群员身份权限检查</h2>
   * <p>isAdminOnly和isGroupOwnerOnly的实现方法</p>
   * @param eventPack 消息事件，私聊或群聊
   * @param item 信息存储类
   * @return 查询结果 true为通过，false为拦截
   * @author diyigemt
   * @since 1.0.0
   */
  public static boolean identityCheck(EventHandlerItem item, MessageEventPack eventPack){//群员身份检查，优先级低
    if (eventPack.getEventType() != EventType.GROUP_MESSAGE_EVENT) return false;
    MemberPermission memberPermission = eventPack.getSenderPermission();
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

  /**
   * <h2>权限检查信息存储构造器</h2>
   * @param methodAnnotation 方法注解
   * @param classAnnotation 类注解
   * @return 构造完成的Permission类
   * @author diyigemt
   * @since 1.0.0
   */
  private static Permission getGroupPermission(CheckPermission methodAnnotation, CheckPermission classAnnotation) {
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

  /**
   * <h2>严格模式工作流程</h2>
   * <p>isStrictRestricted的实现方法</p>
   * @param eventPack 消息事件，群聊或私聊
   * @return 检查结果, true为通过，false为拦截
   * @author Haythem
   * @since 1.0.0
   */
  public static boolean strictRestrictedCheck(MessageEventPack eventPack){
    if (eventPack.getEventType() != EventType.GROUP_MESSAGE_EVENT) return false;
    MemberPermission senderPermissions = eventPack.getSenderPermission();
    int senderAuthLevel = senderPermissions.ordinal();
    MessageChain content = eventPack.getMessage();
    long botId = eventPack.getBot().getId();
    long targetId = -1L;
    for(SingleMessage s : content){
      if(s instanceof At && ((At) s).getTarget() != botId){
        targetId = ((At) s).getTarget();
      }
    }
    if(targetId == -1){
      eventPack.reply("宁禁我干嘛？");
      return false;
    }
    MemberPermission targetPermission = eventPack.getGroup().get(targetId).getPermission();
    int targetAuthLevel = targetPermission.ordinal();
    if(senderAuthLevel <= targetAuthLevel){
      return false;
    }
    return true;
  }

  /**
   * <h2>个例权限检查工作流程</h2>
   * <p>blocks和allows的实现方法</p>
   * <p></p>
   * <p>注：</p>
   * <p>优先级：allows -> blocks</p>
   * @param eventPack 消息事件，群聊或私聊
   * @param item 信息存储类
   * @return 检查结果, true为通过，false为拦截
   * @author Haythem
   * @since 1.0.0
   */
  public static boolean individualAuthCheck(EventHandlerItem item, MessageEventPack eventPack){
    Method method = item.getHandler();
    String[] allows = method.getAnnotation(CheckPermission.class).allows();
    String[] blocks = method.getAnnotation(CheckPermission.class).blocks();
    long senderId = eventPack.getSenderId();
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

  /**
   * <h2>@成员合法性检查（固定检查，无法取消）</h2>
   * <p>@成员合法性检查的实现方法</p>
   * @param data 消息预处理器，获得消息中所有@元素
   * @return 检查结果, true为通过，false为拦截
   * @author Haythem
   * @since 1.0.0
   */
  public static boolean atValidationCheck(PreProcessorData data){
    List<SingleMessage> args = data.getClassified();
    if(args.size() == 0) return true;
    if(!args.contains("[Mirai:")){
      return false;
    }
    return true;
  }
}
