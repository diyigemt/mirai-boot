package org.miraiboot.permission;

import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.mirai.MiraiMain;

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
        if(args == null || args.size() > 3){
            MiraiMain.getInstance().quickReply(event, "获取参数出错");
            return;
        }
        
    }
}
