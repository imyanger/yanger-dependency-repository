package com.yanger.tools.web.exception;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lambda 受检异常处理
 * @Author yanger
 * @Date 2020/12/29 19:26
 */
@UtilityClass
public class Unchecked {

    /**
     * unchecked function
     * @param <T>    the type parameter
     * @param <R>    the type parameter
     * @param mapper the mapper
     * @return the function
     */
    @NotNull
    @Contract(pure = true)
    public static <T, R> Function<T, R> function(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Throwable e) {
                throw Exceptions.unchecked(e);
            }
        };
    }

    /**
     * unchecked consumer
     * @param <T>    the type parameter
     * @param mapper the mapper
     * @return the consumer
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Consumer<T> consumer(Consumer<T> mapper) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                mapper.accept(t);
            } catch (Throwable e) {
                throw Exceptions.unchecked(e);
            }
        };
    }

    /**
     * unchecked supplier
     * @param <T>    the type parameter
     * @param mapper the mapper
     * @return the supplier
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Supplier<T> supplier(Supplier<T> mapper) {
        Objects.requireNonNull(mapper);
        return () -> {
            try {
                return mapper.get();
            } catch (Throwable e) {
                throw Exceptions.unchecked(e);
            }
        };
    }

    /**
     * unchecked runnable
     * @param runnable the runnable
     * @return the runnable
     */
    @NotNull
    @Contract(pure = true)
    public static Runnable runnable(Runnable runnable) {
        Objects.requireNonNull(runnable);
        return () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                throw Exceptions.unchecked(e);
            }
        };
    }

    /**
     * unchecked callable
     * @param <T>      the type parameter
     * @param callable the callable
     * @return the callable
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Callable<T> callable(Callable<T> callable) {
        Objects.requireNonNull(callable);
        return () -> {
            try {
                return callable.call();
            } catch (Throwable e) {
                throw Exceptions.unchecked(e);
            }
        };
    }

    /**
     * unchecked comparator
     * @param <T>        the type parameter
     * @param comparator the comparator
     * @return the comparator
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Comparator<T> comparator(Comparator<T> comparator) {
        Objects.requireNonNull(comparator);
        return (T o1, T o2) -> {
            try {
                return comparator.compare(o1, o2);
            } catch (Throwable e) {
                throw Exceptions.unchecked(e);
            }
        };
    }

}
