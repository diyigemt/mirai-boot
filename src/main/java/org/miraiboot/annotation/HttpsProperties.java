package org.miraiboot.annotation;

import java.lang.annotation.*;

/**
 * <h2>自定义HTTP属性</h2>
 * <p>与SendMessageUtils配套使用，在引用SendMessage工具类的方法上使用</p>
 * <p></p>
 * <p>int Timeout 超时时间，默认3000</p>
 * <p></p>
 * <p>String RequestMethod 请求模式，默认为GET</p>
 * <p></p>
 * <p>String[] RequestProperties 请求属性</p>
 * @author Haythem
 * @since 1.0.0
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpsProperties {

    int Timeout() default 3000;

    String RequestMethod() default "GET";

    String[] RequestProperties() default {
            "Connection", "keep-alive",
            "User-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.72 Safari/537.36",
    };
}
