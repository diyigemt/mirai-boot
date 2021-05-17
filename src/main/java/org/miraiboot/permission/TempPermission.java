package org.miraiboot.permission;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.PermissionItem;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.PermissionUtil;

public class TempPermission {
    public static void tempAuthProcess(GroupMessageEvent event, int commandId){
        long senderId = event.getSender().getId();
        PermissionItem permissionItem = null;
        permissionItem = PermissionUtil.getInstance().getPermissionItem(event.getSender().getId(), String.valueOf(commandId));
        if(permissionItem == null){// 数据库里没有记录，开始授权
            PermissionUtil.getInstance().addPermissionItem(senderId, commandId, 1);
            MiraiMain.getInstance().quickReply(event, "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，授权成功");
            return;
        }
        else if(permissionItem.getPermits() <= 0 && permissionItem.getSenderId() == senderId){//数据库中有被禁止使用当前功能的记录，取消禁用并授权
            PermissionUtil.getInstance().enablePermissionItem(senderId, commandId, 1);
            MiraiMain.getInstance().quickReply(event, "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，授权成功, 禁用已解除");
            return;
        }
        else {
            MiraiMain.getInstance().quickReply(event, "该用户已授权");
        }
    }

    public static void cancelAuthProcess(GroupMessageEvent event, int commandId){
        long senderId = event.getSender().getId();
        PermissionItem permissionItem = PermissionUtil.getInstance().getPermissionItem(senderId, String.valueOf(commandId));
        if(permissionItem != null){
            PermissionUtil.getInstance().removePermissionItem(event.getSender().getId(), commandId);
            MiraiMain.getInstance().quickReply(event, "对用户" + senderId + "的" + FunctionId.getKey(commandId) + "功能，解除授权成功");
        } else {
            MiraiMain.getInstance().quickReply(event, "该用户并没有授予相关权限");
        }
    }
}
