package net.diyigemt.miraiboot.core;

import net.diyigemt.miraiboot.autoconfig.JarPluginLoader;
import net.diyigemt.miraiboot.entity.PluginItem;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.EventHandlerManager;
import net.diyigemt.miraiboot.utils.ExceptionHandlerManager;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginMgr {

    private static List<JarURLConnection> Plugin_Cache = new ArrayList<>();

    /**
     * <h2>加载器集合</h2>
     */
    private static List<Map<String, JarPluginLoader>> loaders = new ArrayList<>();

    private static Map<String, List<PluginItem>> manifests = new HashMap<>();

//    private static List<Method> unLoadControllers = new ArrayList<>();

    public static void addPluginConnection(JarURLConnection connection){
        Plugin_Cache.add(connection);
    }

//    public static void addUnloadController(Method UnloadController){
//        unLoadControllers.add(UnloadController);
//    }

    public static void addPluginManifest(String jarName, List<PluginItem> pluginItemList){
        manifests.put(jarName, pluginItemList);
    }

    public static void addLoader(JarPluginLoader loader, String jarName){
        Map<JarPluginLoader, String> map = new HashMap<>();
        map.put(loader, jarName);
    }

    private static void RemoveLoader(String jarName) throws IOException {
        for(Map<String, JarPluginLoader> map : loaders){
            JarPluginLoader loader = map.get(jarName);
            if(loader != null){
                loader.close();//关闭URL链接
                loader = null;//释放该loader
                loaders.remove(map);//将loader从List中移除
                return;
            }
        }
    }

    public static List<String> getPluginConnectionList(){
        List<String> PluginList = new ArrayList<>();
        try{
            for(JarURLConnection connection : Plugin_Cache){
                PluginList.add(connection.getJarFile().getName());
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return PluginList;
    }

    public static void unLoadPlugin(String jarFileName){
        JarURLConnection connection = getConnection(jarFileName);
        if(connection == null){
            MiraiMain.logger.error("未找到插件：" + jarFileName);
            return;
        }
        try{
            List<PluginItem> pluginItem = manifests.get(jarFileName);
            connection.getJarFile().close();//关闭插件jar打开状态
            RemoveLoader(jarFileName);//销毁该插件的ClassLoader
            //TODO: 释放该插件的实例化
            EventHandlerManager.getInstance().onUnload(pluginItem);//注销EventHandler
            ExceptionHandlerManager.getInstance().onUnload(pluginItem);//注销ExceptionHandler
            MiraiBootConsole.getInstance().onUnload(pluginItem);//注销控制台指令
//            List<Map<String, JarPluginLoader>> list = loaders;
            System.gc();//让JVM启动垃圾回收
            Plugin_Cache.remove(connection);//从当前清单中除名
        }catch (IOException e){
            MiraiMain.logger.error("插件：" + jarFileName + " 卸载失败，请关闭本程序后手动删除插件");
            return;
        }

        MiraiMain.logger.info("插件：" + jarFileName + " 卸载成功");
    }

    private static JarURLConnection getConnection(String jarFileName){
        try{
            for(JarURLConnection connection : Plugin_Cache){
                if(connection.getJarFile().getName().contains(jarFileName)){
                    return connection;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
