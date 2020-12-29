package com.yanger.tools.web.exception;

import com.yanger.tools.general.function.CheckedCallable;
import com.yanger.tools.general.function.CheckedComparator;
import com.yanger.tools.general.function.CheckedConsumer;
import com.yanger.tools.general.function.CheckedFunction;
import com.yanger.tools.general.function.CheckedRunnable;
import com.yanger.tools.general.function.CheckedSupplier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

/**
 * @Description Lambda 受检异常处理
 * @Author yanger
 * @Date 2020/12/29 19:26
 */
@UtilityClass
public class Unchecked {

    /**
     * Function function.
     *
     * @param <T>    the type parameter
     * @param <R>    the type parameter
     * @param mapper the mapper
     * @return the function
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static <T, R> Function<T, R> function(CheckedFunction<T, R> mapper) {
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
     * Consumer consumer.
     *
     * @param <T>    the type parameter
     * @param mapper the mapper
     * @return the consumer
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Consumer<T> consumer(CheckedConsumer<T> mapper) {
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
     * Supplier supplier.
     *
     * @param <T>    the type parameter
     * @param mapper the mapper
     * @return the supplier
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Supplier<T> supplier(CheckedSupplier<T> mapper) {
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
     * Runnable runnable.
     *
     * @param runnable the runnable
     * @return the runnable
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static Runnable runnable(CheckedRunnable runnable) {
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
     * Callable callable.
     *
     * @param <T>      the type parameter
     * @param callable the callable
     * @return the callable
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Callable<T> callable(CheckedCallable<T> callable) {
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
     * Comparator comparator.
     *
     * @param <T>        the type parameter
     * @param comparator the comparator
     * @return the comparator
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static <T> Comparator<T> comparator(CheckedComparator<T> comparator) {
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
