package com.yanger.tools.web.exception;

import com.yanger.tools.web.support.IResultCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类
 * @Author yanger
 * @Date 2020/12/18 14:29
 */
public class AssertUtils extends Asserts {

    /**
     * 失败结果
     * @param resultCode 异常错误码
     */
    @Contract("_ -> fail")
    public static void fail(@NotNull IResultCode resultCode) {
        throw new BasicException(resultCode);
    }

    /**
     * 失败结果
     * @param resultCode 异常错误码
     * @param params    参数占位符
     */
    @Contract("_ -> fail")
    public static void fail(@NotNull IResultCode resultCode, Object... params) {
        throw new BasicException(resultCode, params);
    }

    /**
     * 大于O
     * @param num       the num
     * @param resultCode the error code
     */
    public static void gtZero(Integer num, IResultCode resultCode) {
        if (num == null || num <= 0) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * 大于O
     * @param num       the num
     * @param resultCode the error code
     */
    public static void gtZero(Integer num, IResultCode resultCode, Object... params) {
        if (num == null || num <= 0) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * 大于等于O
     * @param num       the num
     * @param resultCode the error code
     */
    public static void geZero(Integer num, IResultCode resultCode) {
        if (num == null || num < 0) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * 大于等于O
     * @param num       the num
     * @param resultCode the error code
     */
    public static void geZero(Integer num, IResultCode resultCode, Object... params) {
        if (num == null || num < 0) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * num1大于num2
     * @param num1      the num 1
     * @param num2      the num 2
     * @param resultCode the error code
     */
    public static void gt(Integer num1, Integer num2, IResultCode resultCode) {
        if (num1 <= num2) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * num1大于num2
     * @param num1      the num 1
     * @param num2      the num 2
     * @param resultCode the error code
     */
    public static void gt(Integer num1, Integer num2, IResultCode resultCode, Object... params) {
        if (num1 <= num2) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * num1大于等于num2
     * @param num1      the num 1
     * @param num2      the num 2
     * @param resultCode the error code
     */
    public static void ge(Integer num1, Integer num2, IResultCode resultCode) {
        if (num1 < num2) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * num1大于等于num2
     * @param num1      the num 1
     * @param num2      the num 2
     * @param resultCode the error code
     */
    public static void ge(Integer num1, Integer num2, IResultCode resultCode, Object... params) {
        if (num1 < num2) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * obj1 eq obj2
     * @param obj1      the obj 1
     * @param obj2      the obj 2
     * @param resultCode the error code
     */
    public static void eq(@NotNull Object obj1, Object obj2, IResultCode resultCode) {
        if (!obj1.equals(obj2)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * obj1 eq obj2
     * @param obj1      the obj 1
     * @param obj2      the obj 2
     * @param resultCode the error code
     */
    public static void eq(@NotNull Object obj1, Object obj2, IResultCode resultCode, Object... params) {
        if (!obj1.equals(obj2)) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Is true.
     * @param condition the condition
     * @param resultCode the error code
     */
    public static void isTrue(boolean condition, IResultCode resultCode) {
        if (!condition) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Is true
     * @param condition the condition
     * @param resultCode the error code
     */
    public static void isTrue(boolean condition, IResultCode resultCode, Object... params) {
        if (!condition) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Is false
     * @param condition the condition
     * @param resultCode the error code
     */
    public static void isFalse(boolean condition, IResultCode resultCode) {
        if (condition) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Is false
     * @param condition the condition
     * @param resultCode the error code
     */
    public static void isFalse(boolean condition, IResultCode resultCode, Object... params) {
        if (condition) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Is null.
     * @param resultCode the error codes
     * @param target    target
     */
    public static void isNull(Object target, IResultCode resultCode) {
        if (!ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Is null.
     * @param resultCode the error codes
     * @param target    target
     */
    public static void isNull(Object target, IResultCode resultCode, Object... params) {
        if (!ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Fail.
     * @param condition the condition
     * @param resultCode the error code
     */
    public static void fail(boolean condition, IResultCode resultCode) {
        if (condition) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Fail.
     * @param condition the condition
     * @param resultCode the error code
     */
    public static void fail(boolean condition, IResultCode resultCode, Object... params) {
        if (condition) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Not empty
     * @param array     the array
     * @param resultCode the error code
     */
    public static void notEmpty(Object[] array, IResultCode resultCode) {
        if (ObjectUtils.isEmpty(array)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Not empty.
     * @param array     the array
     * @param resultCode the error code
     */
    public static void notEmpty(Object[] array, IResultCode resultCode, Object... params) {
        if (ObjectUtils.isEmpty(array)) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * No null elements.
     * @param array     the array
     * @param resultCode the error code
     */
    public static void noNullElements(Object[] array, IResultCode resultCode) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    AssertUtils.fail(resultCode);
                }
            }
        }
    }

    /**
     * No null elements
     * @param array     the array
     * @param resultCode the error code
     */
    public static void noNullElements(Object[] array, IResultCode resultCode, Object... params) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    AssertUtils.fail(resultCode, params);
                }
            }
        }
    }

    /**
     * Not empty.
     * @param collection the collection
     * @param resultCode  the error code
     */
    public static void notEmpty(Collection<?> collection, IResultCode resultCode) {
        if (!CollectionUtils.isEmpty(collection)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Not empty
     * @param collection the collection
     * @param resultCode  the error code
     */
    public static void notEmpty(Collection<?> collection, IResultCode resultCode, Object... params) {
        if (!CollectionUtils.isEmpty(collection)) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Not empty
     * @param map       the map
     * @param resultCode the error code
     */
    public static void notEmpty(Map<?, ?> map, IResultCode resultCode) {
        if (ObjectUtils.isEmpty(map)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Not empty.
     * @param map       the map
     * @param resultCode the error code
     */
    public static void notEmpty(Map<?, ?> map, IResultCode resultCode, Object... params) {
        if (ObjectUtils.isEmpty(map)) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Is instance of.
     * @param type      the type
     * @param obj       the obj
     * @param resultCode the error code
     */
    public static void isInstanceOf(Class<?> type, Object obj, IResultCode resultCode) {
        AssertUtils.notNull(type, resultCode);
        if (!type.isInstance(obj)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Is instance of.
     * @param type      the type
     * @param obj       the obj
     * @param resultCode the error code
     */
    public static void isInstanceOf(Class<?> type, Object obj, IResultCode resultCode, Object... params) {
        AssertUtils.notNull(type, resultCode);
        if (!type.isInstance(obj)) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Not null.
     * @param resultCode the error codes
     * @param target    target
     */
    public static void notNull(Object target, IResultCode resultCode) {
        if (ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Not null.
     * @param resultCode the error codes
     * @param target    target
     */
    public static void notNull(Object target, IResultCode resultCode, Object... params) {
        if (ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(resultCode, params);
        }
    }

    /**
     * Is assignable.
     * @param superType the super type
     * @param subType   the sub type
     * @param resultCode the error code
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, IResultCode resultCode) {
        AssertUtils.notNull(superType, resultCode);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            AssertUtils.fail(resultCode);
        }
    }

    /**
     * Is assignable.
     * @param superType the super type
     * @param subType   the sub type
     * @param resultCode the error code
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, IResultCode resultCode, Object... params) {
        AssertUtils.notNull(superType, resultCode);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            AssertUtils.fail(resultCode, params);
        }
    }

}
