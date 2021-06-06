package net.diyigemt.miraiboot.function.console;

import net.diyigemt.miraiboot.annotation.ConsoleCommand;
import net.diyigemt.miraiboot.annotation.MiraiBootComponent;
import net.diyigemt.miraiboot.autoconfig.PluginLoader;
import net.diyigemt.miraiboot.mirai.MiraiMain;

import java.util.List;

@MiraiBootComponent
public class ConsoleLoad {

    @ConsoleCommand("load")
    public void load(List<String> args){
        if (args == null || args.isEmpty()) {
            MiraiMain.logger.error("参数：[Path] 不能为空");
            return;
        }
        String PluginName = args.get(0);
        if(PluginName.matches("^.+\\.jar")){
            PluginLoader.LoadPlugin(PluginName);
        } else {
            MiraiMain.logger.error("参数：[Path] 不是有效路径");
        }

    }
}
