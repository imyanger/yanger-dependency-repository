package com.yanger.tools.general.support;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Objects;

/**
 * @Description Concurrent strong key:K -> soft value:V map
 * @Author yanger
 * @Date 2020/12/25 13:38
 */
@SuppressWarnings(value = {"checkstyle:EqualsHashCode", "checkstyle:ModifierOrder"})
public final class ConcurrentSoftValueHashMap<K, V> extends ConcurrentRefValueHashMap<K, V> {

    /**
     * Create value reference
     *
     * @param key   key
     * @param value value
     * @return the value reference
     */
    @Contract("_, _ -> new")
    @NotNull
    @Override
    ValueReference<K, V> createValueReference(@NotNull K key, @NotNull V value) {
        return new MySoftReference<>(key, value, this.myQueue);
    }

    private final static class MySoftReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {

        /** Key */
        private final K key;

        /**
         * My soft reference
         *
         * @param key      key
         * @param referent referent
         * @param q        q
         */
        private MySoftReference(@NotNull K key, @NotNull V referent, @NotNull ReferenceQueue<V> q) {
            super(referent, q);
            this.key = key;
        }

        /**
         * Gets key *
         *
         * @return the key
         */
        @Contract(pure = true)
        @NotNull
        @Override
        public K getKey() {
            return this.key;
        }

        /**
         * When referent is collected, equality should be identity-based (for the processQueues() remove this very same SoftValue)
         * otherwise it's just canonical equals on referents for replace(K,V,V) to work
         *
         * @param o o
         * @return the boolean
         */
        @Contract(value = "null -> false", pure = true)
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            //noinspection unchecked
            ValueReference<K, V> that = (ValueReference<K, V>) o;

            V v = this.get();
            V thatV = that.get();
            return this.key.equals(that.getKey()) && v != null && v.equals(thatV);
        }

        /**
         * Hash code
         *
         * @return the int
         */
        @Override
        public int hashCode() {
            return Objects.hash(this.key);
        }

    }

}