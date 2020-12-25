package com.yanger.general.function;

/**
 * @Description 受检的 Comparator
 * @Author yanger
 * @Date 2020/12/25 9:55
 */
@FunctionalInterface
public interface CheckedComparator<T> {

    /**
     * Compares its two arguments for order.
     *
     * @param o1 o1
     * @param o2 o2
     * @return int int
     * @throws Throwable CheckedException
     */
    int compare(T o1, T o2) throws Throwable;

}
