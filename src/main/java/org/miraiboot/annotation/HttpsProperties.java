package org.miraiboot.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
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
