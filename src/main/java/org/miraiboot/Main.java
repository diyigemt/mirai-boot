package org.miraiboot;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.autoconfig.GlobalLoader;
import org.miraiboot.autoconfig.MainMiraiApplication;
import org.miraiboot.autoconfig.MiraiApplication;
import org.miraiboot.listener.GroupListener;

import java.util.Scanner;

@Slf4j
@MiraiBootApplication(description = "测试项目")
public class Main implements MainMiraiApplication {

  public static void main(String[] args) {
    MiraiApplication.run(Main.class, args);
  }

  public Bot bot() {
    Bot bot = BotFactory.INSTANCE.newBot(1741557205L, "Hunter28", botConfiguration -> {
      botConfiguration.fileBasedDeviceInfo("device.json");
      botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
      botConfiguration.noNetworkLog();
    });
    bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, new GroupListener());
    return bot;
  }

  @Override
  public void run(String... args) {
    GlobalLoader.init();
    Scanner scanner = new Scanner(System.in);
    Bot bean = bot();
    bean.login();
    while (true) {
      String command = scanner.next();
      if (command.equals("exit")) {
        bean.close();
        break;
      }
    }
  }
}
