package com.yanger.starter.mybatis.handler;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description id 转换器
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class SerializableIdTypeHandler extends BaseTypeHandler<Serializable> {

    /** Type */
    private final Class<?> type;

    /**
     * Serializable id type handler
     *
     * @param type type
     */
    @Contract("null -> fail")
    public SerializableIdTypeHandler(Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    /**
     * Sets non null parameter *
     *
     * @param ps        ps
     * @param i         the first parameter is 1, the second is 2, ...
     * @param parameter parameter
     * @param jdbcType  jdbc type
     * @throws SQLException sql exception
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Serializable parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setObject(i, this.getValue(parameter));
        } else {
            ps.setObject(i, this.getValue(parameter), jdbcType.TYPE_CODE);
        }
    }

    /**
     * 通过列表转换结果
     *
     * @param rs         rs
     * @param columnName column name
     * @return the nullable result
     * @throws SQLException sql exception
     */
    @Override
    public Serializable getNullableResult(@NotNull ResultSet rs, String columnName) throws SQLException {
        if (null == rs.getObject(columnName) && rs.wasNull()) {
            return null;
        }
        return this.valueOf(this.type, rs.getObject(columnName));
    }

    /**
     * 通过字段下标获取转换结果
     *
     * @param rs          rs
     * @param columnIndex column index
     * @return the nullable result
     * @throws SQLException sql exception
     */
    @Override
    public Serializable getNullableResult(@NotNull ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs.getObject(columnIndex) && rs.wasNull()) {
            return null;
        }
        return this.valueOf(this.type, rs.getObject(columnIndex));
    }

    /**
     * Gets nullable result *
     *
     * @param cs          cs
     * @param columnIndex column index
     * @return the nullable result
     * @throws SQLException sql exception
     */
    @Override
    public Serializable getNullableResult(@NotNull CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex) && cs.wasNull()) {
            return null;
        }
        return this.valueOf(this.type, cs.getObject(columnIndex));
    }

    /**
     * Value of
     *
     * @param idClass id class
     * @param value   value
     * @return the serializable
     */
    private Serializable valueOf(@NotNull Class<?> idClass, Object value) {
        if (String.class.isAssignableFrom(idClass)) {
            return (String) value;
        } else if (Integer.class.isAssignableFrom(idClass)) {
            return (Integer) value;
        } else if (Long.class.isAssignableFrom(idClass)) {
            return (Long) value;
        }
        throw new MybatisPlusException("暂不支持的 id 类型");
    }

    /**
     * Gets value *
     *
     * @param object object
     * @return the value
     */
    @Contract(value = "_ -> param1", pure = true)
    private Object getValue(Object object) {
        return object;
    }
}
