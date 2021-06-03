package net.diyigemt.miraiboot.core;

import net.diyigemt.miraiboot.mirai.MiraiMain;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class PluginMgr {

    private static List<JarURLConnection> Plugin_Cache = new ArrayList<>();

    public static void addPluginConnection(JarURLConnection connection){
        Plugin_Cache.add(connection);
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

//    public static void unLoadPlugin(String jarFileName){
//        JarURLConnection connection = getConnection(jarFileName);
//        if(connection == null){
//            MiraiMain.logger.error("未找到插件：" + jarFileName);
//            return;
//        }
//        try{
//            connection.getJarFile().close();//关闭插件jar打开状态
//            Thread.currentThread().setContextClassLoader(null);//销毁该插件的ClassLoader
//            System.gc();//让JVM启动垃圾回收
//            Plugin_Cache.remove(connection);//从当前清单中除名
//        }catch (IOException e){
//            MiraiMain.logger.error("插件：" + jarFileName + " 卸载失败，请关闭本程序后手动删除插件");
//            return;
//        }
//
//        MiraiMain.logger.info("插件：" + jarFileName + " 卸载成功");
//    }
//
//    public static void LoadPlugin(JarFile Plugin){
//    }

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
