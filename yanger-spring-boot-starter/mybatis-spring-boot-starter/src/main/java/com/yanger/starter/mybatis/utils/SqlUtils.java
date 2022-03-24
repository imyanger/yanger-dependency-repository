package com.yanger.starter.mybatis.utils;

import com.yanger.tools.web.tools.AesKit;
import com.yanger.tools.web.tools.Base64Utils;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;

/**
 * sql 工具类
 * @Author yanger
 * @Date 2021/1/28 18:42
 */
@UtilityClass
@SuppressWarnings("all")
public class SqlUtils {

    /** SQL_FORMATTER */
    private static final SqlFormatter SQL_FORMATTER = new SqlFormatter();

    /** Sensitive key */
    private String SENSITIVE_KEY;

    /**
     * Set sensitive key
     * @param sensitiveKey sensitive key
     */
    public void setSensitiveKey(String sensitiveKey) {
        SqlUtils.SENSITIVE_KEY = sensitiveKey;
    }

    /**
     * Get encrypt filed
     * 敏感字段做查询条件需加密后查询
     * @param value value
     * @return the string
     */
    public String getEncryptFiled(String value) {
        if (StringUtils.isBlank(value) || StringUtils.isBlank(SqlUtils.SENSITIVE_KEY)) {
            return value;
        }
        byte[] encrypt = AesKit.encrypt(value, SqlUtils.SENSITIVE_KEY);
        return Base64Utils.encodeToString(encrypt);
    }

    /**
     * 格式 sql
     * @param boundSql bound sql
     * @param format   format
     * @return the string
     */
    @Contract("_, false -> param1")
    public static String sqlFormat(String boundSql, boolean format) {
        if (format) {
            try {
                return SQL_FORMATTER.format(boundSql);
            } catch (Exception ignored) {
            }
        }
        return boundSql;
    }

}
