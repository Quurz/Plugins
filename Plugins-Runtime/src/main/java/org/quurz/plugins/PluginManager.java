package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.misc.LogAdapter;
import org.quurz.plugins.data.PluginId;
import org.quurz.plugins.data.PluginMetaData;
import org.quurz.plugins.data.PluginMetaDataValidator;
import org.quurz.plugins.events.PluginChangeEvent;
import org.quurz.plugins.events.PluginChangeEventListener;
import org.quurz.plugins.internal.PluginFactory;
import org.quurz.plugins.internal.PluginHandle;
import org.quurz.plugins.internal.StreamClassLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.plugins.localisation.PluginsMessages.pluginManagerAlreadyInitialised;
import static org.quurz.plugins.localisation.PluginsMessages.pluginManagerNotInitialised;

/**
 * <div>
 *     <p>
 *         Central manager for the plugin system.
 *     </p>
 *     <p>
 *         The {@code PluginManager} is responsible for coordinating the lifecycle of plugins,
 *         including installation, updating, uninstallation, and instantiation.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class PluginManager {

    private static PluginManager INSTANCE;

    /**
     * <div>
     *     <p>
     *         Creates a new {@code PluginManager} instance with the specified repository.
     *     </p>
     * </div>
     *
     * @param repository the repository to use for storing and retrieving plugins; must not be {@code null}
     * @return a new {@code PluginManager} instance
     * @throws NullPointerException if {@code repository} is {@code null}
     *
     * @since 1.0.0
     */
    public static PluginManager pluginManager(final @NonNull PluginRepository repository) {
        Objects.requireNonNull(repository, nullValue("repository"));
        return pluginManager(repository, LogAdapter.noOpLogAdapter());
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code PluginManager} instance with the specified repository and log adapter.
     *     </p>
     * </div>
     *
     * @param repository the repository to use for storing and retrieving plugins; must not be {@code null}
     * @param logAdapter the adapter to use for logging; must not be {@code null}
     * @return a new {@code PluginManager} instance
     * @throws NullPointerException if {@code repository} or {@code logAdapter} is {@code null}
     * @throws IllegalStateException if the {@code PluginManager} has already been initialised
     *
     * @since 1.0.0
     */
    public static PluginManager pluginManager(final @NonNull PluginRepository repository,
                                              final @NonNull LogAdapter logAdapter) {
        if (INSTANCE == null) {
            Objects.requireNonNull(repository, nullValue("repository"));
            Objects.requireNonNull(logAdapter, nullValue("logAdapter"));
            return (INSTANCE = new PluginManager(logAdapter, repository));
        } else {
            throw new IllegalStateException(pluginManagerAlreadyInitialised());
        }
    }

    /**
     * <div>
     *     <p>
     *         Returns the singleton instance of the {@code PluginManager}.
     *     </p>
     * </div>
     *
     * @return the singleton instance
     * @throws IllegalStateException if the {@code PluginManager} has not been initialised
     *
     * @since 1.0.0
     */
    public static PluginManager instance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(pluginManagerNotInitialised());
        }
        return INSTANCE;
    }

    private final LogAdapter logAdapter;
    private final PluginRepository repository;
    private final Map<PluginIdentifier, Set<PluginChangeEventListener>> idsToListeners;
    private final Map<PluginId, PluginHandle<?>> idsToHandles;

    /**
     * <div>
     *     <p>
     *         Private constructor for the {@code PluginManager}.
     *     </p>
     * </div>
     *
     * @param logAdapter the adapter to use for logging; must not be {@code null}
     * @param repository the repository to use for storing and retrieving plugins; must not be {@code null}
     *
     * @since 1.0.0
     */
    private PluginManager(final LogAdapter logAdapter,
                          final PluginRepository repository) {
        this.logAdapter
            = logAdapter;
        this.repository
            = repository;
        this.idsToListeners
            = new ConcurrentHashMap<>();
        this.idsToHandles
            = new ConcurrentHashMap<>();
    }

    /**
     * <div>
     *     <p>
     *         Returns a {@link PluginConstructor} for the specified plugin.
     *     </p>
     *     <p>
     *         The constructor is cached and can be used to create one or more instances
     *         of the plugin that implement the contract {@code A}.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @param <A>      the type of the plugin contract
     * @return a {@link PluginConstructor} instance
     * @throws NullPointerException if {@code pluginId} is {@code null}
     * @throws RuntimeException     if the plugin cannot be loaded or initialized
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <A> PluginConstructor<A> constructor(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));

        return (PluginConstructor<A>) this.idsToHandles.computeIfAbsent(pluginId, id -> {
            try {
                final var jarSupplier
                    = repository.retrieve(id);
                final var metaData
                    = discoverMetaData(jarSupplier);
                final var loader
                    = new StreamClassLoader(jarSupplier.get(), PluginManager.class.getClassLoader());

                final Class<A> contract
                    = (Class<A>) loader.loadClass(metaData.getContract());
                final Class<? extends A> implementation
                    = (Class<? extends A>) loader.loadClass(metaData.getImplementation());

                final String proxyName = "org.quurz.plugins.internal.Proxy_" + id.getGroup().replace('.', '_') + "_" + id.getName() + "_" + id.getVersion().toString().replace('.', '_');
                final var factory = PluginFactory.pluginFactory(contract, implementation, proxyName, loader);

                return PluginHandle.pluginHandle(metaData, loader, factory);
            } catch (final Exception exception) {
                throw new RuntimeException(exception);
            }
        }).getFactory();
    }

    /**
     * <div>
     *     <p>
     *         Returns a {@link PluginConstructor} for the specified plugin and registers
     *         a listener for change events of that plugin.
     *     </p>
     *     <p>
     *         The listener is registered before the constructor is returned, ensuring
     *         it receives all subsequent lifecycle events for the plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @param listener the listener to register; must not be {@code null}
     * @param <A>      the type of the plugin contract
     * @return a {@link PluginConstructor} instance
     * @throws NullPointerException if {@code pluginId} or {@code listener} is {@code null}
     * @throws RuntimeException     if the plugin cannot be loaded or initialized
     *
     * @since 1.0.0
     */
    public <A> PluginConstructor<A> constructor(final @NonNull PluginId pluginId,
                                                final @NonNull PluginChangeEventListener listener) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        Objects.requireNonNull(listener, nullValue("listener"));

        registerListener(pluginId, listener);

        return constructor(pluginId);
    }

    /**
     * <div>
     *     <p>
     *         Registers a listener for change events of the specified plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the identifier of the plugin to listen to; must not be {@code null}
     * @param listener the listener to register; must not be {@code null}
     * @throws NullPointerException if {@code pluginId} or {@code listener} is {@code null}
     *
     * @since 1.0.0
     */
    public void registerListener(final @NonNull PluginId pluginId,
                                 final @NonNull PluginChangeEventListener listener) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        Objects.requireNonNull(listener, nullValue("listener"));

        this.idsToListeners.computeIfAbsent(PluginIdentifier.pluginIdentifier(pluginId), key -> ConcurrentHashMap.newKeySet()).add(listener);
    }

    /**
     * <div>
     *     <p>
     *         Unregisters a listener from change events of the specified plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the identifier of the plugin; must not be {@code null}
     * @param listener the listener to unregister; must not be {@code null}
     * @throws NullPointerException if {@code pluginId} or {@code listener} is {@code null}
     *
     * @since 1.0.0
     */
    public void unregisterListener(final @NonNull PluginId pluginId,
                                   final @NonNull PluginChangeEventListener listener) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        Objects.requireNonNull(listener, nullValue("listener"));

        Optional.ofNullable(this.idsToListeners.get(PluginIdentifier.pluginIdentifier(pluginId)))
                .ifPresent(set -> set.remove(listener));
    }

    /**
     * <div>
     *     <p>
     *         Fires a change announcement event for the specified plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the identifier of the plugin
     *
     * @since 1.0.0
     */
    private void firePluginChangeAnnouncementEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(PluginIdentifier.pluginIdentifier(pluginId)))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginChangeAnnouncementEvent(pluginId);
                    set.forEach(listener -> listener.onPluginChangeAnnouncementEvent(event));
                });
    }

    /**
     * <div>
     *     <p>
     *         Fires an installation event for the specified plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the identifier of the plugin
     *
     * @since 1.0.0
     */
    private void firePluginInstallEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(PluginIdentifier.pluginIdentifier(pluginId)))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginInstallEvent(pluginId);
                    set.forEach(listener -> listener.onPluginInstallEvent(event));
                });
    }

    /**
     * <div>
     *     <p>
     *         Fires an update event for the specified plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the identifier of the plugin
     *
     * @since 1.0.0
     */
    private void firePluginUpdateEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(PluginIdentifier.pluginIdentifier(pluginId)))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginUpdateEvent(pluginId);
                    set.forEach(listener -> listener.onPluginUpdateEvent(event));
                });
    }

    /**
     * <div>
     *     <p>
     *         Fires an uninstallation event for the specified plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the identifier of the plugin
     *
     * @since 1.0.0
     */
    private void firePluginUninstallEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(PluginIdentifier.pluginIdentifier(pluginId)))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginUninstallEvent(pluginId);
                    set.forEach(listener -> listener.onPluginUninstallEvent(event));
                });
    }

    /**
     * <div>
     *     <p>
     *         Internal record to uniquely identify a plugin by its group and name,
     *         ignoring the version.
     *     </p>
     *     <p>
     *         This is used for registering listeners that should receive events
     *         for all versions of a plugin.
     *     </p>
     * </div>
     *
     * @param group the group of the plugin
     * @param name  the name of the plugin
     *
     * @since 1.0.0
     */
    private record PluginIdentifier(String group,
                                    String name) {
        /**
         * <div>
         *     <p>
         *         Creates a {@code PluginIdentifier} from a {@link PluginId}.
         *     </p>
         * </div>
         *
         * @param pluginId the plugin identifier to extract the identity from
         * @return a new {@code PluginIdentifier}
         *
         * @since 1.0.0
         */
        static PluginIdentifier pluginIdentifier(final @NonNull PluginId pluginId) {
            return new PluginIdentifier(pluginId.getGroup(), pluginId.getName());
        }
    }

    /**
     * <div>
     *     <p>
     *         Discovers and validates the metadata within a plugin JAR.
     *     </p>
     * </div>
     *
     * @param jarSupplier a supplier providing an {@link InputStream} to the JAR
     * @return the extracted {@link PluginMetaData}
     * @throws IOException if the metadata cannot be found or is invalid
     *
     * @since 1.0.0
     */
    private PluginMetaData discoverMetaData(final Supplier<InputStream> jarSupplier) throws IOException {
        try (final var is = jarSupplier.get();
             final var jis = new JarInputStream(is)) {
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                if ("plugin.json".equals(entry.getName())) {
                    final var result = PluginMetaDataValidator.loadAndValidatePluginMetadata(jis);
                    if (result.isSuccess()) {
                        final var either = result.get();
                        if (either.isRight()) {
                            return either.get();
                        } else {
                            throw new IOException("Validation failed: " + either.getLeft());
                        }
                    } else {
                        throw new IOException("Failed to read metadata", result.getException());
                    }
                }
            }
        }
        throw new IOException("Metadata not found in JAR");
    }

}
