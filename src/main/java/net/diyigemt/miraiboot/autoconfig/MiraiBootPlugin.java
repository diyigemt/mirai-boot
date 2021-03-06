package net.diyigemt.miraiboot.autoconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * <h2>插件类</h2>
 * <P>在主类上继承该类，输出的JAR即为插件</P>
 * @author Haythem
 */
public class MiraiBootPlugin {

    private List<JarFile> PluginDependencies = new ArrayList<>();

    /**
     * <h2>UEFI模式</h2>
     * <p>仅加载主类所在的包，其余的包均不加载，大幅提升加载速度</p>
     * <p>（前提是将必须的依赖打入插件包中）</p>
     */
    public boolean UEFIMode = true;

    /**
     * <h2>插件被加载前执行的方法</h2>
     */
    public void onLoad(){}

    /**
     * <h2>单独加载依赖</h2>
     * <p>在onLoad方法中使用</p>
     * <p>注:此方法并不是UEFI启动</p>
     */
    protected void addDependencies(List<JarFile> file){
        PluginDependencies.addAll(file);
        PluginLoader.LoadPluginDependencies(PluginDependencies);
    }
}
