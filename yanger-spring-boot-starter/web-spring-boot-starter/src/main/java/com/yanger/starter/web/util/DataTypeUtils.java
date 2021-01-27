package com.yanger.starter.web.util;

import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.tools.general.format.StringFormatter;
import com.yanger.tools.general.tools.DateUtils;
import com.yanger.tools.web.exception.BasicException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import lombok.experimental.UtilityClass;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/1/27 18:42
 */
@UtilityClass
@SuppressWarnings(value = {"checkstyle:FileLength", "checkstyle:ReturnCount"})
public class DataTypeUtils {

    /**
     * 把输入数据转换为预期的输出类型
     * 转换数据类型通常都是子类转换为父类或者转换为接口,这种情况至少占到程序编码90%以上,此方法内部默认此种判断为第一级判断,从而保证效率最优
     * 此工具类的其他代码专注于解决另外的10%复杂情况,旨在把常见的容错代码,封装于此,避免调用端编写复杂、重复、冗长、不健壮的代码
     * <h1>容错概述: </h1>
     * 1.自动拆箱装箱
     * 2.数字与字符串向枚举转换
     * 3.数组与Collection自动互转
     * 4.除boolean外的七种基本数据类型互转时取值范围自适应
     * 5.字符串与时间与基本数据类型互转
     * 6.以及对上述容错情况自由排列组合后所产生的更复杂情况自动容错
     * 7.请格外注意,由于Boolean在Java中的通俗约定较之其他语言例如C非常不同,不存在其他数据类型与之兼容取值范围的习惯做法,因此并未容错,如果想要让数字类型转换为boolean,
     * 注意这里是未作支持的,现仅以最低兼容方式构造Boolean,即仅支持Java官方约定的通过字符串构造Boolean对象以及boolean基本值构造Boolean对象
     * <h1>常见情景: </h1>
     * 1.比如一个int数组完全可以放入long数组或 List 中, 用此工具方法自动处理,即可避免自己的代码篇幅冗长,或者代码编写质量不高导致程序健壮性不高的问题
     * 2.如果long数组数值并不是大数,当然也可以放入int数组,其他数值类型同理,反之亦然
     * 3.很多数据拿到的时候是Object类型,如果用强制类型转换,很容易出错,比如到底Object是数组还是集合类,都有可能,这种时候强制类型转换必然出错
     * 4.实现类自动转换为接口,比如HashMap、LinkedHashMap转换为Map
     * <h1>示例: </h1>
     * log.info("{}", convert(byte[].class, "-128"));
     * log.info("{}", convert(byte.class, "-128"));
     * log.info("{}", convert(byte[].class, -128));
     * log.info("{}", convert(byte.class, -128));
     * log.info("{}", convert(Byte[].class, "-128"));
     * log.info("{}", convert(Byte.class, "-128"));
     * log.info("{}", convert(Byte[].class, -128));
     * log.info("{}", convert(Byte.class, -128));
     * log.info("{}", convert(byte[].class, "-128.00000"));
     * log.info("{}", convert(byte.class, "-128.00000"));
     * log.info("{}", convert(byte[].class, -128.00000));
     * log.info("{}", convert(byte.class, -128.00000));
     * log.info("{}", convert(Byte[].class, "-128.00000"));
     * log.info("{}", convert(Byte.class, "-128.00000"));
     * log.info("{}", convert(Byte[].class, -128.00000));
     * log.info("{}", convert(Byte.class, -128.00000));
     * log.info("{}", convert(float[].class, "-128.00000"));
     * log.info("{}", convert(float.class, "-128.00000"));
     * log.info("{}", convert(float[].class, -128.00000));
     * log.info("{}", convert(float.class, -128.00000));
     * log.info("{}", convert(Float[].class, "-128.00000"));
     * log.info("{}", convert(Float.class, "-128.00000"));
     * log.info("{}", convert(Float[].class, -128.00000));
     * log.info("{}", convert(Float.class, -128.00000));
     * log.info("{}", convert(BigDecimal.class, "-128"));
     * log.info("{}", convert(BigDecimal.class, -128));
     * log.info("{}", convert(BigDecimal.class, "-128.00000"));
     * log.info("{}", convert(BigDecimal.class, -128.00000));
     * log.info("{}", convert(Date.class, "0"));
     * log.info("{}", convert(Date.class, "1451577600000"));
     * log.info("{}", convert(Date[].class, new Object[]{"0","2016-01-01 00:00:00","1451577600000",1451577600000L}));
     * log.info("{}", convert(Date[].class, Arrays.asList(new Object[]{"0","2016-01-01 00:00:00","1451577600000",1451577600000L})));
     * log.info("{}", convert(List.class, new Date[]{new Date(0),new Date(1451577600000L)}));
     * log.info("{}", convert(List.class, Arrays.asList(new Object[]{(byte)1,"1",1,1L})));
     * log.info("{}", convert(Set.class, Arrays.asList(new Object[]{(byte)1,"1",1,1L})));
     * log.info("{}", convert(Set.class, Arrays.asList(new Long[]{1L,1L,2L})));
     * log.info("{}", convert(Set.class, new Long[]{1L,1L,2L}));
     * log.info("{}", convert(BigDecimal[].class, new Object[]{(byte)1,1,1L,"1"}));
     *
     * @param <T>       the type parameter
     * @param destClazz 预期将要输出为何种数据类型
     * @param srcObj    输入任意类型的数据
     * @return the t
     */
    @Contract("_, null -> null; null, !null -> null")
    @SuppressWarnings("unchecked")
    public static <T> T convert(Class<T> destClazz, Object srcObj) {
        if (null == srcObj) {
            return null;
        }
        if (null == destClazz) {
            return null;
        }
        if (destClazz.isAssignableFrom(srcObj.getClass())) {
            return (T) srcObj;
        }

        if (isPrimitive(destClazz)) {
            Class<?> boxingType = typeBoxing(destClazz);
            return (T) unBoxing(convert(boxingType, srcObj));
        } else if (destClazz.isArray()) {
            return convertArray(destClazz, srcObj);
        } else if (destClazz.isEnum()) {
            return Object2Enum.convert(destClazz, srcObj);
        } else {
            T ret = null;
            if (String.class.isAssignableFrom(destClazz)) {
                ret = (T) Object2String.convert(srcObj);
            } else if (isNumber(destClazz)) {
                if (Long.class.isAssignableFrom(destClazz)) {
                    ret = (T) Object2Long.convert(srcObj);
                } else if (Integer.class.isAssignableFrom(destClazz)) {
                    ret = (T) Object2Integer.convert(srcObj);
                } else if (BigDecimal.class.isAssignableFrom(destClazz)) {
                    ret = (T) Object2BigDecimal.convert(srcObj);
                } else if (Double.class.isAssignableFrom(destClazz)) {
                    ret = (T) Object2Double.convert(srcObj);
                } else if (Float.class.isAssignableFrom(destClazz)) {
                    ret = (T) Object2Float.convert(srcObj);
                } else if (Short.class.isAssignableFrom(destClazz)) {
                    ret = (T) Object2Short.convert(srcObj);
                } else if (Byte.class.isAssignableFrom(destClazz)) {
                    ret = (T) Object2Byte.convert(srcObj);
                }
            } else if (Date.class.isAssignableFrom(destClazz)) {
                ret = (T) Object2Date.convert(srcObj);
            } else if (Boolean.class.isAssignableFrom(destClazz)) {
                ret = (T) Object2Boolean.convert(srcObj);
            } else if (Character.class.isAssignableFrom(destClazz)) {
                ret = (T) Object2Character.convert(srcObj);
            } else if (Collection.class.isAssignableFrom(destClazz)) {
                return (T) toCollection(destClazz, srcObj);
            } else {
                throw new BasicException("指定的数据类型 [{}] 暂不支持,无法转换", destClazz);
            }
            if (null == ret) {
                throw new BasicException("[{}] 无法转换为 [{}] Data: [{}]", srcObj.getClass(), destClazz.getName(), srcObj);
            } else {
                return ret;
            }
        }
    }

    /**
     * To collection collection
     *
     * @param <T>       parameter
     * @param destClazz dest clazz
     * @param srcObj    src obj
     * @return the collection
     */
    @SuppressWarnings("unchecked")
    private static <T> Collection<Object> toCollection(@NotNull Class<T> destClazz, Object srcObj) {
        Collection<Object> coll;
        if (destClazz.isInterface()) {
            if (List.class.isAssignableFrom(destClazz)) {
                coll = new ArrayList<>();
            } else if (Set.class.isAssignableFrom(destClazz)) {
                coll = new HashSet<>();
            } else if (Queue.class.isAssignableFrom(destClazz)) {
                coll = new LinkedList<>();
            } else {
                throw new BasicException("暂不支持向集合类型 {} 转换", destClazz.getName());
            }
        } else {
            try {
                coll = (Collection<Object>) destClazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new BasicException("无法对类型 {} 进行实例化", destClazz.getName());
            }
        }
        Class<?> objClazz = srcObj.getClass();
        if (objClazz.isArray()) {
            coll.addAll(Arrays.asList((Object[]) boxing(srcObj)));
        } else if (srcObj instanceof Collection) {
            coll.addAll((Collection<?>) srcObj);
        } else {
            coll.add(srcObj);
        }
        return coll;
    }

    /**
     * Convert array t
     *
     * @param <T>       parameter
     * @param destClazz dest clazz
     * @param srcObj    src obj
     * @return the t
     */
    @SuppressWarnings("unchecked")
    private static <T> T convertArray(Class<T> destClazz, @NotNull Object srcObj) {
        Object[] targetObjectArray;
        Class<?> objClazz = srcObj.getClass();
        if (objClazz.isArray()) {
            targetObjectArray = (Object[]) boxing(srcObj);
        } else if (Collection.class.isAssignableFrom(objClazz)) {
            targetObjectArray = ((Collection<?>) srcObj).toArray();
        } else {
            targetObjectArray = new Object[] {srcObj};
        }
        Class<?> everyClazz = destClazz.getComponentType();
        Object[] newOjb = (Object[]) Array.newInstance(everyClazz, targetObjectArray.length);
        for (int i = 0; i < targetObjectArray.length; i++) {
            newOjb[i] = convert(everyClazz, targetObjectArray[i]);
        }
        return (T) newOjb;
    }

    /**
     * 判断输入数据是否为null 数组与集合类内的每个元素都为null那么输入数据被视作null
     *
     * @param obj the obj
     * @return the boolean
     */
    @Contract("null -> true")
    public static boolean deepIsNull(Object obj) {
        if (null == obj) {
            return true;
        }
        Class<Object[]> arrClazz = Object[].class;
        Object[] newObj = convertArray(arrClazz, obj);
        for (Object it : newObj) {
            if (null != it) {
                return false;
            }
        }
        return true;
    }

    /**
     * 与Java自己的toString不同在于,如果是数组,打印数组内容而不再是打印内存地址
     *
     * @param obj the obj
     * @return the string
     */
    @Contract("null -> null")
    public static String toStr(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            if (isPrimitive(obj)) {
                if (obj instanceof byte[]) {
                    return Arrays.toString((byte[]) obj);
                } else if (obj instanceof short[]) {
                    return Arrays.toString((short[]) obj);
                } else if (obj instanceof int[]) {
                    return Arrays.toString((int[]) obj);
                } else if (obj instanceof long[]) {
                    return Arrays.toString((long[]) obj);
                } else if (obj instanceof double[]) {
                    return Arrays.toString((double[]) obj);
                } else if (obj instanceof float[]) {
                    return Arrays.toString((float[]) obj);
                } else if (obj instanceof char[]) {
                    return Arrays.toString((char[]) obj);
                } else if (obj instanceof boolean[]) {
                    return Arrays.toString((boolean[]) obj);
                }
                return Arrays.toString((Object[]) obj);
            } else {
                return Arrays.deepToString((Object[]) obj);
            }
        }
        return obj.toString();
    }

    /**
     * 判断输入的数据是否为基本数据类型 基本数据类型和基本数据类型的数组类型都是做是基本数据类型 null视作非基本数据类型
     *
     * @param obj the obj
     * @return the boolean
     * @see #isPrimitive(Class) #isPrimitive(Class)#isPrimitive(Class)#isPrimitive(Class)#isPrimitive(Class)#isPrimitive(Class)
     *     #isPrimitive(Class) #isPrimitive(Class)
     */
    @Contract("null -> false")
    public static boolean isPrimitive(Object obj) {
        if (null == obj) {
            return false;
        }
        return isPrimitive(obj.getClass());
    }

    /**
     * 判断输入的数据类型是否为基本数据类型 基本数据类型和基本数据类型的数组类型都是做是基本数据类型 null视作非基本数据类型 void为基本数据类型
     * log.info("{}", isPrimitive(Long[].class));  //false
     * log.info("{}", isPrimitive(Long.class));    //false
     * log.info("{}", isPrimitive(long[].class));  //true
     * log.info("{}", isPrimitive(long.class));    //true
     * log.info("{}", isPrimitive(void.class));    //true
     * log.info("{}", isPrimitive(Object[].class));//false
     * log.info("{}", isPrimitive(Object.class));  //false
     *
     * @param clazz the clazz
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isPrimitive(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        if (clazz.isPrimitive()) {
            return true;
        }
        if (clazz.isArray()) {
            return int[].class.isAssignableFrom(clazz)
                   || long[].class.isAssignableFrom(clazz)
                   || double[].class.isAssignableFrom(clazz)
                   || float[].class.isAssignableFrom(clazz)
                   || short[].class.isAssignableFrom(clazz)
                   || byte[].class.isAssignableFrom(clazz)
                   || char[].class.isAssignableFrom(clazz)
                   || boolean[].class.isAssignableFrom(clazz);
        }
        return false;
    }

    /**
     * 判断输入的数据是否是char
     *
     * @param obj the obj
     * @return the boolean
     * @see #isChar(Class) #isChar(Class)#isChar(Class)#isChar(Class)#isChar(Class)#isChar(Class)#isChar(Class)#isChar(Class)
     */
    @Contract("null -> false")
    public static boolean isChar(Object obj) {
        if (obj == null) {
            return false;
        }
        if (isChar(obj.getClass())) {
            return true;
        }
        return obj.toString().length() == 1;
    }

    /**
     * 判断输入的数据类型是否是char
     *
     * @param clazz the clazz
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isChar(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (char.class.isAssignableFrom(clazz)) {
            return true;
        }
        return Character.class.isAssignableFrom(clazz);
    }

    /**
     * 判断输入的数据是否是数字 支持基本数据类型和装箱数据类型的判断 支持数组数据类型的判断
     * null视作不是数字 存在容错机制,如果数据类型不是数字,继续判断toString字符串是否为数字,并兼容正负号
     *
     * @param obj the obj
     * @return the boolean
     * @see #isNumber(Class)
     */
    @Contract("null -> false")
    public static boolean isNumber(Object obj) {
        if (null == obj) {
            return false;
        }
        if (isNumber(obj.getClass())) {
            return true;
        }
        // 进行容错,如果数据类型对不上,但依然有可能是字符串数字,那么用正则表达式判断是否是数字,并兼容正负号
        return obj.toString().matches("^([-+])?\\d+(\\.\\d+)?$");
    }

    /**
     * 判断输入的数据类型是否是数字 支持基本数据类型和装箱数据类型的判断 支持数组数据类型的判断 null视作不是数字
     * log.info("{}", isNumber(Long[].class));//true
     * log.info("{}", isNumber(Long.class));  //true
     * log.info("{}", isNumber(long.class));  //true
     * log.info("{}", isNumber(long.class));  //true
     * log.info("{}", isNumber(void.class));  //false
     * log.info("{}", isNumber(Object[].class));//false
     * log.info("{}", isNumber(Object.class));//false
     *
     * @param clazz the clazz
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isNumber(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        if (Number.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Number[].class.isAssignableFrom(clazz)) {
            return true;
        }
        if (clazz.isArray()) {
            return int[].class.isAssignableFrom(clazz)
                   || long[].class.isAssignableFrom(clazz)
                   || double[].class.isAssignableFrom(clazz)
                   || float[].class.isAssignableFrom(clazz)
                   || short[].class.isAssignableFrom(clazz)
                   || byte[].class.isAssignableFrom(clazz)
                   || char[].class.isAssignableFrom(clazz);
        }
        if (isPrimitive(clazz)) {
            return int.class.isAssignableFrom(clazz)
                   || long.class.isAssignableFrom(clazz)
                   || double.class.isAssignableFrom(clazz)
                   || float.class.isAssignableFrom(clazz)
                   || short.class.isAssignableFrom(clazz)
                   || byte.class.isAssignableFrom(clazz)
                   || char.class.isAssignableFrom(clazz);
        }
        return false;
    }

    /**
     * 判断对象是否是日期;
     * 进行类型判断以及内容判断;
     * 内容判断为判断字符串格式是否是时间格式;
     * 字符串判断:  日期部分兼容减号、斜杠、点号分隔;  时间部分兼容冒号、点号;  兼容空格;
     * 特别提醒: 2011.11这种既是日期又是数字,在这里也返回true,单纯的2011也会返回true
     *
     * @param obj the obj
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isDate(Object obj) {
        if (null == obj) {
            return false;
        }
        if (isDate(obj.getClass())) {
            return true;
        }
        // 进行容错,如果数据类型对不上,但依然有可能是字符串日期,那么用正则表达式判断是否日期,日期部分兼容减号、斜杠、点号分隔; 时间部分兼容冒号、点号; 兼容空格;
        return obj.toString().matches("^"
                                      + "( *)(\\d{1,4})( *)" //年
                                      + "(([-/.])?)( *)((\\d{1,2})?)( *)" //月
                                      + "(([-/.])?)( *)((\\d{1,2})?)( *)" //日
                                      + "((( +)\\d{1,2})?)( *)" //时
                                      + "(([:.])?)( *)((\\d{1,2})?)( *)" //分
                                      + "(([:.])?)( *)((\\d{1,2})?)( *)" //秒
                                      + "(([:.])?)( *)((\\d{1,3})?)( *)" //毫秒
                                      + "$");
    }

    /**
     * 判断Class是否是日期
     *
     * @param clazz the clazz
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isDate(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return Date.class.isAssignableFrom(clazz);
    }

    /**
     * 对输入数据进行装箱 如果输入数据是基本数据类型则返回装箱数据类型的数据 可以对基本数据类型的数组进行装箱
     *
     * @param srcObj the src obj
     * @return the object
     * @see #typeBoxing(Class) #typeBoxing(Class)#typeBoxing(Class)#typeBoxing(Class)#typeBoxing(Class)#typeBoxing(Class)#typeBoxing
     *     (Class)#typeBoxing (Class)#typeBoxing(Class)
     */
    @Contract("null -> null")
    public static Object boxing(Object srcObj) {
        if (null == srcObj) {
            return null;
        }
        Class<?> objClazz = srcObj.getClass();
        if (!isPrimitive(objClazz)) {
            return srcObj;
        }
        Object obj;
        if (!objClazz.isArray()) {
            obj = boxingObject(srcObj, objClazz);
        } else {
            obj = boxingArray(srcObj, objClazz);
        }
        return obj;
    }

    /**
     * Process array object
     *
     * @param srcObj   src obj
     * @param objClazz obj clazz
     * @return the object
     */
    @Nullable
    private static Object boxingArray(Object srcObj, Class<?> objClazz) {
        if (int[].class.isAssignableFrom(objClazz)) {
            int[] obj = (int[]) srcObj;
            Integer[] ret = new Integer[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        } else if (long[].class.isAssignableFrom(objClazz)) {
            long[] obj = (long[]) srcObj;
            Long[] ret = new Long[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        } else if (double[].class.isAssignableFrom(objClazz)) {
            double[] obj = (double[]) srcObj;
            Double[] ret = new Double[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        } else if (float[].class.isAssignableFrom(objClazz)) {
            float[] obj = (float[]) srcObj;
            Float[] ret = new Float[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        } else if (short[].class.isAssignableFrom(objClazz)) {
            short[] obj = (short[]) srcObj;
            Short[] ret = new Short[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        } else if (byte[].class.isAssignableFrom(objClazz)) {
            byte[] obj = (byte[]) srcObj;
            Byte[] ret = new Byte[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        } else if (char[].class.isAssignableFrom(objClazz)) {
            char[] obj = (char[]) srcObj;
            Character[] ret = new Character[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        } else if (boolean[].class.isAssignableFrom(objClazz)) {
            boolean[] obj = (boolean[]) srcObj;
            Boolean[] ret = new Boolean[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ret[i] = obj[i];
            }
            return ret;
        }
        return null;
    }

    /**
     * Process object object
     *
     * @param srcObj   src obj
     * @param objClazz obj clazz
     * @return the object
     */
    @Nullable
    @SuppressWarnings("all")
    private static Object boxingObject(Object srcObj, Class<?> objClazz) {
        if (int.class.isAssignableFrom(objClazz)) {
            return srcObj;
        } else if (long.class.isAssignableFrom(objClazz)) {
            return Long.valueOf((long) srcObj);
        } else if (double.class.isAssignableFrom(objClazz)) {
            return Double.valueOf((double) srcObj);
        } else if (float.class.isAssignableFrom(objClazz)) {
            return Float.valueOf((float) srcObj);
        } else if (short.class.isAssignableFrom(objClazz)) {
            return Short.valueOf((short) srcObj);
        } else if (byte.class.isAssignableFrom(objClazz)) {
            return Byte.valueOf((byte) srcObj);
        } else if (char.class.isAssignableFrom(objClazz)) {
            return Character.valueOf((char) srcObj);
        } else if (boolean.class.isAssignableFrom(objClazz)) {
            return Boolean.valueOf((boolean) srcObj);
        }
        return null;
    }

    /**
     * 对输入数据进行拆箱 如果输入数据是包装数据类型则返回拆箱数据类型的数据 可以对包装数据类型的数组进行拆箱
     *
     * @param obj the obj
     * @return the object
     * @see #typeUnBoxing(Class) #typeUnBoxing(Class)#typeUnBoxing(Class)#typeUnBoxing(Class)#typeUnBoxing(Class)#typeUnBoxing(Class)
     *     #typeUnBoxing(Class) #typeUnBoxing(Class)
     */
    @Contract("null -> null")
    public static Object unBoxing(Object obj) {
        if (null == obj) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        if (isPrimitive(clazz)) {
            return obj;
        }
        if (!clazz.isArray()) {
            return unBoxingObject(obj, clazz);
        } else {
            return unBoxingArray(obj, clazz);
        }
    }

    /**
     * Un boxing object object
     *
     * @param obj   obj
     * @param clazz clazz
     * @return the object
     */
    @Contract("_, _ -> param1")
    private static Object unBoxingObject(Object obj, Class<?> clazz) {
        if (Integer.class.isAssignableFrom(clazz)) {
            return obj;
        } else if (Long.class.isAssignableFrom(clazz)) {
            return obj;
        } else if (Double.class.isAssignableFrom(clazz)) {
            return obj;
        } else if (Float.class.isAssignableFrom(clazz)) {
            return obj;
        } else if (Short.class.isAssignableFrom(clazz)) {
            return obj;
        } else if (Byte.class.isAssignableFrom(clazz)) {
            return obj;
        } else if (Character.class.isAssignableFrom(clazz)) {
            return obj;
        } else {
            // Boolean
            return obj;
        }
    }

    /**
     * Un boxing array object
     *
     * @param obj   obj
     * @param clazz clazz
     * @return the object
     */
    private static Object unBoxingArray(Object obj, Class<?> clazz) {
        if (Integer[].class.isAssignableFrom(clazz)) {
            Integer[] tmp = (Integer[]) obj;
            int[] newObj = new int[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (int) unBoxing(tmp[i]);
            }
            return newObj;
        } else if (Long[].class.isAssignableFrom(clazz)) {
            Long[] tmp = (Long[]) obj;
            long[] newObj = new long[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (long) unBoxing(tmp[i]);
            }
            return newObj;
        } else if (Double[].class.isAssignableFrom(clazz)) {
            Double[] tmp = (Double[]) obj;
            double[] newObj = new double[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (double) unBoxing(tmp[i]);
            }
            return newObj;
        } else if (Float[].class.isAssignableFrom(clazz)) {
            Float[] tmp = (Float[]) obj;
            float[] newObj = new float[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (float) unBoxing(tmp[i]);
            }
            return newObj;
        } else if (Short[].class.isAssignableFrom(clazz)) {
            Short[] tmp = (Short[]) obj;
            short[] newObj = new short[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (short) unBoxing(tmp[i]);
            }
            return newObj;
        } else if (Byte[].class.isAssignableFrom(clazz)) {
            Byte[] tmp = (Byte[]) obj;
            byte[] newObj = new byte[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (byte) unBoxing(tmp[i]);
            }
            return newObj;
        } else if (Character[].class.isAssignableFrom(clazz)) {
            Character[] tmp = (Character[]) obj;
            char[] newObj = new char[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (char) unBoxing(tmp[i]);
            }
            return newObj;
        } else {
            Boolean[] tmp = (Boolean[]) obj;
            boolean[] newObj = new boolean[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newObj[i] = (boolean) unBoxing(tmp[i]);
            }
            return newObj;
        }
    }

    /**
     * 对输入数据类型进行装箱 把八种基本数据类型装箱为包装类型 支持对基本数据类型的数组类型装箱为包装类型
     * void类型以及非基本数据类型不进行装箱原样返回 输入null返回null不作处理
     * log.info("{}", typeBoxing(Long[].class));// 原样返回=>class [Ljava.lang.Long;
     * log.info("{}", typeBoxing(Long.class));// 原样返回=>class java.lang.Long
     * log.info("{}", typeBoxing(long[].class));// 装箱=>class [Ljava.lang.Long;
     * log.info("{}", typeBoxing(long.class));// 装箱=>class java.lang.Long
     * log.info("{}", typeBoxing(void.class));// 原样返回=>void
     * log.info("{}", typeBoxing(Object[].class));// 原样返回=>class [Ljava.lang.Object;
     * log.info("{}", typeBoxing(Object.class));// 原样返回=>class java.lang.Object
     *
     * @param clazz the clazz
     * @return the class
     * @see #typeUnBoxing(Class) #typeUnBoxing(Class)#typeUnBoxing(Class)#typeUnBoxing(Class)#typeUnBoxing(Class)#typeUnBoxing(Class)
     *     #typeUnBoxing(Class) #typeUnBoxing(Class)
     */
    @Contract("null -> null")
    public static Class<?> typeBoxing(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        if (!isPrimitive(clazz)) {
            return clazz;
        }
        if (!clazz.isArray()) {
            if (int.class.isAssignableFrom(clazz)) {
                return Integer.class;
            } else if (long.class.isAssignableFrom(clazz)) {
                return Long.class;
            } else if (double.class.isAssignableFrom(clazz)) {
                return Double.class;
            } else if (float.class.isAssignableFrom(clazz)) {
                return Float.class;
            } else if (short.class.isAssignableFrom(clazz)) {
                return Short.class;
            } else if (byte.class.isAssignableFrom(clazz)) {
                return Byte.class;
            } else if (char.class.isAssignableFrom(clazz)) {
                return Character.class;
            } else if (boolean.class.isAssignableFrom(clazz)) {
                return Boolean.class;
            }
        } else {
            if (int[].class.isAssignableFrom(clazz)) {
                return Integer[].class;
            } else if (long[].class.isAssignableFrom(clazz)) {
                return Long[].class;
            } else if (double[].class.isAssignableFrom(clazz)) {
                return Double[].class;
            } else if (float[].class.isAssignableFrom(clazz)) {
                return Float[].class;
            } else if (short[].class.isAssignableFrom(clazz)) {
                return Short[].class;
            } else if (byte[].class.isAssignableFrom(clazz)) {
                return Byte[].class;
            } else if (char[].class.isAssignableFrom(clazz)) {
                return Character[].class;
            } else if (boolean[].class.isAssignableFrom(clazz)) {
                return Boolean[].class;
            }
        }
        return clazz;
    }

    /**
     * 对输入数据类型进行拆箱 把八种基本数据类型的包装类拆箱为基本类型 支持对包装数据类型的数组类型拆箱为基本数据类型的数组类型
     * void类型和基本数据类型不进行拆箱原样返回 输入null返回null不作处理
     * log.info("{}", typeUnBoxing(Long[].class));// 拆箱=>class [J
     * log.info("{}", typeUnBoxing(Long.class));// 拆箱=>long
     * log.info("{}", typeUnBoxing(long[].class));// 原样返回=>class [J
     * log.info("{}", typeUnBoxing(long.class));// 原样返回=>long
     * log.info("{}", typeUnBoxing(void.class));// 原样返回=>void
     * log.info("{}", typeUnBoxing(Object[].class));// 原样返回=>class [Ljava.lang.Object;
     * log.info("{}", typeUnBoxing(Object.class));// 原样返回=>class java.lang.Object
     *
     * @param clazz the clazz
     * @return the class
     * @see #typeBoxing(Class) #typeBoxing(Class)#typeBoxing(Class)#typeBoxing(Class)#typeBoxing(Class)#typeBoxing(Class)#typeBoxing
     *     (Class)#typeBoxing(Class)
     */
    @Contract("null -> null")
    public static Class<?> typeUnBoxing(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        if (isPrimitive(clazz)) {
            return clazz;
        }
        if (!clazz.isArray()) {
            if (Integer.class.isAssignableFrom(clazz)) {
                return int.class;
            } else if (Long.class.isAssignableFrom(clazz)) {
                return long.class;
            } else if (Double.class.isAssignableFrom(clazz)) {
                return double.class;
            } else if (Float.class.isAssignableFrom(clazz)) {
                return float.class;
            } else if (Short.class.isAssignableFrom(clazz)) {
                return short.class;
            } else if (Byte.class.isAssignableFrom(clazz)) {
                return byte.class;
            } else if (Character.class.isAssignableFrom(clazz)) {
                return char.class;
            } else if (Boolean.class.isAssignableFrom(clazz)) {
                return boolean.class;
            }
        } else {
            if (Integer[].class.isAssignableFrom(clazz)) {
                return int[].class;
            } else if (Long[].class.isAssignableFrom(clazz)) {
                return long[].class;
            } else if (Double[].class.isAssignableFrom(clazz)) {
                return double[].class;
            } else if (Float[].class.isAssignableFrom(clazz)) {
                return float[].class;
            } else if (Short[].class.isAssignableFrom(clazz)) {
                return short[].class;
            } else if (Byte[].class.isAssignableFrom(clazz)) {
                return byte[].class;
            } else if (Character[].class.isAssignableFrom(clazz)) {
                return char[].class;
            } else if (Boolean[].class.isAssignableFrom(clazz)) {
                return boolean[].class;
            }
        }
        return clazz;
    }

    /**
     * The type Data type exception.
     */
    static final class DataTypeException extends BasicException {

        /** serialVersionUID */
        private static final long serialVersionUID = 4654330042965245163L;

        /**
         * Instantiates a new Data type exception.
         *
         * @param message the message
         */
        DataTypeException(String message) {
            super(message);
        }
    }

    private static final class CatchDataTypeException {

        /** LONG_MAX */
        private static final BigDecimal LONG_MAX = BigDecimal.valueOf(Long.MAX_VALUE);
        /** LONG_MIN */
        private static final BigDecimal LONG_MIN = BigDecimal.valueOf(Long.MIN_VALUE);
        /** INTEGER_MAX */
        private static final BigDecimal INTEGER_MAX = BigDecimal.valueOf(Integer.MAX_VALUE);
        /** INTEGER_MIN */
        private static final BigDecimal INTEGER_MIN = BigDecimal.valueOf(Integer.MIN_VALUE);
        /** SHORT_MAX */
        private static final BigDecimal SHORT_MAX = BigDecimal.valueOf(Short.MAX_VALUE);
        /** SHORT_MIN */
        private static final BigDecimal SHORT_MIN = BigDecimal.valueOf(Short.MIN_VALUE);
        /** BYTE_MAX */
        private static final BigDecimal BYTE_MAX = BigDecimal.valueOf(Byte.MAX_VALUE);
        /** BYTE_MIN */
        private static final BigDecimal BYTE_MIN = BigDecimal.valueOf(Byte.MIN_VALUE);
        /** CHAR_MAX */
        private static final BigDecimal CHAR_MAX = BigDecimal.valueOf(Character.MAX_VALUE);
        /** CHAR_MIN */
        private static final BigDecimal CHAR_MIN = BigDecimal.valueOf(Character.MIN_VALUE);
        /** DOUBLE_MAX */
        private static final BigDecimal DOUBLE_MAX = BigDecimal.valueOf(Double.MAX_VALUE);
        /** DOUBLE_MIN */
        private static final BigDecimal DOUBLE_MIN = BigDecimal.valueOf(-Double.MAX_VALUE);
        /** FLOAT_MAX */
        private static final BigDecimal FLOAT_MAX = BigDecimal.valueOf(Float.MAX_VALUE);
        /** FLOAT_MIN */
        private static final BigDecimal FLOAT_MIN = BigDecimal.valueOf(-Float.MAX_VALUE);

        /**
         * 如果是小数,则抛出无法转换数据类型的异常
         *
         * @param <T>        parameter
         * @param clazz      clazz
         * @param targetData target data
         * @author hcqt @qq.com
         */
        private static <T> void notDecimal(Class<T> clazz, @NotNull BigDecimal targetData) {
            BigDecimal bigDecimal = targetData.setScale(0, BigDecimal.ROUND_CEILING);
            if (targetData.compareTo(bigDecimal) != 0) {
                throw new DataTypeException(
                    StringFormatter.format("类型为 {} 的数据 {} 为小数,无法转换为类型 {}",
                                           targetData.getClass(),
                                           targetData,
                                           clazz.getName()));
            }
        }

        /**
         * Is between span *
         *
         * @param <T>       parameter
         * @param destClazz dest clazz
         * @param srcObj    src obj
         * @author hcqt @qq.com
         * @see #isBetweenSpan(Class, Object) #isBetweenSpan(Class, Object)#isBetweenSpan(Class, Object)
         */
        private static <T> void isBetweenSpan(Class<T> destClazz, BigDecimal srcObj) {
            isBetweenSpan(destClazz, (Object) srcObj);
        }

        /**
         * 检查目标数字是否介于最大值与最小值之间,符合指定数据类型的取值范围
         *
         * @param <T>       parameter
         * @param destClazz dest clazz
         * @param srcObj    src obj
         * @author hcqt @qq.com
         */
        private static <T> void isBetweenSpan(Class<T> destClazz, Object srcObj) {
            if (!DataTypeUtils.isNumber(srcObj)) {
                throw CatchDataTypeException.returnCouldNotConvertException(destClazz, srcObj);
            }
            BigDecimal max = null;
            BigDecimal min = null;
            if (Long.class.isAssignableFrom(destClazz)) {
                max = LONG_MAX;
                min = LONG_MIN;
            } else if (Integer.class.isAssignableFrom(destClazz)) {
                max = INTEGER_MAX;
                min = INTEGER_MIN;
            } else if (Double.class.isAssignableFrom(destClazz)) {
                max = DOUBLE_MAX;
                min = DOUBLE_MIN;
            } else if (Float.class.isAssignableFrom(destClazz)) {
                max = FLOAT_MAX;
                min = FLOAT_MIN;
            } else if (Short.class.isAssignableFrom(destClazz)) {
                max = SHORT_MAX;
                min = SHORT_MIN;
            } else if (Byte.class.isAssignableFrom(destClazz)) {
                max = BYTE_MAX;
                min = BYTE_MIN;
            } else if (Character.class.isAssignableFrom(destClazz)) {
                max = CHAR_MAX;
                min = CHAR_MIN;
            }
            BigDecimal src = new BigDecimal(srcObj.toString());
            boolean maxOverflow = max != null && src.compareTo(max) > 0;
            boolean minOverflow = min != null && src.compareTo(min) < 0;
            if (maxOverflow || minOverflow) {
                throw new DataTypeException(
                    StringFormatter.format("类型为 {} 的数值 {} 不在数据类型 {} 可以容纳的数值范围内",
                                           srcObj.getClass().getName(),
                                           srcObj,
                                           destClazz.getName()));
            }
        }

        /**
         * 用于抛出无法转换数据类型的异常——输入的数据无法转换为预期的类型 该异常总是抛出,使用前你必须确定你是需要抛出此异常
         *
         * @param <T>       parameter
         * @param destClazz 预期的类型
         * @param srcObj    输入的数据
         * @return the data type exception
         * @author hcqt @qq.com
         */
        @NotNull
        @Contract("_, _ -> new")
        private static <T> DataTypeException returnCouldNotConvertException(Class<T> destClazz, Object srcObj) {
            return new DataTypeException(
                StringFormatter.format("类型为 {} 的数据 {} 无法转换为类型 {}",
                                       srcObj == null ? "null" : srcObj.getClass(),
                                       srcObj,
                                       destClazz == null ? "null" : destClazz.getName()));
        }

    }

    private static final class Object2Byte {

        /**
         * Convert byte
         *
         * @param targetObject target object
         * @return the byte
         */
        private static Byte convert(@NotNull Object targetObject) {
            if (targetObject.getClass().isEnum()) {
                return convert(((Enum<?>) targetObject).ordinal());
            } else if (isNumber(targetObject)) {
                BigDecimal targetData = new BigDecimal(targetObject.toString());
                CatchDataTypeException.notDecimal(Byte.class, targetData);
                CatchDataTypeException.isBetweenSpan(Byte.class, targetData);
                if (targetObject instanceof String
                    || targetObject instanceof Long
                    || targetObject instanceof Byte
                    || targetObject instanceof Short
                    || targetObject instanceof Integer
                    || targetObject instanceof BigDecimal
                    || targetObject instanceof Float
                    || targetObject instanceof Double) {
                    return targetData.byteValue();
                } else {
                    throw CatchDataTypeException.returnCouldNotConvertException(Byte.class, targetObject);
                }
            } else if (isChar(targetObject)) {
                return Integer.valueOf(targetObject.toString().charAt(0)).byteValue();
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return convert(d.getTime());
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Byte.class, targetObject);
        }

    }

    private static final class Object2Short {

        /**
         * Convert short
         *
         * @param targetObject target object
         * @return the short
         */
        private static Short convert(@NotNull Object targetObject) {
            if (targetObject.getClass().isEnum()) {
                return convert(((Enum<?>) targetObject).ordinal());
            } else if (isNumber(targetObject)) {
                BigDecimal targetData = BigDecimal.valueOf(Double.parseDouble(targetObject.toString()));

                CatchDataTypeException.notDecimal(Short.class, targetData);
                CatchDataTypeException.isBetweenSpan(Short.class, targetData);
                if (targetObject instanceof String
                    || targetObject instanceof Long
                    || targetObject instanceof Byte
                    || targetObject instanceof Short
                    || targetObject instanceof Integer
                    || targetObject instanceof BigDecimal
                    || targetObject instanceof Float
                    || targetObject instanceof Double
                ) {
                    return targetData.shortValue();
                } else {
                    throw CatchDataTypeException.returnCouldNotConvertException(Short.class, targetObject);
                }
            } else if (isChar(targetObject)) {
                return Integer.valueOf(targetObject.toString().charAt(0)).shortValue();
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return convert(d.getTime());
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Short.class, targetObject);
        }

    }

    private static final class Object2Integer {

        /**
         * Convert integer
         *
         * @param targetObject target object
         * @return the integer
         */
        private static Integer convert(@NotNull Object targetObject) {
            if (targetObject.getClass().isEnum()) {
                return ((Enum<?>) targetObject).ordinal();
            } else if (isNumber(targetObject)) {
                BigDecimal targetData = BigDecimal.valueOf(Double.parseDouble(targetObject.toString()));

                CatchDataTypeException.notDecimal(Integer.class, targetData);
                CatchDataTypeException.isBetweenSpan(Integer.class, targetData);
                if (targetObject instanceof String
                    || targetObject instanceof Long
                    || targetObject instanceof Byte
                    || targetObject instanceof Short
                    || targetObject instanceof Integer
                    || targetObject instanceof BigDecimal
                    || targetObject instanceof Float
                    || targetObject instanceof Double) {
                    return targetData.intValue();
                } else {
                    throw CatchDataTypeException.returnCouldNotConvertException(Integer.class, targetObject);
                }
            } else if (isChar(targetObject)) {
                return (int) targetObject.toString().charAt(0);
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return convert(d.getTime());
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Integer.class, targetObject);
        }

    }

    private static final class Object2Long {

        /**
         * Convert long
         *
         * @param targetObject target object
         * @return the long
         */
        private static Long convert(@NotNull Object targetObject) {
            if (targetObject.getClass().isEnum()) {
                return (long) ((Enum<?>) targetObject).ordinal();
            } else if (isNumber(targetObject)) {
                BigDecimal targetData = BigDecimal.valueOf(Double.parseDouble(targetObject.toString()));
                CatchDataTypeException.notDecimal(Long.class, targetData);
                CatchDataTypeException.isBetweenSpan(Long.class, targetData);
                if (targetObject instanceof String
                    || targetObject instanceof Long
                    || targetObject instanceof Byte
                    || targetObject instanceof Short
                    || targetObject instanceof Integer
                    || targetObject instanceof BigDecimal
                    || targetObject instanceof Float
                    || targetObject instanceof Double
                ) {
                    return targetData.longValue();
                } else {
                    throw CatchDataTypeException.returnCouldNotConvertException(Long.class, targetObject);
                }
            } else if (isChar(targetObject)) {
                return (long) (int) targetObject.toString().charAt(0);
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return d.getTime();
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Long.class, targetObject);
        }

    }

    private static final class Object2Double {

        /**
         * Convert double
         *
         * @param targetObject target object
         * @return the double
         */
        private static Double convert(@NotNull Object targetObject) {
            if (targetObject.getClass().isEnum()) {
                return (double) ((Enum<?>) targetObject).ordinal();
            } else if (isNumber(targetObject)) {
                BigDecimal targetData = BigDecimal.valueOf(Double.parseDouble(targetObject.toString()));
                CatchDataTypeException.isBetweenSpan(Double.class, targetData);
                return targetData.doubleValue();
            } else if (isChar(targetObject)) {
                return (double) (int) targetObject.toString().charAt(0);
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return convert(d.getTime());
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Double.class, targetObject);
        }

    }

    private static final class Object2Float {

        /**
         * Convert float
         *
         * @param targetObject target object
         * @return the float
         */
        private static Float convert(@NotNull Object targetObject) {
            if (targetObject.getClass().isEnum()) {
                return convert(((Enum<?>) targetObject).ordinal());
            } else if (isNumber(targetObject)) {
                BigDecimal targetData = BigDecimal.valueOf(Double.parseDouble(targetObject.toString()));
                CatchDataTypeException.isBetweenSpan(Float.class, targetData);
                if (targetObject instanceof String
                    || targetObject instanceof Long
                    || targetObject instanceof Byte
                    || targetObject instanceof Short
                    || targetObject instanceof Integer
                    || targetObject instanceof BigDecimal
                    || targetObject instanceof Float
                    || targetObject instanceof Double) {
                    return targetData.floatValue();
                } else {
                    throw CatchDataTypeException.returnCouldNotConvertException(Float.class, targetObject);
                }
            } else if (isChar(targetObject)) {
                return (float) (int) targetObject.toString().charAt(0);
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return convert(d.getTime());
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Float.class, targetObject);
        }

    }

    private static final class Object2BigDecimal {

        /**
         * Convert big decimal
         *
         * @param targetObject target object
         * @return the big decimal
         */
        private static BigDecimal convert(@NotNull Object targetObject) {
            if (targetObject.getClass().isEnum()) {
                return BigDecimal.valueOf(((Enum<?>) targetObject).ordinal());
            } else if (isNumber(targetObject)) {
                try {
                    return BigDecimal.valueOf(Double.parseDouble(targetObject.toString()));
                } catch (Exception e) {
                    throw CatchDataTypeException.returnCouldNotConvertException(BigDecimal.class, targetObject);
                }
            } else if (isChar(targetObject)) {
                return BigDecimal.valueOf((int) targetObject.toString().charAt(0));
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return BigDecimal.valueOf(d.getTime());
            }
            throw CatchDataTypeException.returnCouldNotConvertException(BigDecimal.class, targetObject);
        }

    }

    private static final class Object2Character {

        /**
         * Convert character
         *
         * @param targetObject target object
         * @return the character
         */
        private static Character convert(Object targetObject) {
            if (isChar(targetObject)) {
                return targetObject.toString().charAt(0);
            } else if (isNumber(targetObject)) {
                BigDecimal b = DataTypeUtils.convert(BigDecimal.class, targetObject);
                CatchDataTypeException.notDecimal(Character.class, b);
                CatchDataTypeException.isBetweenSpan(Character.class, b);
                return (char) b.intValue();
            } else if (targetObject.getClass().isEnum()) {
                Integer i = DataTypeUtils.convert(Integer.class, targetObject);
                return convert(i);
            } else if (isDate(targetObject)) {
                Date d = DataTypeUtils.convert(Date.class, targetObject);
                return convert(d.getTime());
            } else if (targetObject instanceof String) {
                String s = (String) targetObject;
                if (s.length() != 1) {
                    throw CatchDataTypeException.returnCouldNotConvertException(Character.class, targetObject);
                }
                return (s).charAt(0);
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Character.class, targetObject);
        }

    }

    private static final class Object2Boolean {

        /**
         * Convert boolean
         *
         * @param targetObject target object
         * @return the boolean
         */
        @NotNull
        private static Boolean convert(Object targetObject) {
            if (targetObject instanceof String) {
                return Boolean.valueOf((String) targetObject);
            } else if (targetObject instanceof Boolean) {
                return (Boolean) targetObject;
            }
            throw CatchDataTypeException.returnCouldNotConvertException(Boolean.class, targetObject);
        }
    }

    private static final class Object2Enum {

        /**
         * Convert t
         *
         * @param <T>          parameter
         * @param clazz        clazz
         * @param targetObject target object
         * @return the t
         */
        private static <T> T convert(@NotNull Class<T> clazz, Object targetObject) {
            T[] enumConstants = clazz.getEnumConstants();
            if (targetObject instanceof String) {
                for (T enumElement : enumConstants) {
                    try {
                        Method method = enumElement.getClass().getMethod("name");
                        Object enumName;
                        enumName = method.invoke(enumElement);
                        if (null == enumName) {
                            continue;
                        }
                        if (enumName.toString().equals(targetObject.toString())) {
                            return enumElement;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            // 尝试输入值是否可以转换为整型,如果可以,那么以枚举底层整型下标进行转换
            try {
                Integer index = DataTypeUtils.convert(Integer.class, targetObject);
                for (T enumElement : enumConstants) {
                    try {
                        Method method = enumElement.getClass().getMethod("ordinal");
                        Object enumOrdinal;
                        enumOrdinal = method.invoke(enumElement);
                        if (null == enumOrdinal) {
                            continue;
                        }
                        if (enumOrdinal.toString().equals(index.toString())) {
                            return enumElement;
                        }
                    } catch (Exception ignored) {
                    }
                }
            } catch (BasicException ignored) {
            }
            throw new DataTypeException(
                StringFormatter.format("无法把数据类型为 {} 的数据 {} 转换到枚举类型 {}", targetObject.getClass().getName(), targetObject, clazz));
        }

    }

    private static final class Object2String {

        /**
         * Convert string
         *
         * @param targetObject target object
         * @return the string
         */
        private static String convert(Object targetObject) {
            String ret = null;
            if (null == targetObject) {
                ret = null;
            } else if (isDate(targetObject) && !isNumber(targetObject)) {
                // 这里需要既是日期,又不是数字才进行转换,存在例如数据2011.11|2011这种既是数字又是时间的数据中抉择的问题,那么此处偏向与认为这是一个数字,不转换为日期显示
                Date d = Object2Date.convert(targetObject);
                ret = DateUtils.format(d, JsonUtils.PATTERN_DATETIME);
            }
            if (null == ret && targetObject != null) {
                ret = targetObject.toString();
            }
            return ret;
        }

    }

    private static final class Object2Date {

        /**
         * Convert date
         *
         * @param targetObject target object
         * @return the date
         */
        @Nullable
        private static Date convert(Object targetObject) {
            if (!isDate(targetObject)) {
                return null;
            } else {
                if (targetObject instanceof Date) {
                    return (Date) targetObject;
                } else if (targetObject instanceof String) {
                    try {
                        return DateUtils.parse((String) targetObject, JsonUtils.PATTERN_DATETIME);
                    } catch (BasicException ignored) {
                    }
                }
            }
            // 注意此处不用else if的原因是由于存在2011.11这种既是日期又是数字的情况,所以不能用else if做互斥代码
            if (isNumber(targetObject)) {
                try {
                    return new Date(DataTypeUtils.convert(Long.class, targetObject));
                } catch (BasicException ignored) {
                }
            }

            throw CatchDataTypeException.returnCouldNotConvertException(Date.class, targetObject);
        }

    }

}
