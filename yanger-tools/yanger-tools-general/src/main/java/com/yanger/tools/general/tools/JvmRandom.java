package com.yanger.tools.general.tools;

import org.jetbrains.annotations.Contract;

import java.util.Random;

/**
 * JVMRandom是一个包装器, 它通过数学随机方法及其系统范围内的随机对象.
 * 这样做是为了允许在类的所有成员之间共享种子的随机类-更好的名称应该是shared seed Random.
 * 当前的实现覆盖了方法 {@link Random#nextInt(int)} 和 {@link Random#nextLong()} 生成从 0（包含）到最大值（不包含）的正数.
 *
 * @Author yanger
 * @Date 2021/1/28 18:54
 */
public final class JvmRandom extends Random {

    private static final long serialVersionUID = 1L;

    /** SHARED_RANDOM */
    private static final Random SHARED_RANDOM = new Random();

    /** Constructed */
    private final boolean constructed;

    /**
     * Constructs a new instance.
     */
    public JvmRandom() {
        this.constructed = true;
    }

    /**
     * Unsupported in 2.0.
     *
     * @param seed ignored
     */
    @Contract(pure = true)
    @Override
    public synchronized void setSeed(long seed) {
        if (this.constructed) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Unsupported in 2.0.
     *
     * @param byteArray ignored
     */
    @Contract("_ -> fail")
    @Override
    public void nextBytes(byte[] byteArray) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed int value from the Math.random() sequence.</p> Identical
     * to <code>nextInt(Integer.MAX_VALUE)</code> <p> N.B. All values are >= 0. </p>
     *
     * @return the random int
     */
    @Override
    public int nextInt() {
        return this.nextInt(Integer.MAX_VALUE);
    }

    /**
     * <p>Returns a pseudorandom, uniformly distributed int value between <code>0</code> (inclusive) and the specified
     * value (exclusive), from the Math.random() sequence.</p>
     *
     * @param n the specified exclusive max-value
     * @return the random int
     * @throws IllegalArgumentException when <code>n &lt;= 0</code>
     */
    @Override
    public int nextInt(int n) {
        return SHARED_RANDOM.nextInt(n);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed long value from the Math.random() sequence.</p> Identical
     * to <code>nextLong(Long.MAX_VALUE)</code> <p> N.B. All values are >= 0. </p>
     *
     * @return the random long
     */
    @Override
    public long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    /**
     * <p>Returns a pseudorandom, uniformly distributed long value between <code>0</code> (inclusive) and the specified
     * value (exclusive), from the Math.random() sequence.</p>
     *
     * @param n the specified exclusive max-value
     * @return the random long
     * @throws IllegalArgumentException when <code>n &lt;= 0</code>
     */
    public static long nextLong(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                "Upper bound for nextInt must be positive"
            );
        }
        // Code adapted from Harmony Random#nextInt(int)
        // n is power of 2
        if ((n & -n) == n) {
            // dropping lower order bits improves behaviour for low values of n
            return next63bits() >> 63 - bitsRequired(n - 1);
        }
        // Not a power of two
        long val;
        long bits;
        // reject some values to improve distribution
        do {
            bits = next63bits();
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }

    /**
     * Get the next unsigned random long
     *
     * @return unsigned random long
     */
    private static long next63bits() {
        // drop the sign bit to leave 63 random bits
        return SHARED_RANDOM.nextLong() & 0x7fffffffffffffffL;
    }

    /**
     * Count the number of bits required to represent a long number.
     *
     * @param num long number
     * @return number of bits required
     */
    @Contract(pure = true)
    private static int bitsRequired(long num) {
        // Derived from Hacker's Delight, Figure 5-9
        long y = num;
        int n = 0;
        while (true) {
            // 64 = number of bits in a long
            if (num < 0) {
                return 64 - n;
            }
            if (y == 0) {
                return n;
            }
            n++;
            num = num << 1;
            y = y >> 1;
        }
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed boolean value from the Math.random() sequence.</p>
     *
     * @return the random boolean
     */
    @Override
    public boolean nextBoolean() {
        return SHARED_RANDOM.nextBoolean();
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed float value between <code>0.0</code> and <code>1.0</code>
     * from the Math.random() sequence.</p>
     *
     * @return the random float
     */
    @Override
    public float nextFloat() {
        return SHARED_RANDOM.nextFloat();
    }

    /**
     * <p>Synonymous to the Math.random() call.</p>
     *
     * @return the random double
     */
    @Override
    public double nextDouble() {
        return SHARED_RANDOM.nextDouble();
    }

    /**
     * Next gaussian double
     *
     * @return the double
     */
    @Contract(" -> fail")
    @Override
    public synchronized double nextGaussian() {
        throw new UnsupportedOperationException();
    }

}
