package net.diyigemt.miraiboot.autoconfig;

import net.diyigemt.miraiboot.Main;
import net.diyigemt.miraiboot.annotation.AutoInit;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.ExceptionHandlerComponent;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.FileUtil;

import java.io.File;
import java.lang.reflect.Method;
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

    private static int CurrentListSize = 0;

    private static List<Class<?>> classes = new ArrayList<>();

    public static URLClassLoader loader;

    private static boolean flag = false;

    public static List<Class<?>> getPluginClasses(){
        MiraiMain.logger.info("开始扫描插件");
        try{
            FileUtil.init(Main.class);
            File[] plugins = FileUtil.getInstance().getPlugins();
            for(File file : plugins){
                String fileName = file.getName();
                if(fileName.endsWith(".jar")){
                    MiraiMain.logger.info("正在加载： " + fileName);
                    JarFile jar = new JarFile(file.getAbsolutePath());
                    URL url = file.toURL();
                    loader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
                    CurrentListSize = classes.size();
                    Manifest manifest = jar.getManifest();
                    String MainClass = manifest.getMainAttributes().getValue("Main-Class");
                    String PackName = MainClass.substring(0, MainClass.lastIndexOf(".")).replace(".", "/");//获取主方法所在的包路径
                    //反射onLoad方法
                    Class<?> mainClass = loader.loadClass(MainClass);
                    Method onLoad = null;
                    try{
                        onLoad = mainClass.getMethod("onLoad");
                    }catch (NoSuchMethodException e){
                        MiraiMain.logger.error("插件: " + file.getName() + ": 为非法插件");
                        continue;
                    }
                    onLoad.invoke(mainClass.newInstance());

                    Enumeration<JarEntry> files = jar.entries();

                    QuickJarScanner(files, PackName);//UEFI（迫真）
                    if(CurrentListSize == classes.size()){//扫了一圈啥也没有
                        GlobalJarScanner(files);//LEGACY（传统）
                    }

                    if(flag){//加载过程中存在失败
                        MiraiMain.logger.error("插件: " + file.getName() + ": 启动失败，缺少部分依赖");
                        classes.clear();
                        return classes;
                    }
                    if(CurrentListSize == classes.size()){//还是啥也没有
                        MiraiMain.logger.error("插件: " + file.getName() + ": 该插件中未找到任何有效的类，加载失败");
                    }else {
                        MiraiMain.logger.info("插件: " + file.getName() + "，加载成功");
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
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

            classes.add(classFile);
        }
    }
}
