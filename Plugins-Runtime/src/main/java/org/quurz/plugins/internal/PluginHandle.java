package org.quurz.plugins.internal;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.plugins.data.PluginMetaData;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Encapsulates all resources and metadata associated with a loaded plugin.
 *     </p>
 *     <p>
 *         A {@code PluginHandle} holds the plugin's metadata, its dedicated class loader,
 *         and the factory used for instantiating it. This class is immutable.
 *     </p>
 * </div>
 *
 * @param <A> the type of the plugin contract
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class PluginHandle<A> {

    /**
     * <div>
     *     <p>
     *         Creates a new {@code PluginHandle} instance.
     *     </p>
     * </div>
     *
     * @param metaData    the plugin's metadata; must not be {@code null}
     * @param classLoader the class loader used for the plugin; must not be {@code null}
     * @param factory     the factory for creating plugin instances; must not be {@code null}
     * @param <A>         the type of the plugin contract
     * @return a new {@code PluginHandle} instance
     * @throws NullPointerException if any parameter is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> PluginHandle<A> pluginHandle(final @NonNull PluginMetaData metaData,
                                                   final @NonNull StreamClassLoader classLoader,
                                                   final @NonNull PluginFactory<A> factory) {
        Objects.requireNonNull(metaData, nullValue("metaData"));
        Objects.requireNonNull(classLoader, nullValue("classLoader"));
        Objects.requireNonNull(factory, nullValue("factory"));
        return new PluginHandle<>(metaData, classLoader, factory);
    }

    private final PluginMetaData metaData;
    private final StreamClassLoader classLoader;
    private final PluginFactory<A> factory;

    private PluginHandle(final PluginMetaData metaData,
                         final StreamClassLoader classLoader,
                         final PluginFactory<A> factory) {
        this.metaData
            = metaData;
        this.classLoader
            = classLoader;
        this.factory
            = factory;
    }

    /**
     * <div>
     *     <p>
     *         Returns the plugin's metadata.
     *     </p>
     * </div>
     *
     * @return the metadata
     *
     * @since 1.0.0
     */
    public @NonNull PluginMetaData getMetaData() {
        return this.metaData;
    }

    /**
     * <div>
     *     <p>
     *         Returns the class loader used for the plugin.
     *     </p>
     * </div>
     *
     * @return the class loader
     *
     * @since 1.0.0
     */
    public @NonNull StreamClassLoader getClassLoader() {
        return this.classLoader;
    }

    /**
     * <div>
     *     <p>
     *         Returns the factory for creating plugin instances.
     *     </p>
     * </div>
     *
     * @return the factory
     *
     * @since 1.0.0
     */
    public @NonNull PluginFactory<A> getFactory() {
        return this.factory;
    }

}
