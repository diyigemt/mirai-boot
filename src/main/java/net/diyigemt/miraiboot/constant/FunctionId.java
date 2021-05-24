package net.diyigemt.miraiboot.constant;

import java.util.*;

/**
 * <h2>管理EventHandler在权限管理中对应的id</h2>
 * 所有功能都需要在这注册ID号，依次顺延<br/>
 * 建用 管理员功能使用负数ID
 * 对外开放功能使用正数ID<br/>
 * 极值±127（Tinyint），需要扩充请去修改permission表属性，记得注释也更新一下<br/>
 * (不用尝试枚举了，注解里不会生效的233)<br/>
 * ⬆大概是指注解里不能用枚举?
 * @author Haythem723
 * @author diyigemt
 */

public class FunctionId {

    //用来查询ID
    public static final Map<String, Integer> map = new HashMap<>();

    public static void put(String key, int value) {
        map.put(key, value);
    }

    /**
     *
     * @param target
     * 提供原始命令
     * @return 返回ID值
     */
    public static int getMap(String target){
        Integer integer = map.get(target);
        return integer == null ? 0 : integer;
    }

    public static String getKey(int value){
        Set set=map.entrySet();
        Iterator it=set.iterator();
        while(it.hasNext()) {
            Map.Entry entry=(Map.Entry)it.next();
            if(entry.getValue().equals(value)) {
                return entry.getKey().toString();
            }
        }
        return null;
    }

    public static void registerAlias(String target, String alias) {
        Integer permissionIndex = getMap(target);
        if (permissionIndex == null) return;
        map.put(alias, permissionIndex);
    }

    // 默认permissionIndex
    public static final int DEFAULT_INDEX = 0;

    /**
     * 给注解用的
     */

    //对外开放功能
    public static final int reply = 1;

    //管理员功能
    public static final int permit = -1;
}
