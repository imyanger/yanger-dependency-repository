package com.yanger.generator.util;

/**
 * string tool
 */
public class StrUtils {

    /**
     * @Description 首字母大写
     * @author yanger
     * @date 2020/7/17
     * @param str
     * @return java.lang.String
     */
    public static String upperCaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * @Description 首字母小写
     * @author yanger
     * @date 2020/7/17
     * @param str
     * @return java.lang.String
     */
    public static String lowerCaseFirst(String str) {
        return (str != null && str.length() > 1) ? str.substring(0, 1).toLowerCase() + str.substring(1) : "";
    }

    /**
     * @Description 下划线转驼峰
     * @author yanger
     * @date 2020/7/17
     * @param underscoreName
     * @return java.lang.String
     */
    public static String underlineToCamelCase(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.trim().length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * @Description 驼峰转下划线
     * @author yanger
     * @date 2020/7/17
     * @param para
     * @return java.lang.String
     */
    public static String camelCaseToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 1; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

}
