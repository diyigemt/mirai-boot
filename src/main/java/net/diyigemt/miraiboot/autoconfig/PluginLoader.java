package net.diyigemt.miraiboot.autoconfig;

import net.diyigemt.miraiboot.annotation.AutoInit;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.ExceptionHandlerComponent;
import net.diyigemt.miraiboot.mirai.MiraiMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private static int CurrentListSize = 0;

    public static URLClassLoader loader;

    public static List<Class<?>> getPluginClasses(){
        MiraiMain.logger.info("开始扫描插件");
        List<Class<?>> classes = new ArrayList<>();
        try{
            File Directory = new File("data\\plugin");
            if(!Directory.isDirectory()){
                throw new FileNotFoundException("\"" + Directory.getAbsolutePath() + "\"" + "is not a file directory");
            }
            File[] plugins = Directory.listFiles();
            for(File file : plugins){
                String fileName = file.getName();
                if(fileName.endsWith(".jar")){
                    boolean flag = false;
                    MiraiMain.logger.info("正在加载： " + fileName);
                    JarFile jar = new JarFile(file.getAbsolutePath());
                    URL url = file.toURL();
                    loader = new URLClassLoader(new URL[]{url}, MiraiApplication.class.getClassLoader());
                    ClassLoader loader1 = loader.getParent();

                    Enumeration<JarEntry> files = jar.entries();
                    CurrentListSize = classes.size();
                    while (files.hasMoreElements()) {
                        JarEntry jarEntry = files.nextElement();
                        String name = jarEntry.getName();
                        if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")){
                            String className = name.substring(0, name.length() - 6).replace("/", ".");
                            if(className.equals("module-info") || className.startsWith("META-INF")) continue;//无意义的class文件
                            Class<?> classFile = null;
                            try{
                                classFile = loader.loadClass(className);
                            }catch (ClassNotFoundException | NoClassDefFoundError e){
                                MiraiMain.logger.error( className + ": 加载失败\n缺少依赖: " + e.getMessage().replace("/", "."));
                                flag = true;
                                continue;
                            }
                            if(classFile.isAnnotationPresent(EventHandlerComponent.class)
                                    || classFile.isAnnotationPresent(ExceptionHandlerComponent.class)
                                    || classFile.isAnnotationPresent(AutoInit.class)){

                                classes.add(classFile);
                            }
                        }
                    }

                    if(flag){
                        MiraiMain.logger.error("插件: " + file.getName() + ": 启动失败，缺少部分依赖");
                        classes.clear();
                        return classes;
                    }
                    if(CurrentListSize == classes.size()){
                        MiraiMain.logger.error("插件: " + file.getName() + ": 该插件中未找到任何有效的类，加载失败");
                    }else {
                        MiraiMain.logger.info("插件: " + file.getName() + "，加载成功");
                    }
                }
            }
        }catch (IOException e) { e.printStackTrace();}

        return classes;
    }
}
