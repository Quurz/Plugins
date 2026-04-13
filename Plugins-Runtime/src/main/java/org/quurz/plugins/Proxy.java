package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class Proxy<A> {

    private A $__implementation;
    final ReadWriteLock $__access_lock
        = new ReentrantReadWriteLock(true);

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

    A $__get_implementation() {
        $__access_lock.readLock().lock();
        try {
            return this.$__implementation;
        } finally {
            $__access_lock.readLock().unlock();
        }
    }

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
