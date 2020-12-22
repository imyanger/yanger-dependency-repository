package com.yanger.web.exception;

import com.yanger.web.support.IResultCode;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

/**
 * @Description 断言工具类
 * @Author yanger
 * @Date 2020/12/18 14:29
 */
public class AssertUtils extends Asserts {

    /**
     * 失败结果
     *
     * @param errorCode 异常错误码
     */
    @Contract("_ -> fail")
    public static void fail(@NotNull IResultCode errorCode) {
        fail(errorCode.generateCode(), errorCode.generateMessage());
    }

    /**
     * 失败结果
     *
     * @param errorCode 异常错误码
     * @param params    参数占位符
     */
    @Contract("_ -> fail")
    public static void fail(@NotNull IResultCode errorCode, Object... params) {
        fail(errorCode.generateCode(), errorCode.generateMessage(params));
    }

    /**
     * 大于O
     *
     * @param num       the num
     * @param errorCode the error code
     */
    public static void gtZero(Integer num, IResultCode errorCode) {
        if (num == null || num <= 0) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * 大于O
     *
     * @param num       the num
     * @param errorCode the error code
     */
    public static void gtZero(Integer num, IResultCode errorCode, Object... params) {
        if (num == null || num <= 0) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * 大于等于O
     *
     * @param num       the num
     * @param errorCode the error code
     */
    public static void geZero(Integer num, IResultCode errorCode) {
        if (num == null || num < 0) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * 大于等于O
     *
     * @param num       the num
     * @param errorCode the error code
     */
    public static void geZero(Integer num, IResultCode errorCode, Object... params) {
        if (num == null || num < 0) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * num1大于num2
     *
     * @param num1      the num 1
     * @param num2      the num 2
     * @param errorCode the error code
     */
    public static void gt(Integer num1, Integer num2, IResultCode errorCode) {
        if (num1 <= num2) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * num1大于num2
     *
     * @param num1      the num 1
     * @param num2      the num 2
     * @param errorCode the error code
     */
    public static void gt(Integer num1, Integer num2, IResultCode errorCode, Object... params) {
        if (num1 <= num2) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * num1大于等于num2
     *
     * @param num1      the num 1
     * @param num2      the num 2
     * @param errorCode the error code
     */
    public static void ge(Integer num1, Integer num2, IResultCode errorCode) {
        if (num1 < num2) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * num1大于等于num2
     *
     * @param num1      the num 1
     * @param num2      the num 2
     * @param errorCode the error code
     */
    public static void ge(Integer num1, Integer num2, IResultCode errorCode, Object... params) {
        if (num1 < num2) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * obj1 eq obj2
     *
     * @param obj1      the obj 1
     * @param obj2      the obj 2
     * @param errorCode the error code
     */
    public static void eq(@NotNull Object obj1, Object obj2, IResultCode errorCode) {
        if (!obj1.equals(obj2)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * obj1 eq obj2
     *
     * @param obj1      the obj 1
     * @param obj2      the obj 2
     * @param errorCode the error code
     */
    public static void eq(@NotNull Object obj1, Object obj2, IResultCode errorCode, Object... params) {
        if (!obj1.equals(obj2)) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Is true.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void isTrue(boolean condition, IResultCode errorCode) {
        if (!condition) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Is true.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void isTrue(boolean condition, IResultCode errorCode, Object... params) {
        if (!condition) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Is false.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void isFalse(boolean condition, IResultCode errorCode) {
        if (condition) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Is false.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void isFalse(boolean condition, IResultCode errorCode, Object... params) {
        if (condition) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Is null.
     *
     * @param errorCode the error codes
     * @param target    target
     */
    public static void isNull(Object target, IResultCode errorCode) {
        if (!ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Is null.
     *
     * @param errorCode the error codes
     * @param target    target
     */
    public static void isNull(Object target, IResultCode errorCode, Object... params) {
        if (!ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Fail.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void fail(boolean condition, IResultCode errorCode) {
        if (condition) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Fail.
     *
     * @param condition the condition
     * @param errorCode the error code
     */
    public static void fail(boolean condition, IResultCode errorCode, Object... params) {
        if (condition) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Not empty.
     *
     * @param array     the array
     * @param errorCode the error code
     */
    public static void notEmpty(Object[] array, IResultCode errorCode) {
        if (ObjectUtils.isEmpty(array)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Not empty.
     *
     * @param array     the array
     * @param errorCode the error code
     */
    public static void notEmpty(Object[] array, IResultCode errorCode, Object... params) {
        if (ObjectUtils.isEmpty(array)) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * No null elements.
     *
     * @param array     the array
     * @param errorCode the error code
     */
    public static void noNullElements(Object[] array, IResultCode errorCode) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    AssertUtils.fail(errorCode);
                }
            }
        }
    }

    /**
     * No null elements.
     *
     * @param array     the array
     * @param errorCode the error code
     */
    public static void noNullElements(Object[] array, IResultCode errorCode, Object... params) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    AssertUtils.fail(errorCode, params);
                }
            }
        }
    }

    /**
     * Not empty.
     *
     * @param collection the collection
     * @param errorCode  the error code
     */
    public static void notEmpty(Collection<?> collection, IResultCode errorCode) {
        if (!CollectionUtils.isEmpty(collection)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Not empty.
     *
     * @param collection the collection
     * @param errorCode  the error code
     */
    public static void notEmpty(Collection<?> collection, IResultCode errorCode, Object... params) {
        if (!CollectionUtils.isEmpty(collection)) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Not empty.
     *
     * @param map       the map
     * @param errorCode the error code
     */
    public static void notEmpty(Map<?, ?> map, IResultCode errorCode) {
        if (ObjectUtils.isEmpty(map)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Not empty.
     *
     * @param map       the map
     * @param errorCode the error code
     */
    public static void notEmpty(Map<?, ?> map, IResultCode errorCode, Object... params) {
        if (ObjectUtils.isEmpty(map)) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Is instance of.
     *
     * @param type      the type
     * @param obj       the obj
     * @param errorCode the error code
     */
    public static void isInstanceOf(Class<?> type, Object obj, IResultCode errorCode) {
        AssertUtils.notNull(type, errorCode);
        if (!type.isInstance(obj)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Is instance of.
     *
     * @param type      the type
     * @param obj       the obj
     * @param errorCode the error code
     */
    public static void isInstanceOf(Class<?> type, Object obj, IResultCode errorCode, Object... params) {
        AssertUtils.notNull(type, errorCode);
        if (!type.isInstance(obj)) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Not null.
     *
     * @param errorCode the error codes
     * @param target    target
     */
    public static void notNull(Object target, IResultCode errorCode) {
        if (ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Not null.
     *
     * @param errorCode the error codes
     * @param target    target
     */
    public static void notNull(Object target, IResultCode errorCode, Object... params) {
        if (ObjectUtils.isEmpty(target)) {
            AssertUtils.fail(errorCode, params);
        }
    }

    /**
     * Is assignable.
     *
     * @param superType the super type
     * @param subType   the sub type
     * @param errorCode the error code
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, IResultCode errorCode) {
        AssertUtils.notNull(superType, errorCode);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            AssertUtils.fail(errorCode);
        }
    }

    /**
     * Is assignable.
     *
     * @param superType the super type
     * @param subType   the sub type
     * @param errorCode the error code
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, IResultCode errorCode, Object... params) {
        AssertUtils.notNull(superType, errorCode);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            AssertUtils.fail(errorCode, params);
        }
    }

}
