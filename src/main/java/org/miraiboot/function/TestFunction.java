package org.miraiboot.function;


import net.mamoe.mirai.event.events.MessageEvent;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.annotation.HttpsProperties;
import org.miraiboot.constant.FunctionId;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.permission.CheckPermission;
import org.miraiboot.utils.SendMessageUtil;

@EventHandlerComponent
public class TestFunction{

  @EventHandler(target = "reply")
  @CheckPermission(isAdminOnly = true, permissionIndex = FunctionId.reply)
  @HttpsProperties(Timeout = 2000, RequestMethod = "GET", RequestProperties = {"Connection", "keep-alive"})
  public void testReply(MessageEvent event, PreProcessorData data) {
//    event.getSubject().sendMessage(event.getMessage());
    String Path = "G:\\素材\\header.jpg";
    String soundPath = "C:\\Users\\Administrator\\Desktop\\01-1.mp3";
    String soundURL = "https://meamea.moe/voices/01-1.mp3";
    String urlPath = "https://i0.hdslb.com/bfs/article/0a53e07dd26d946adfe9fe843263bc12ef441bf8.jpg@860w_482h.jpg";
    String[] msg = {"ffa", "faf"};
    String[] Paths = {"G:\\素材\\header.jpg","https://i0.hdslb.com/bfs/article/0a53e07dd26d946adfe9fe843263bc12ef441bf8.jpg@860w_482h.jpg"};

    SendMessageUtil.ImageMessageSender(event, urlPath);
  }
}
