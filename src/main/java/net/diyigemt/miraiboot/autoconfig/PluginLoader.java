package net.diyigemt.miraiboot.autoconfig;

import net.diyigemt.miraiboot.annotation.AutoInit;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.ExceptionHandlerComponent;
import net.diyigemt.miraiboot.mirai.MiraiMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private static int CurrentListSize = 0;

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
                    JarFile jar = new JarFile(file.getAbsolutePath());
                    URL url = file.toURL();
                    URLClassLoader loader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
                    Enumeration<JarEntry> files = jar.entries();
                    CurrentListSize = classes.size();
                    while (files.hasMoreElements()) {
                        JarEntry jarEntry = files.nextElement();
                        String name = jarEntry.getName();
                        if (name.matches("^.*class$")){
                            String className = name.substring(0, name.length() - 6).replace("/", ".");
                            System.out.println(className);
                            Class<?> classFile = loader.loadClass(className);

                            if(classFile.isAnnotationPresent(EventHandlerComponent.class)
                                    || classFile.isAnnotationPresent(ExceptionHandlerComponent.class)
                                    || classFile.isAnnotationPresent(AutoInit.class)){

                                classes.add(classFile);
                            }
                        }
                    }
                    if(CurrentListSize == classes.size()){
                        MiraiMain.logger.error("插件: " + file.getName() + ": 该插件中未找到任何有效的类，加载失败");
                    }else {
                        MiraiMain.logger.info("插件: " + file.getName() + "，加载成功");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        } catch (ClassNotFoundException | NoClassDefFoundError ignored) { }//过滤掉一些无关紧要的错误

        return classes;
    }
}
