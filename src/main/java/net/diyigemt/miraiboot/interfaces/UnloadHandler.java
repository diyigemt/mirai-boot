package net.diyigemt.miraiboot.interfaces;

import net.diyigemt.miraiboot.entity.PluginItem;

import java.util.List;

/**
 * <h2>插件卸载接口</h2>
 * @author Haythem
 */
public interface UnloadHandler {
    /**
     * <h2>当插件被卸载时调用的方法</h2>
     * @param pluginItems PluginMgr提供的类清单
     */
    void onUnload(List<PluginItem> pluginItems);
}
