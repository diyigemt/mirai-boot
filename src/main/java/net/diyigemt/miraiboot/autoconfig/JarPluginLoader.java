package net.diyigemt.miraiboot.autoconfig;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <h2>插件ClassLoader</h2>
 * <p>每个插件都有一个该ClassLoader实例</p>
 * @author Haythem
 */
public class JarPluginLoader extends URLClassLoader {

    public JarPluginLoader(URL[] urls){
        super(urls);
    }

    public JarPluginLoader(ClassLoader parent){
        super(new URL[0], parent);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
