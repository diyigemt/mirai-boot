package org.miraiboot.permission;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.MessagePreProcessor;
import org.miraiboot.annotation.MessagePreProcessors;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.constant.MessagePreProcessorMessageType;
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
    @MessagePreProcessor(filterType = MessagePreProcessorMessageType.At)
    public void authorityManager(MessageEvent event, PreProcessorData data){
        List<String> args = data.getArgs();
        if(args == null || args.size() > 2){
            MiraiMain.getInstance().quickReply(event, "获取参数出错");
            return;
        }
        List<SingleMessage> classified = data.getClassified();
        long senderId = -1L;
        for (SingleMessage message : classified) {
            senderId = ((At) message).getTarget();
            if (senderId != event.getBot().getId()) break;
        }
        int commandId = 0;
        try{
            commandId = FunctionId.getMap(args.get(0));
        }catch (NullPointerException e){
            MiraiMain.getInstance().quickReply(event, "一个或多个参数无效");
            return;
        }
        int permit = 2;
        if(args.get(1).equals("off")){
            permit = 0;
        }else if(args.get(1).equals("on")){
            permit = 1;
        }
        if(permit == 2 || commandId == 0 || senderId == -1L){
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
