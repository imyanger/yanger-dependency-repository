package com.yanger.tools.web.tools;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 对象工具类
 * @Author yanger
 * @Date 2020/12/22 14:10
 */
@UtilityClass
@SuppressWarnings("checkstyle:ReturnCount")
public class ObjectUtils extends org.springframework.util.ObjectUtils {

    /**
     * 判断对象为null
     * @param object 对象
     * @return 对象是否为空 boolean
     */
    @Contract(value = "!null -> false; null -> true", pure = true)
    public static boolean isNull(@Nullable Object object) {
        return Objects.isNull(object);
    }

    /**
     * 判断对象不为null
     * @param object 对象
     * @return 对象是否不为空 boolean
     */
    @Contract(value = "!null -> true; null -> false", pure = true)
    public static boolean isNotNull(@Nullable Object object) {
        return Objects.nonNull(object);
    }

    /**
     * 判断对象为true
     * @param object 对象
     * @return 对象是否为true boolean
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean isTrue(@Nullable Boolean object) {
        return Boolean.TRUE.equals(object);
    }

    /**
     * 判断对象为false
     * @param object 对象
     * @return 对象是否为false boolean
     */
    @Contract(value = "null -> true", pure = true)
    public static boolean isFalse(@Nullable Boolean object) {
        return object == null || Boolean.FALSE.equals(object);
    }

    /**
     * 判断数组不为空
     * @param array 数组
     * @return 数组是否为空 boolean
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return !ObjectUtils.isEmpty(array);
    }

    /**
     * 判断对象不为空
     * @param obj 数组
     * @return 数组是否为空 boolean
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtils.isEmpty(obj);
    }

    /**
     * 对象 eq
     * @param o1 Object
     * @param o2 Object
     * @return 是否eq boolean
     */
    @Contract(value = "null, !null -> false; !null, null -> false", pure = true)
    public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
        return Objects.equals(o1, o2);
    }

    /**
     * 比较两个对象是否不相等. <br>
     * @param o1 对象1
     * @param o2 对象2
     * @return 是否不eq boolean
     */
    @Contract(value = "null, !null -> true; !null, null -> true", pure = true)
    public static boolean isNotEqual(Object o1, Object o2) {
        return !Objects.equals(o1, o2);
    }

    /**
     * 返回对象的 hashCode
     * @param obj Object
     * @return hashCode int
     */
    public static int hashCode(@Nullable Object obj) {
        return Objects.hashCode(obj);
    }

    /**
     * 如果对象为null,返回默认值
     * @param object       Object
     * @param defaultValue 默认值
     * @return Object object
     */
    @Contract(value = "!null, _ -> param1; null, _ -> param2", pure = true)
    public static Object defaultIfNull(@Nullable Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * 强转string
     * @param object Object
     * @return String string
     */
    @Nullable
    public static String toStr(@Nullable Object object) {
        return toStr(object, null);
    }

    /**
     * 强转string
     * @param object       Object
     * @param defaultValue 默认值
     * @return String string
     */
    @Contract("null, _ -> param2")
    @Nullable
    public static String toStr(@Nullable Object object, @Nullable String defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).toString();
        }
        return String.valueOf(object);
    }

    /**
     * 对象转为 int  (支持 String 和 Number) ,默认: 0
     * @param object Object
     * @return int int
     */
    public static int toInt(@Nullable Object object) {
        return toInt(object, 0);
    }

    /**
     * 对象转为 int  (支持 String 和 Number)
     * @param object       Object
     * @param defaultValue 默认值
     * @return int int
     */
    @Contract("null, _ -> param2")
    public static int toInt(@Nullable Object object, int defaultValue) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }
        if (object instanceof CharSequence) {
            String value = ((CharSequence) object).toString();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 对象转为 long  (支持 String 和 Number) ,默认: 0L
     * @param object Object
     * @return long long
     */
    public static long toLong(@Nullable Object object) {
        return toLong(object, 0L);
    }

    /**
     * 对象转为 long  (支持 String 和 Number) ,默认: 0L
     * @param object       Object
     * @param defaultValue the default value
     * @return long long
     */
    @Contract("null, _ -> param2")
    public static long toLong(@Nullable Object object, long defaultValue) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }
        if (object instanceof CharSequence) {
            String value = ((CharSequence) object).toString();
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 对象转为 Float
     * @param object Object
     * @return 结果 float
     */
    public static float toFloat(@Nullable Object object) {
        return toFloat(object, 0.0f);
    }

    /**
     * 对象转为 Float
     * @param object       Object
     * @param defaultValue float
     * @return 结果 float
     */
    @Contract("null, _ -> param2")
    public static float toFloat(@Nullable Object object, float defaultValue) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }
        if (object instanceof CharSequence) {
            String value = ((CharSequence) object).toString();
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 对象转为 Double
     * @param object Object
     * @return 结果 double
     */
    public static double toDouble(@Nullable Object object) {
        return toDouble(object, 0.0d);
    }

    /**
     * 对象转为 Double
     * @param object       Object
     * @param defaultValue double
     * @return 结果 double
     */
    @Contract("null, _ -> param2")
    public static double toDouble(@Nullable Object object, double defaultValue) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        if (object instanceof CharSequence) {
            String value = ((CharSequence) object).toString();
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 对象转为 Byte
     * @param object Object
     * @return 结果 byte
     */
    public static byte toByte(@Nullable Object object) {
        return toByte(object, (byte) 0);
    }

    /**
     * 对象转为 Byte
     * @param object       Object
     * @param defaultValue byte
     * @return 结果 byte
     */
    @Contract("null, _ -> param2")
    public static byte toByte(@Nullable Object object, byte defaultValue) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }
        if (object instanceof CharSequence) {
            String value = ((CharSequence) object).toString();
            try {
                return Byte.parseByte(value);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 对象转为 Short
     * @param object Object
     * @return 结果 short
     */
    public static short toShort(@Nullable Object object) {
        return toShort(object, (short) 0);
    }

    /**
     * 对象转为 Short
     * @param object       Object
     * @param defaultValue short
     * @return 结果 short
     */
    @Contract("null, _ -> param2")
    public static short toShort(@Nullable Object object, short defaultValue) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }
        if (object instanceof CharSequence) {
            String value = ((CharSequence) object).toString();
            try {
                return Short.parseShort(value);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 对象转为 Boolean
     * @param object Object
     * @return 结果 boolean
     */
    @Nullable
    public static Boolean toBoolean(@Nullable Object object) {
        return toBoolean(object, null);
    }

    /**
     * 对象转为 Boolean
     * @param object       Object
     * @param defaultValue 默认值
     * @return 结果 boolean
     */
    @Contract("null, _ -> param2")
    @Nullable
    public static Boolean toBoolean(@Nullable Object object, @Nullable Boolean defaultValue) {
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        if (object instanceof CharSequence) {
            String value = ((CharSequence) object).toString();
            try {
                return Boolean.parseBoolean(value.trim());
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

}
