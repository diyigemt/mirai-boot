package org.miraiboot.permission;

import org.miraiboot.constant.EventType;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PermissionItem;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.PermissionUtil;

/**
 * 临时权限授权和处理类
 * @author Haythem
 * @since 1.0.0
 */

public class TempPermission {

    /**
     * <h2>临时权限授权方法</h2>
     * <p>临时权限分为两种：</p>
     * <p>带次数限制的临时权限</p>
     * <p>permit assign 命令名称/别名 限制次数 @目标成员</p>
     * <p></p>
     * <p>无次数限制的临时权限</p>
     * <p>permit assign 命令名称/别名 @目标成员</p>
     * <p></p>
     * <p>注：</p>
     * <p>1. 临时权限的权限级别低于permit off的权限级别，如果管理员使用了permit off会自动解除临时权限</p>
     * <p>2. 当管理员使用assign授予临时权限时，也会自动解除permit off的限制</p>
     * <p>3. assign允许提升或降级临时权限级别，从有限次数升为无限次数或从无限次数降为有限次数，只需执行对应的有限或无限次数的assign指令即可</p>
     * <p>4. assign不能授予permit命令使用权，如有需求请联系群主设置目标为管理员</p>
     * @param eventPack 消息事件，私聊或群聊
     * @param commandId 命令ID
     * @param remain 规定的次数，默认为-1，即为无限制次数
     */
    public static void tempAuthProcess(MessageEventPack eventPack, int commandId, int remain){
        if (eventPack.getEventType() != EventType.GROUP_MESSAGE_EVENT) return;
        long senderId = eventPack.getSenderId();
        PermissionItem permissionItem = null;
        permissionItem = PermissionUtil.getInstance().getPermissionItem(senderId, String.valueOf(commandId));
        if(permissionItem == null){// 数据库里没有记录，开始授权
            PermissionUtil.getInstance().addPermissionItem(senderId, commandId, 1, remain);
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，临时授权成功");
            return;
        }
        else if(permissionItem.getPermits() <= 0 && permissionItem.getSenderId() == senderId){//数据库中有被禁止使用当前功能的记录，取消禁用并授权
            permissionItem.setPermits(1);
            PermissionUtil.getInstance().updatePermissionItem(permissionItem);
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，临时授权成功, 禁用已解除");
            return;
        }
        else if(permissionItem.getRemain() < 0 && permissionItem.getSenderId() == senderId && remain != -2){//数据库存在高级权限
            permissionItem.setRemain(remain);
            PermissionUtil.getInstance().updatePermissionItem(permissionItem);
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，降低权限成功, 次数限制已生效");
        }
        else if(permissionItem.getRemain() > 0 && permissionItem.getSenderId() == senderId && remain == -2){//数据库存在低级权限
            permissionItem.setRemain(-1);
            PermissionUtil.getInstance().updatePermissionItem(permissionItem);
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，提升权限成功, 次数限制已解除");
        }
        else {
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "该用户已授权");
        }
    }

    /**
     * <h2>解除临时权限授权方法</h2>
     * <p>解除目标用户的临时使用权</p>
     * <p>permit cancel 命令名称/别名 @目标成员</p>
     * <p></p>
     * <p>注：</p>
     * <p>解除临时权限不会解除permit off的限制</p>
     * @param eventPack 消息事件，私聊或群聊
     * @param commandId 命令ID
     */
    public static void cancelAuthProcess(MessageEventPack eventPack, int commandId){
        if (eventPack.getEventType() != EventType.GROUP_MESSAGE_EVENT) return;
        long senderId = eventPack.getSender().getId();
        PermissionItem permissionItem = PermissionUtil.getInstance().getPermissionItem(senderId, String.valueOf(commandId));
        if(permissionItem != null){
            PermissionUtil.getInstance().removePermissionItem(eventPack.getSender().getId(), commandId);
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，解除授权成功");
        } else {
            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "该用户并没有授予相关权限");
        }
    }
}
