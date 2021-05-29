package net.diyigemt.miraiboot.autoconfig;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.ExceptionHandlerComponent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    public static List<Class<?>> getPluginClasses(){
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
                    URLClassLoader loader =  new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
                    Enumeration<JarEntry> files = jar.entries();
                    while (files.hasMoreElements()) {
                        JarEntry jarEntry = files.nextElement();
                        Class<?> classFile;
                        String name = jarEntry.getName();
                        if (name.matches("^.*class$")){
                            String className = name.substring(0, name.length() - 6).replace("/", ".");
                            classFile = loader.loadClass(className);
                            Package temp = classFile.getPackage();
                            Boolean res = temp.isAnnotationPresent(EventHandlerComponent.class);
                            int i = 0;
                            Annotation annotation = classFile.getAnnotation(EventHandlerComponent.class);
                            Method[] methods = classFile.getDeclaredMethods();
                            for(Method method : methods){
                                Annotation[] annotationa = method.getDeclaredAnnotations();
                            }
                            if(classFile.isAnnotationPresent(EventHandlerComponent.class) || classFile.isAnnotationPresent(ExceptionHandlerComponent.class)){
                                classes.add(classFile);
                            }
                        }
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException | NoClassDefFoundError e) { }//过滤掉一些无关紧要的错误

        return classes;
    }
}
