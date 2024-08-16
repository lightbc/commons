package com.lightbc.commons.aspect;

import com.lightbc.commons.annotations.FieldDesensitization;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Aspect
@Component
public class FieldDesensitizationAspect {

    @Around("@annotation(com.lightbc.commons.annotations.DesensitizationInterface)")
    public Object fieldDesensitization(ProceedingJoinPoint point) throws Throwable {
        // 响应内容
        Object response = point.proceed();
        if (response instanceof List) {
            multipleData(response);
        } else {
            singleData(response);
        }
        return response;
    }

    /**
     * 处理单条数据
     *
     * @param response 单条数据
     * @throws IllegalAccessException
     */
    private void singleData(Object response) throws IllegalAccessException {
        if (response != null) {
            // 获取响应对象的字段
            Field[] fields = response.getClass().getDeclaredFields();
            if (fields.length > 0) {
                for (Field field : fields) {
                    // 判断字段上是否包含字段脱敏的注解
                    if (field.isAnnotationPresent(FieldDesensitization.class)) {
                        // 获取注解里的参数值
                        FieldDesensitization fd = field.getAnnotation(FieldDesensitization.class);
                        // 替换字符
                        String rc = fd.replaceChar();
                        // 脱敏开始位置下标
                        int start = fd.start();
                        // 脱敏结束位置下标
                        int end = fd.end();
                        // 设置私有属性可访问
                        field.setAccessible(true);
                        // 获取脱敏注解注解的字段值
                        Object fv = field.get(response);
                        // 只处理string类型的字段
                        if (fv instanceof String) {
                            String sfv = String.valueOf(fv);
                            String nsf = sfv;
                            // 结束位置下标需要大于开始位置下标
                            if (end > start) {
                                // 当结束位置下标大于字符串的长度时，脱敏开始位置到字符串结尾位置
                                if (end > sfv.length()) {
                                    end = sfv.length();
                                }
                                // 将脱敏位置使用替换字符替换掉
                                nsf = sfv.substring(0, start) + sfv.substring(start, end).replaceAll(".", rc) + sfv.substring(end);
                            }
                            // 当开始位置下标大于0，结束位置下标为-1时，表明从开始位置下标到字符串结尾都脱敏处理
                            if (start > 0 && end == -1) {
                                // 将脱敏位置使用替换字符替换掉
                                nsf = sfv.substring(0, start) + sfv.substring(start).replaceAll(".", rc);
                            }
                            // 将脱敏后的内容再赋值给原字段
                            field.set(response, nsf);
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理多条数据
     *
     * @param response 多条数据
     * @throws IllegalAccessException
     */
    private void multipleData(Object response) throws IllegalAccessException {
        if (response != null) {
            List<Object> list = (List<Object>) response;
            if (list.size() > 0) {
                for (Object object : list) {
                    singleData(object);
                }
            }
        }
    }

}
