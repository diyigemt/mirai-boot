package org.miraiboot.constant;

import java.util.*;

/**
 * 所有功能都需要在这注册ID号，依次顺延
 * 管理员功能使用负数ID
 * 对外开放功能使用正数ID
 * 极值±127（Tinyint），需要扩充请去修改permission表属性，记得注释也更新一下
 * (不用尝试枚举了，注解里不会生效的233)
 * @author Haythem
 */

public class FunctionId {

    //用来查询ID
    public static final Map<String, Integer> map = new HashMap<>(){{
        //对外开放功能
        put("reply", reply);

        //管理员功能
        put("permit", permit);
    }};

    /**
     *
     * @param target
     * 提供原始命令
     * @return 返回ID值
     */
    public static int getMap(String target){
        return map.get(target);
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

    /**
     * 给注解用的
     */

    //对外开放功能
    public static final int reply = 1;

    //管理员功能
    public static final int permit = -1;
}
