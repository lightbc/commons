package com.lightbc.commons.util;

import java.lang.reflect.Constructor;

/**
 * 反射工具
 */
public class ReflectionUtil {

    /**
     * 创建实例
     *
     * @param cls 实例类
     * @param <T> 实例类型
     * @return 实例对象
     */
    public static <T> T createInstance(Class<T> cls) {
        try {
            Constructor<T> constructor = cls.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建实例
     *
     * @param cls        实例类
     * @param paramTypes 参数类型
     * @param args       参数
     * @param <T>        实例类型
     * @return 实例对象
     */
    public static <T> T createInstance(Class<T> cls, Class<?>[] paramTypes, Object[] args) {
        try {
            Constructor<T> constructor = cls.getConstructor(paramTypes);
            return constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
