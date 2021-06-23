package net.diyigemt.miraiboot.function.console;

import net.diyigemt.miraiboot.annotation.ConsoleCommand;
import net.diyigemt.miraiboot.annotation.MiraiBootComponent;
import net.diyigemt.miraiboot.autoconfig.PluginLoader;
import net.diyigemt.miraiboot.core.PluginMgr;
import net.diyigemt.miraiboot.mirai.MiraiMain;

import java.util.List;

@MiraiBootComponent
public class ConsolePlugin {

    @ConsoleCommand("plugin")
    public void plugin(List<String> args){
        if(args == null || args.isEmpty()){
            MiraiMain.logger.error("命令：“\"plugin\"" + "缺少参数");
            return;
        }

        switch (args.get(0)){
            case "list":
                List<String> res = PluginMgr.getPluginConnectionList();
                MiraiMain.logger.info("已加载插件：");
                for (String s : res) {
                    MiraiMain.logger.info(s);
                }
                break;
            case "unload":
                if(args.get(1) == null){
                    MiraiMain.logger.error("参数: [PluginName] 不能为空");
                    break;
                }
                String PluginName = args.get(1);
                if(PluginName.matches("^.+\\.jar")){
                    PluginMgr.unLoadPlugin(PluginName);
                } else {
                    MiraiMain.logger.error("非法的MiraiBoot插件名称");
                }
                break;
            case "load":
                if(args.get(1) == null){
                    MiraiMain.logger.error("参数: [PluginName] 不能为空");
                    break;
                }
                String Path = args.get(1);
                if(Path.matches("^.+\\.jar")){
                    PluginLoader.LoadPlugin(Path);
                } else {
                    MiraiMain.logger.error("参数：[Path] 不是有效插件名称");
                }
                break;

            case "reload":
                if(args.get(1) == null){
                    MiraiMain.logger.error("参数: [PluginName] 不能为空");
                    break;
                }
                Path = args.get(1);
                if(Path.matches("^.+\\.jar")){
                    PluginLoader.LoadPlugin(Path);
                } else {
                    MiraiMain.logger.error("参数：[Path] 不是有效插件名称");
                }
                break;
        }
    }
}
