package com.yanger.tools.web.support;

import com.yanger.tools.web.exception.BasicException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Lambda 受检异常
 * @Author yanger
 * @Date 2020/12/18 14:37
 */
public class Try {

    /**
     * Of function.
     * @param <T>    the type parameter
     * @param <R>    the type parameter
     * @param mapper the mapper
     * @return the function
     * @Author yanger
     */
    @NotNull
    @Contract(pure = true)
    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Exception e) {
                throw new BasicException(e);
            }
        };
    }

    /**
     * The interface Unchecked function.
     * @param <T> the type parameter
     * @param <R> the type parameter
     * @date 2020.01.27 14:52
     * @Author yanger
     */
    @FunctionalInterface
    public interface UncheckedFunction<T, R> {

        /**
         * 调用
         * @param t t
         * @return R r
         * @throws Exception Exception
         * @Author yanger
         */
        R apply(T t) throws Exception;

    }

}
