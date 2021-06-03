package net.diyigemt.miraiboot.autoconfig;

import net.diyigemt.miraiboot.Main;
import net.diyigemt.miraiboot.annotation.AutoInit;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.ExceptionHandlerComponent;
import net.diyigemt.miraiboot.core.PluginMgr;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * <h2>插件加载器</h2>
 * @author Haythem
 * @author diyigemt
 */
public class PluginLoader {

    //通过检查的最终结果
    private static List<Class<?>> classes = new ArrayList<>();

    //读取缓冲，等待检查
    private static List<Class<?>> Plugin_Temp = new ArrayList<>();

    public static URLClassLoader loader;

    private static boolean flag = false;

    private static boolean isUEFI  = true;

    public static List<Class<?>> getPluginClasses(){
        MiraiMain.logger.info("开始扫描插件");
        try{
            FileUtil.init(Main.class);
            File[] plugins = FileUtil.getInstance().getPlugins();
            for(File file : plugins){
                String fileName = file.getName();
                if(fileName.endsWith(".jar")){
                    URL url = new URL("jar:file:/" + file.getAbsolutePath() + "!/");
                    JarURLConnection connection = (JarURLConnection) url.openConnection();
                    JarFile jar = connection.getJarFile();
                    loader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
                    Plugin_Temp.clear();//重新初始化
                    Enumeration<JarEntry> files = jar.entries();
                    Manifest manifest = jar.getManifest();
                    String MainClass = manifest.getMainAttributes().getValue("Main-Class");
                    String PackName = MainClass.substring(0, MainClass.lastIndexOf(".")).replace(".", "/");//获取主方法所在的包路径
                    //反射onLoad方法
                    Class<?> mainClass = loader.loadClass(MainClass);
                    Method onLoad = null;
                    try{
                        onLoad = mainClass.getMethod("onLoad");
                    }catch (NoSuchMethodException e){
                        MiraiMain.logger.error(file.getName() + ": 未知的MiraiBoot插件格式");
                        continue;
                    }
                    onLoad.invoke(mainClass.newInstance());

                    Field UEFI = mainClass.getDeclaredField("UEFIMode");//获取UEFI设置
                    UEFI.setAccessible(true);
                    isUEFI = (boolean) UEFI.get(mainClass);

                    if(isUEFI){
                        MiraiMain.logger.info("正在加载： " + fileName + "(UEFI)");
                        QuickJarScanner(files, PackName);//UEFI（迫真）
                        if(Plugin_Temp.size() == 0){//扫了一圈啥也没有
                            GlobalJarScanner(files);//LEGACY（传统）
                        }
                    }else {
                        MiraiMain.logger.info("正在加载： " + fileName);
                        GlobalJarScanner(files);
                    }

                    if(flag){//加载过程中存在失败
                        Plugin_Temp.clear();//因为存在依赖加载失败，为了程序稳定性，放弃扫描该插件的所有结果
                        connection.getJarFile().close();
                        MiraiMain.logger.error("插件: " + file.getName() + ": 启动失败，缺少部分依赖");
                        continue;
                    }
                    if(Plugin_Temp.size() == 0){//还是啥也没有
                        connection.getJarFile().close();
                        MiraiMain.logger.error("插件: " + file.getName() + ": 该插件中未找到任何有效的类，加载失败");
                    }else {
                        classes.addAll(Plugin_Temp);
                        PluginMgr.addPluginConnection(connection);
                        MiraiMain.logger.info("插件: " + file.getName() + "，加载成功");
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }


    public static void LoadPluginDependencies(List<JarFile> dependencies){
        for(JarFile file : dependencies){
            Enumeration<JarEntry> files = file.entries();
            GlobalJarScanner(files);
        }
    }

    private static void QuickJarScanner(Enumeration<JarEntry> files, String PackName) {
        while (files.hasMoreElements()) {
            JarEntry jarEntry = files.nextElement();
            String name = jarEntry.getName();
            if (!jarEntry.isDirectory() && name.endsWith(".class") && name.contains(PackName)){
                String className = name.substring(0, name.length() - 6).replace("/", ".");
                LoadProcess(className);
            }
        }
    }

    private static void GlobalJarScanner(Enumeration<JarEntry> files){
        while (files.hasMoreElements()) {
            JarEntry jarEntry = files.nextElement();
            String name = jarEntry.getName();
            if (!jarEntry.isDirectory() && name.endsWith(".class")){
                String className = name.substring(0, name.length() - 6).replace("/", ".");
                LoadProcess(className);
            }
        }
    }

    private static void LoadProcess(String className) {
        if(className.equals("module-info") || className.startsWith("META-INF")) return;//无意义的class文件
        Class<?> classFile = null;
        try{
            classFile = loader.loadClass(className);
        }catch (ClassNotFoundException e){
            MiraiMain.logger.error( className + ": 加载失败\n缺少依赖: " + e.getMessage().replace("/", "."));
            flag = true;
            return;
        }
        if(classFile.isAnnotationPresent(EventHandlerComponent.class)
                || classFile.isAnnotationPresent(ExceptionHandlerComponent.class)
                || classFile.isAnnotationPresent(AutoInit.class)){

            Plugin_Temp.add(classFile);
        }
    }
}
