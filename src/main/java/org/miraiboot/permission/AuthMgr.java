package org.miraiboot.permission;

import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;
import org.miraiboot.utils.PermissionUtil;

import java.util.List;

/**
 * 权限控制台
 * @author Haythem
 */

@EventHandlerComponent
public class AuthMgr {

    @EventHandler(target = "permit")
    @CheckPermission(isAdminOnly = true, permissionIndex = FunctionId.permit)
    public void authorityManager(MessageEvent event, PreProcessorData data){
        List<String> args = data.getArgs();
        if(args == null || args.size() > 2){
            MiraiMain.getInstance().quickReply(event, "获取参数出错");
            return;
        }
        String context = event.getMessage().serializeToMiraiCode();
        long senderId = Long.parseLong(context.substring(context.lastIndexOf(":") + 1, context.lastIndexOf("]")));
        int commandId = FunctionId.getMap(args.get(0));
        int permit = 2;
        if(args.get(1).equals("off")){
            permit = 0;
        }else if(args.get(1).equals("on")){
            permit = 1;
        }
        if(permit == 2 || commandId == 0 || senderId == 0){
            MiraiMain.getInstance().quickReply(event, "一个或多个参数无效");
            permit = 2;
            return;
        }
        if(permit == 0){
            PermissionUtil.getInstance().addPermissionItem(senderId, commandId);
            MiraiMain.getInstance().quickReply(event, "对用户" + senderId + "的" + args.get(0) + "功能，禁用完成");
        }else if(permit == 1){
            PermissionUtil.getInstance().removePermissionItem(senderId, commandId);
            MiraiMain.getInstance().quickReply(event, "对用户" + senderId + "的" + args.get(0) + "功能，解锁完成");
        }
    }
}
