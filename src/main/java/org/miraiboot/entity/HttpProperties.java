package org.miraiboot.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <h2>自定义HTTP属性</h2>
 * <p>实例化后在Builder中作为参数传入</p>
 * <p>构造函数已为您添加：</p>
 * <p>&nbsp;&nbsp;最大请求时间：3000</p>
 * <p>&nbsp;&nbsp;请求模式：GET</p>
 * <p>&nbsp;&nbsp;引擎伪装</p>
 * <p>&nbsp;&nbsp;保持连接</p>
 * <p></p>
 * <p>如有需求，重写对应属性即可</p>
 * @author Haythem
 * @since 1.0.0
 */

public class HttpProperties {

    @Getter
    private Map<String, String> RequestProperties = new LinkedHashMap<>();

    @Getter
    @Setter
    private int Timeout = 3000;

    @Setter
    @Getter
    private String RequestMethod = "GET";

    /**
     * <h2>自定义HTTP属性</h2>
     * <p>实例化后在Builder中作为参数传入</p>
     * <p>构造函数已为您添加保持连接和引擎伪装,如有需求重写对应参数即可覆盖</p>
     */
    public HttpProperties(){
        RequestProperties.put("Connection", "keep-alive");
        RequestProperties.put("User-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.72 Safari/537.36");
    }

    /**
     * <h2>获取RequestProperties长度</h2>
     */
    public int getRequestPropertiesSize(){
        return RequestProperties.size();
    }

    /**
     * <h2>添加HTTP属性</h2>
     * <p>提供属性键值对</p>
     * <p>构造函数已为您添加保持连接和引擎伪装，如有需求重写对应参数即可覆盖</p>
     * @param key 键
     * @param value 值
     */
    public void setRequestProperties(String key, String value){
        if(RequestProperties.containsKey(key)){
            RequestProperties.replace(key, value);
        }else {
            RequestProperties.put(key, value);
        }
    }
}
