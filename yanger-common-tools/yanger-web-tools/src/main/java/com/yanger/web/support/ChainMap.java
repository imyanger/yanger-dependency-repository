package com.yanger.web.support;

import com.google.common.primitives.Ints;

import com.yanger.general.format.StringFormatter;
import com.yanger.web.tools.NumberUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Description 链式 map (linkedmap) key 必须为 string
 * @Author yanger
 * @Date 2020/12/22 10:30
 */
public final class ChainMap extends LinkedCaseInsensitiveMap<Object> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3992810369797036508L;

    /**
     * Chain map
     */
    private ChainMap() {
        super();
    }

    /**
     * Chain map
     *
     * @param size size
     */
    private ChainMap(int size) {
        super(capacity(size));
    }

    /**
     * 初始化 map 容量
     *
     * @param expectedSize the expected size
     * @return the int
     */
    @Contract(pure = true)
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    private static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            if (expectedSize < 0) {
                throw new IllegalArgumentException("expectedSize cannot be negative but was: " + expectedSize);
            }
            return expectedSize + 1;
        }
        if (expectedSize < Ints.MAX_POWER_OF_TWO) {
            // This is the calculation used in JDK8 to resize when a putAll
            // happens; it seems to be the most conservative calculation we
            // can make.  0.75 is the default load factor.
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }
        // any large value
        return Integer.MAX_VALUE;
    }

    /**
     * Build chain map.
     *
     * @return the chain map
     */
    @NotNull
    @Contract(" -> new")
    public static ChainMap build() {
        return new ChainMap();
    }

    /**
     * Build chain map.
     *
     * @param size the size
     * @return the chain map
     */
    @NotNull
    @Contract("_ -> new")
    public static ChainMap build(int size) {
        return new ChainMap(size);
    }

    /**
     * 设置列,当键或值为null时忽略
     *
     * @param attr  属性
     * @param value 值
     * @return 本身 ignore null
     */
    @Contract("null, _ -> this; !null, null -> this")
    public ChainMap setIgnoreNull(String attr, Object value) {
        if (null != attr && null != value) {
            put(attr, value);
        }
        return this;
    }

    /**
     * 存入 kv
     *
     * @param attr  属性
     * @param value 值
     * @return 本身 kv
     */
    @Contract("_, _ -> this")
    @NotNull
    @Override
    public ChainMap put(@NotNull String attr, Object value) {
        super.put(attr, value);
        return this;
    }

    /**
     * Clone chain map
     *
     * @return the chain map
     */
    @NotNull
    @Override
    public ChainMap clone() {
        return (ChainMap) super.clone();
    }

    /**
     * Gets obj.
     *
     * @param key the key
     * @return the obj
     */
    public Object getObj(String key) {
        return super.get(key);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 str
     */
    public String getStr(String attr) {
        return StringFormatter.toStr(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 int
     */
    @NotNull
    public Integer getInt(String attr) {
        return NumberUtils.toInt(attr, -1);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 long
     */
    @NotNull
    public Long getLong(String attr) {
        return NumberUtils.toLong(attr, -1L);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 float
     */
    public Float getFloat(String attr) {
        return NumberUtils.toFloat(attr, null);
    }

    /**
     * Gets double.
     *
     * @param attr the attr
     * @return the double
     */
    public Double getDouble(String attr) {
        return NumberUtils.toDouble(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 bool
     */
    public Boolean getBool(String attr) {
        if (attr != null) {
            String val = String.valueOf(attr);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        }
        return null;
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 byte [ ]
     */
    public byte[] getBytes(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param <T>          值类型
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值 t
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String attr, T defaultValue) {
        Object result = get(attr);
        return (T) (result != null ? result : defaultValue);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 date
     */
    public Date getDate(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 time
     */
    public Time getTime(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 timestamp
     */
    public Timestamp getTimestamp(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 number
     */
    public Number getNumber(String attr) {
        return get(attr, null);
    }

}
