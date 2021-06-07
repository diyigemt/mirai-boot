package net.diyigemt.miraiboot.autoconfig;

import net.diyigemt.miraiboot.Main;
import net.diyigemt.miraiboot.annotation.*;
import net.diyigemt.miraiboot.core.PluginMgr;
import net.diyigemt.miraiboot.core.RegisterProcess;
import net.diyigemt.miraiboot.entity.PluginItem;
import net.diyigemt.miraiboot.mirai.MiraiMain;
import net.diyigemt.miraiboot.utils.FileUtil;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
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

    public static JarPluginLoader loader;

    private static boolean flag = false;

    private static boolean isUEFI  = true;

    private static List<PluginItem> pluginItems = new ArrayList<>();

    public static List<Class<?>> getPluginClasses(){
        MiraiMain.logger.info("开始扫描插件");
        boolean flag = false;
        try{
            FileUtil.init(Main.class);
            File[] plugins = FileUtil.getInstance().getPlugins();
            if(plugins.length == 0){
                MiraiMain.logger.warning("当前MiraiBoot没有加载任何插件");
                return classes;
            }
            for(File file : plugins){
                flag = LoadPlugin(file);
                if(flag) {
                    PluginMgr.addLoader(loader, file.getName());//成功的loader交给插件管理器保存
                    PluginMgr.addPluginManifest(file.getName(), pluginItems);//创建加载类清单
                    RegisterProcess.AnnotationScanner(classes);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * <h2>热加载插件指令</h2>
     * @param path 绝对/相对路径
     */
    public static void LoadPlugin(String path){
        try{
            File file = new File(path);
            boolean flag = LoadPlugin(file);
            if(!flag) return;
            PluginMgr.addLoader(loader, file.getName());//成功的loader交给插件管理器保存
            PluginMgr.addPluginManifest(file.getName(), pluginItems);//创建加载类清单
            RegisterProcess.AnnotationScanner(classes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void LoadPluginDependencies(List<JarFile> dependencies){
        for(JarFile file : dependencies){
            MiraiMain.logger.info("解析到自定义加载项，开始加载");
            Plugin_Temp.clear();//重新初始化
            flag = false;
            Enumeration<JarEntry> files = file.entries();
            GlobalJarScanner(files);
        }
    }

    private static boolean LoadPlugin(File file) throws Exception{
        String fileName = file.getName();
        if(fileName.endsWith(".jar")){
            URL url = new URL("jar:file:/" + file.getAbsolutePath() + "!/");
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            JarFile jar = connection.getJarFile();
            loader = new JarPluginLoader(new URL[]{url});
            Plugin_Temp.clear();//重新初始化
            flag = false;
            pluginItems = new ArrayList<>();
            Enumeration<JarEntry> files = jar.entries();
            Manifest manifest = jar.getManifest();
            String MainClass = manifest.getMainAttributes().getValue("Main-Class");
            if(MainClass == null){
                MiraiMain.logger.error(file.getName() + ": 该jar没有绑定主类，无法加载");
                return false;
            }
            String PackName = MainClass.substring(0, MainClass.lastIndexOf(".")).replace(".", "/");//获取主方法所在的包路径
            //反射onLoad方法
            Class<?> mainClass = loader.loadClass(MainClass);
            Object main = mainClass.newInstance();
            Method onLoad = null;
            try{
                onLoad = mainClass.getMethod("onLoad");
            }catch (NoSuchMethodException e){
                MiraiMain.logger.error(file.getName() + ": 未知的MiraiBoot插件格式");
                return false;
            }
            MiraiMain.logger.info("发现插件：" + file.getName());
            onLoad.invoke(main);

            if(flag){//在onLoad中加载其它包时存在失败，放弃该插件的加载。
                MiraiMain.logger.error("自定义加载项: " + file.getName() + ": 加载失败，缺少部分依赖，已放弃该插件所有加载");
                return false;
            }

            Field UEFI = mainClass.getField("UEFIMode");//获取UEFI设置
            int i = 0;
            UEFI.setAccessible(true);
            isUEFI = (boolean) UEFI.get(main);

            if(isUEFI){
                MiraiMain.logger.info("正在加载： " + fileName + "(UEFI)");
                QuickJarScanner(files, PackName);//UEFI（迫真）
                if(Plugin_Temp.size() == 0){//扫了一圈啥也没有
                    MiraiMain.logger.warning("插件：" + fileName + ": 未扫描到任何有效注解，正在尝试全包扫描。");
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
                return false;
            }
            if(Plugin_Temp.size() == 0){//还是啥也没有
                connection.getJarFile().close();
                MiraiMain.logger.error("插件: " + file.getName() + ": 该插件中未找到任何有效的类，加载失败");
                return false;
            }else {
                classes.addAll(Plugin_Temp);
                PluginMgr.addPluginConnection(connection);
                MiraiMain.logger.info("插件: " + file.getName() + "，加载成功");
            }
        }

        return true;
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
        }catch (ClassNotFoundException | NoClassDefFoundError e){
            MiraiMain.logger.error( className + ": 加载失败\n缺少依赖: " + e.getMessage().replace("/", "."));
            flag = true;
            return;
        }
        if(classFile.isAnnotationPresent(EventHandlerComponent.class)
                || classFile.isAnnotationPresent(ExceptionHandlerComponent.class)
                || classFile.isAnnotationPresent(AutoInit.class)){

            Plugin_Temp.add(classFile);

            if(classFile.isAnnotationPresent(AutoInit.class)) return;//对于AutoInit来说，他不需要再往下走了
//            Method onUnload = null;
//            try{
//                onUnload = classFile.getMethod("onUnload");
//            }catch (NoSuchMethodException ignore){}
//            if(onUnload != null){
//                PluginMgr.addUnloadController(onUnload);
//            }

            //记录
            String ClassName = classFile.getName();
            PluginItem pluginItem = new PluginItem();
            pluginItem.setClassName(ClassName.substring(ClassName.lastIndexOf(".") + 1));//类名
            pluginItem.setPackageName(classFile.getPackageName());//包名
            for(Method method : classFile.getMethods()){
                for(Annotation annotation : method.getAnnotations()){
                    if(annotation instanceof EventHandler){
                        pluginItem.setAnnotationData(annotation);//命令所在的注解
                    }
                    else if(annotation instanceof ExceptionHandler){
                        pluginItem.setAnnotationData(annotation);//命令所在的注解
                    }
                    else continue;//啥都不是

                    pluginItems.add(pluginItem);
                }
            }
        }
    }
}
