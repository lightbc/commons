package com.lightbc.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDesensitization {

    // 敏感数据替换字符，默认*
    String replaceChar() default "*";

    // 开始下标
    int start() default 0;

    // 结束下标，默认到字符串结尾
    int end() default -1;

}
