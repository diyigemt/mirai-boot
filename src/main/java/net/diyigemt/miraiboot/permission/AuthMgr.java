package net.diyigemt.miraiboot.permission;

import net.diyigemt.miraiboot.annotation.MessagePreProcessor;
import net.diyigemt.miraiboot.constant.MessagePreProcessorMessageType;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.SingleMessage;
import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.constant.FunctionId;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PermissionItem;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.utils.PermissionUtil;

import java.util.List;

/**
 * <h2>权限控制台</h2>
 * <p>此功能仅限群主和管理员使用，普通群员没有足够权限使用该命令</p>
 * <p>此功能强制开启严格限制模式</p>
 * <p>此功能不能通过临时权限授权</p>
 * @author Haythem
 * @since 1.0.0
 */

@EventHandlerComponent
public class AuthMgr {

    @EventHandler(target = "permit")
    @CheckPermission(isAdminOnly = true, isStrictRestricted = true, FunctionID = FunctionId.permit)
    @MessagePreProcessor(filterType = MessagePreProcessorMessageType.At)
    public void authorityManager(MessageEventPack eventPack, PreProcessorData data){
        List<String> args = data.getArgs();
        if(args == null){
//            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "获取参数出错");
            eventPack.reply("获取参数出错");
            return;
        }
        List<SingleMessage> classified = data.getClassified();
        long senderId = -1L;
        for (SingleMessage message : classified) {
            senderId = ((At) message).getTarget();
            if (senderId != eventPack.getBotId()) break;
        }
        if(senderId == -1L){
            eventPack.reply("@成员不存在");
            return;
        }
        int commandId = 0;

        //临时权限剩余次数限制
        int remain = -2;//-1代表无限制
        try{
            remain = Integer.parseInt(args.get(2));
            if(remain == 0 || remain < 0){
//                MiraiMain.getInstance().quickReply(eventPack.getEvent(), "参数：次数限制必须 > 0");
                eventPack.reply("参数：次数限制必须 > 0");
                return;
            }
        }catch (IndexOutOfBoundsException e){
            remain = -1;
        }catch (NumberFormatException e){
            eventPack.reply("参数：次数限制输入非法");
            return;
        }
        try{
            if(args.get(0).contains("assign") || args.get(0).contains("cancel")){
                commandId = FunctionId.getMap(args.get(1));
            }else{
                commandId = FunctionId.getMap(args.get(0));
            }
        }catch (NullPointerException e){
//            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "一个或多个参数无效");
            eventPack.reply("一个或多个参数无效");
            return;
        }
        // 说明指令并没有被CheckPermission注解 不能进行权限判断
        if (commandId == 0) return;

        int permit = 2;
        if(args.get(1).equals("off")){
            permit = 0;
        }else if(args.get(1).equals("on")){
            permit = 1;
        }else if(args.get(0).contains("assign") || args.get(0).contains("cancel")){
            if(args.get(0).equals("assign")){
                permit = -1;
                TempPermission.tempAuthProcess(eventPack, commandId, remain);
            }
            else if(args.get(0).equals("cancel")){
                permit = -1;
                TempPermission.cancelAuthProcess(eventPack, commandId);
            }
        }
        if(permit == 2 || (senderId == -1L) || senderId == eventPack.getBotId()){
//            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "命令：permit 无法将“permit”项识别为 函数、脚本文件或可运行程序的名称，请检查参数的拼写。");
            eventPack.reply("命令：permit 无法将“permit”项识别为 函数、脚本文件或可运行程序的名称，请检查参数的拼写。");
            permit = 2;
            return;
        }
        if(permit == 0){
            PermissionItem permissionItem = PermissionUtil.getInstance().getPermissionItem(senderId, String.valueOf(commandId));
            try{
                if(permissionItem.getSenderId() == senderId && permissionItem.getPermits() > 0){//数据库中存在临时权限
                    permissionItem.setPermits(0);
                    PermissionUtil.getInstance().updatePermissionItem(permissionItem);
//                    MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + args.get(0) + "功能，禁用完成，临时权限已取消");
                    eventPack.reply("对用户" + senderId + "的" + args.get(0) + "功能，禁用完成，临时权限已取消");
                }else {
//                    MiraiMain.getInstance().quickReply(eventPack.getEvent(), "该用户已被禁用，无需操作");
                    eventPack.reply("该用户已被禁用，无需操作");
                }
            }catch (NullPointerException e){//数据库没有记录
                PermissionUtil.getInstance().addPermissionItem(senderId, commandId);
//                MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + args.get(0) + "功能，禁用完成");
                eventPack.reply("对用户" + senderId + "的" + args.get(0) + "功能，禁用完成");
            }

        }else if(permit == 1){
            PermissionItem pi = PermissionUtil.getInstance().getPermissionItem(senderId, String.valueOf(commandId));
            if(pi == null){
                eventPack.reply("该用户并没有被禁用相关功能");
                return;
            }
            PermissionUtil.getInstance().removePermissionItem(senderId, commandId);
//            MiraiMain.getInstance().quickReply(eventPack.getEvent(), "对用户" + senderId + "的" + args.get(0) + "功能，解锁完成");
            eventPack.reply("对用户" + senderId + "的" + args.get(0) + "功能，解锁完成");
        }
    }
}
