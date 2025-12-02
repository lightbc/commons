package com.lightbc.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件处理工具
 */
public class FileUtil {

    /**
     * 通过流的形式读取资源文件
     *
     * @param clazz        实例对象
     * @param resourcePath 资源路径
     * @return 资源文件内容
     */
    public static String readResource(Class<?> clazz, String resourcePath) {
        List<String> result = readResourceAsStream(clazz, resourcePath);
        StringBuilder builder = new StringBuilder();
        for (String s : result) {
            builder.append(s).append("\n");
        }
        return builder.toString();
    }

    /**
     * 通过流的形式读取资源文件
     *
     * @param clazz        实例对象
     * @param resourcePath 资源路径
     * @return 资源文件内容
     */
    public static List<String> readResourceAsStream(Class<?> clazz, String resourcePath) {
        InputStream is = clazz.getResourceAsStream(resourcePath);
        return readResourceAsStream(is);
    }

    /**
     * 通过流的形式读取资源文件
     *
     * @param is 资源文件流
     * @return 资源文件内容
     */
    public static List<String> readResourceAsStream(InputStream is) {
        List<String> lines = new ArrayList<>();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines;
        }
    }

    /**
     * 获取属性键值对
     *
     * @param content 属性文件内容
     * @return 属性键值对
     */
    public static Map<String, String> getProperties(List<String> content) {
        Map<String, String> map = new HashMap<>();
        if (content != null && content.size() > 0) {
            for (String c : content) {
                int splitStrIndex = c.indexOf("=");
                String key = c.substring(0, splitStrIndex);
                String value = c.substring(splitStrIndex + 1);
                map.put(key.trim(), value.trim());
            }
        }
        return map;
    }

}
