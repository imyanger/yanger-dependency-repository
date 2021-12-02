package com.yanger.tools.web.exception;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

import lombok.SneakyThrows;

/**
 * 业务断言
 * @Author yanger
 * @Date 2020/12/18 10:34
 */
public abstract class Asserts {

    /**
     * Not blank
     *
     * @param content content
     * @param msg     msg
     */
    public static void notBlank(String content, @NotNull String msg) {
        hasText(content, msg);
    }

    /**
     * Not blank
     *
     * @param content           content
     * @param exceptionSupplier exception supplier
     */
    public static void notBlank(@Nullable String content, Supplier<? extends BasicException> exceptionSupplier) {
        if (StringUtils.isBlank(content)) {
            Asserts.fail(exceptionSupplier);
        }
    }

    /**
     * Fail
     *
     * @param exceptionSupplier exception supplier
     */
    @Contract("_ -> fail")
    @SneakyThrows
    public static void fail(Supplier<? extends BasicException> exceptionSupplier) {
        throw nullSafeGetException(exceptionSupplier);
    }

    /**
     * Null safe get exception
     *
     * @param messageSupplier message supplier
     * @return the exception
     */
    @Contract("null -> new")
    private static Exception nullSafeGetException(@Nullable Supplier<? extends BasicException> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : new BasicException());
    }

    /**
     * Fail
     *
     * @param condition condition
     * @param message   message
     */
    public static void fail(boolean condition, String message) {
        if (condition) {
            Asserts.fail(message);
        }
    }

    /**
     * Fail
     *
     * @param message message
     */
    @Contract("_ -> fail")
    public static void fail(String message) {
        throw new BasicException(message);
    }

    /**
     * Fail
     *
     * @param code    code
     * @param message message
     */
    @Contract("_, _ -> fail")
    public static void fail(String code, String message) {
        throw BasicException.of(code, message);
    }

    /**
     * Not empty
     *
     * @param array array
     * @param msg   msg
     */
    public static void notEmpty(byte[] array, @NotNull String msg) {
        if (ObjectUtils.isEmpty(array)) {
            Asserts.fail(msg);
        }
    }

    /**
     * Not null s
     *
     * @param object            object
     * @param exceptionSupplier exception supplier
     */
    public static void notNullS(@Nullable Object object, Supplier<? extends BasicException> exceptionSupplier) {
        if (object == null) {
            Asserts.fail(exceptionSupplier);
        }
    }

    /**
     * Not empty s
     *
     * @param collection        collection
     * @param exceptionSupplier exception supplier
     */
    public static void notEmptyS(@Nullable Collection<?> collection, Supplier<? extends BasicException> exceptionSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            Asserts.fail(exceptionSupplier);
        }
    }

    /**
     * State
     *
     * @param expression expression
     * @param message    message
     */
    @Contract("false, _ -> fail")
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new BasicException(message);
        }
    }

    /**
     * State
     *
     * @param expression      expression
     * @param messageSupplier message supplier
     */
    @Contract("false, _ -> fail")
    public static void state(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Is true
     *
     * @param expression expression
     * @param message    message
     */
    @Contract("false, _ -> fail")
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BasicException(message);
        }
    }

    /**
     * Is true
     *
     * @param expression      expression
     * @param messageSupplier message supplier
     */
    @Contract("false, _ -> fail")
    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Is true s
     *
     * @param expression        expression
     * @param exceptionSupplier exception supplier
     */
    public static void isTrueS(boolean expression, Supplier<? extends BasicException> exceptionSupplier) {
        if (!expression) {
            Asserts.fail(exceptionSupplier);
        }
    }

    /**
     * Is null
     *
     * @param object  object
     * @param message message
     */
    @Contract("!null, _ -> fail")
    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new BasicException(message);
        }
    }

    /**
     * Is null
     *
     * @param object          object
     * @param messageSupplier message supplier
     */
    @Contract("!null, _ -> fail")
    public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Not null
     *
     * @param object  object
     * @param message message
     */
    @Contract("null, _ -> fail")
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new BasicException(message);
        }
    }

    /**
     * Not null
     *
     * @param object          object
     * @param messageSupplier message supplier
     */
    @Contract("null, _ -> fail")
    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Has length
     *
     * @param text    text
     * @param message message
     */
    public static void isNotBlank(@Nullable String text, String message) {
        if (!StringUtils.isNotBlank(text)) {
            throw new BasicException(message);
        }
    }

    /**
     * Has length
     *
     * @param text            text
     * @param messageSupplier message supplier
     */
    public static void isNotBlank(@Nullable String text, Supplier<String> messageSupplier) {
        if (!StringUtils.isNotBlank(text)) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Has text
     *
     * @param text    text
     * @param message message
     */
    public static void hasText(@Nullable String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new BasicException(message);
        }
    }

    /**
     * Has text
     *
     * @param text            text
     * @param messageSupplier message supplier
     */
    public static void hasText(@Nullable String text, Supplier<String> messageSupplier) {
        if (StringUtils.isBlank(text)) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Does not contain
     *
     * @param textToSearch text to search
     * @param substring    substring
     * @param message      message
     */
    public static void doesNotContain(@Nullable String textToSearch, String substring, String message) {
        if (StringUtils.isNotBlank(textToSearch)
            && StringUtils.isNotBlank(substring)
            && textToSearch.contains(substring)) {
            throw new BasicException(message);
        }
    }

    /**
     * Does not contain
     *
     * @param textToSearch    text to search
     * @param substring       substring
     * @param messageSupplier message supplier
     */
    public static void doesNotContain(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.isNotBlank(textToSearch)
            && StringUtils.isNotBlank(substring)
            && textToSearch.contains(substring)) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Not empty
     *
     * @param array   array
     * @param message message
     */
    public static void notEmpty(@Nullable Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new BasicException(message);
        }
    }

    /**
     * Not empty
     *
     * @param array           array
     * @param messageSupplier message supplier
     */
    public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (ObjectUtils.isEmpty(array)) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * No null elements
     *
     * @param array   array
     * @param message message
     */
    public static void noNullElements(@Nullable Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new BasicException(message);
                }
            }
        }
    }

    /**
     * No null elements
     *
     * @param array           array
     * @param messageSupplier message supplier
     */
    public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new BasicException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    /**
     * Not empty
     *
     * @param collection collection
     * @param message    message
     */
    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BasicException(message);
        }
    }

    /**
     * Not empty
     *
     * @param collection      collection
     * @param messageSupplier message supplier
     */
    public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * No null elements
     *
     * @param collection collection
     * @param message    message
     */
    public static void noNullElements(@Nullable Collection<?> collection, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new BasicException(message);
                }
            }
        }
    }

    /**
     * No null elements
     *
     * @param collection      collection
     * @param messageSupplier message supplier
     */
    public static void noNullElements(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new BasicException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    /**
     * Not empty
     *
     * @param map     map
     * @param message message
     */
    public static void notEmpty(@Nullable Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(Collections.singleton(map))) {
            throw new BasicException(message);
        }
    }

    /**
     * Not empty
     *
     * @param map             map
     * @param messageSupplier message supplier
     */
    public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(Collections.singleton(map))) {
            throw new BasicException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Is instance of
     *
     * @param type    type
     * @param obj     obj
     * @param message message
     */
    public static void isInstanceOf(Class<?> type, @Nullable Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    /**
     * Is instance of
     *
     * @param type            type
     * @param obj             obj
     * @param messageSupplier message supplier
     */
    public static void isInstanceOf(Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
        }
    }

    /**
     * Is instance of
     *
     * @param type type
     * @param obj  obj
     */
    public static void isInstanceOf(Class<?> type, @Nullable Object obj) {
        isInstanceOf(type, obj, "");
    }

    /**
     * Is assignable
     *
     * @param superType super type
     * @param subType   sub type
     * @param message   message
     */
    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, String message) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, message);
        }
    }

    /**
     * Is assignable
     *
     * @param superType       super type
     * @param subType         sub type
     * @param messageSupplier message supplier
     */
    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
        }
    }

    /**
     * Is assignable
     *
     * @param superType super type
     * @param subType   sub type
     */
    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, "");
    }


    /**
     * Instance check failed
     *
     * @param type type
     * @param obj  obj
     * @param msg  msg
     */
    private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable String msg) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.isNotBlank(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new BasicException(result);
    }

    /**
     * Assignable check failed
     *
     * @param superType super type
     * @param subType   sub type
     * @param msg       msg
     */
    private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable String msg) {
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.isNotBlank(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " is not assignable to " + superType);
        }
        throw new BasicException(result);
    }

    /**
     * Ends with separator
     *
     * @param msg msg
     * @return the boolean
     */
    private static boolean endsWithSeparator(@NotNull String msg) {
        return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
    }

    /**
     * Message with type name
     *
     * @param msg      msg
     * @param typeName type name
     * @return the string
     */
    @Contract(pure = true)
    private static @NotNull String messageWithTypeName(@NotNull String msg, @Nullable Object typeName) {
        return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
    }

    /**
     * Null safe get
     *
     * @param messageSupplier message supplier
     * @return the string
     */
    @Contract("null -> null")
    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

}
