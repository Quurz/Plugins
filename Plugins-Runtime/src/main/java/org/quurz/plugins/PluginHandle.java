package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class PluginHandle {

    public static <A> PluginHandle pluginHandle(final @NonNull PluginId id,
                                                final @NonNull String description,
                                                final @NonNull Class<A> contract,
                                                final @NonNull Class<? extends A> implementation,
                                                final @NonNull ClassLoader classLoader) {
        Objects.requireNonNull(id, nullValue("id"));
        Objects.requireNonNull(contract, nullValue("contract"));
        Objects.requireNonNull(implementation, nullValue("implementation"));
        return new PluginHandle(id, description, contract, implementation, classLoader);
    }

    private final PluginId id;
    private final String description;
    private final Class<?> contract;
    private final Class<?> implementation;
    public final ClassLoader classLoader;

    private PluginHandle(final PluginId id,
                         final String description,
                         final Class<?> contract,
                         final Class<?> implementation,
                         final ClassLoader classLoader) {
        this.id
            = id;
        this.description
            = description;
        this.contract
            = contract;
        this.implementation
            = implementation;
        this.classLoader
            = classLoader;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PluginHandle.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("description='" + description + "'")
                .add("contract=" + contract)
                .add("implementation=" + implementation)
                .toString();
    }

}
