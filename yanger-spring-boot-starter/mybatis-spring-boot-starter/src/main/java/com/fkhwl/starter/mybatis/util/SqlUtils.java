package com.fkhwl.starter.mybatis.util;

import com.fkhwl.starter.core.util.AesUtils;
import com.fkhwl.starter.core.util.Base64Utils;
import com.fkhwl.starter.core.util.StringUtils;

import org.jetbrains.annotations.Contract;

import lombok.experimental.UtilityClass;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.17 18:09
 * @since 1.0.0
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
     *
     * @param sensitiveKey sensitive key
     * @since 1.6.0
     */
    public void setSensitiveKey(String sensitiveKey) {
        SqlUtils.SENSITIVE_KEY = sensitiveKey;
    }

    /**
     * Get encrypt filed
     * 敏感字段做查询条件需加密后查询
     *
     * @param value value
     * @return the string
     * @since 1.6.0
     */
    public String getEncryptFiled(String value) {
        if (StringUtils.isBlank(value) || StringUtils.isBlank(SqlUtils.SENSITIVE_KEY)) {
            return value;
        }
        byte[] encrypt = AesUtils.encrypt(value, SqlUtils.SENSITIVE_KEY);
        return Base64Utils.encodeToString(encrypt);
    }

    /**
     * 格式sql
     *
     * @param boundSql bound sql
     * @param format   format
     * @return the string
     * @since 1.0.0
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
