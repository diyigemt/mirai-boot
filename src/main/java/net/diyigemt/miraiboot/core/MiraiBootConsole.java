package net.diyigemt.miraiboot.core;

import net.diyigemt.miraiboot.autoconfig.PluginLoader;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.BotManager;
import net.diyigemt.miraiboot.utils.EventHandlerManager;

import java.util.List;
import java.util.Scanner;

/**
 * <h2>MiraiBoot控制台</h2>
 * @author diyigemt
 */
public class MiraiBootConsole {

    /**
     * <h2>控制台输入监听</h2>
     */
    public static void ConsoleListener(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.next();
            //退出命令
            if (command.equals("exit")) {
                EventHandlerManager.getInstance().cancelAll();
                BotManager.getInstance().logoutAll();
                break;
            }
//            热卸载插件命令
            else if(command.matches("^unload")){
                String PluginName = scanner.next();
                if(PluginName.matches("^.+\\.jar")){
                    PluginMgr.unLoadPlugin(PluginName);
                }else MiraiMain.logger.error("非法的MiraiBoot插件名称");
            }
            //已加载插件查询
            else if(command.equals("plugin")){
                List<String> res = PluginMgr.getPluginConnectionList();
                if(res.size() == 0){
                    MiraiMain.logger.info("MiraiBoot没有加载任何插件");
                } else{
                    MiraiMain.logger.info("已加载插件：");
                    for(String s : res){
                        MiraiMain.logger.info(s);
                    }
                }
            }
            //加载插件指令
            else if(command.matches("^load")){
                String JarPath = scanner.next();
                if(JarPath.equals(command)){
                    MiraiMain.logger.error("无效的命令格式：\nHelp：load [Path]");
                }
                PluginLoader.LoadPlugin(JarPath);
            }
            //根本匹配不上
            else {
                MiraiMain.logger.error("命令：" + "\"" + command + "\"" + " 不是有效的MiraiBoot命令");
            }
        }
    }
}
