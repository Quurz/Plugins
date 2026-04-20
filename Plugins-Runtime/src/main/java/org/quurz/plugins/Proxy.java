package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A generic proxy base class that manages the underlying implementation of a plugin.
 *     </p>
 *     <p>
 *         This class provides thread-safe access to the implementation instance using a
 *         {@link ReadWriteLock}. It is designed to be subclassed by dynamic proxies
 *         (e.g., generated via ByteBuddy) to provide a transparent, thread-safe wrapper
 *         around plugin implementations.
 *     </p>
 * </div>
 *
 * @param <A> the type of the plugin implementation
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class Proxy<A> {

    /**
     * <div>
     *     <p>
     *         The actual plugin implementation instance.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    private A $__implementation;

    /**
     * <div>
     *     <p>
     *         The lock used to synchronize access to the implementation.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    final ReadWriteLock $__access_lock
        = new ReentrantReadWriteLock(true);

    /**
     * <div>
     *     <p>
     *         Sets the plugin implementation.
     *     </p>
     *     <p>
     *         This method acquires a write lock before updating the implementation.
     *     </p>
     * </div>
     *
     * @param implementation the new implementation instance; must not be {@code null}
     * @throws NullPointerException if {@code implementation} is {@code null}
     *
     * @since 1.0.0
     */
    void $__set_implementation(final @NonNull A implementation) {
        Objects.requireNonNull(implementation, nullValue("implementation"));
        $__access_lock.writeLock().lock();
        try {
            $__implementation
                = implementation;
        } finally {
            $__access_lock.writeLock().unlock();
        }
    }

    /**
     * <div>
     *     <p>
     *         Returns the current plugin implementation.
     *     </p>
     *     <p>
     *         This method acquires a read lock before accessing the implementation.
     *     </p>
     * </div>
     *
     * @return the current implementation instance
     *
     * @since 1.0.0
     */
    A $__get_implementation() {
        $__access_lock.readLock().lock();
        try {
            return this.$__implementation;
        } finally {
            $__access_lock.readLock().unlock();
        }
    }

    /**
     * <div>
     *     <p>
     *         Replaces the current implementation with a new one and returns the old one.
     *     </p>
     *     <p>
     *         This method acquires a write lock during the replacement process.
     *     </p>
     * </div>
     *
     * @param implementation the new implementation instance; must not be {@code null}
     * @return the previous implementation instance
     * @throws NullPointerException if {@code implementation} is {@code null}
     *
     * @since 1.0.0
     */
    A $__replace_implementation(final @NonNull A implementation) {
        Objects.requireNonNull(implementation, nullValue("implementation"));
        $__access_lock.writeLock().lock();
        try {
            final var oldPlugin
                = this.$__implementation;
            this.$__implementation
                = implementation;
            return oldPlugin;
        } finally {
            $__access_lock.writeLock().unlock();
        }
    }

}
