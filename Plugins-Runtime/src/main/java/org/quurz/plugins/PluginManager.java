package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.misc.LogAdapter;
import org.quurz.plugins.data.PluginId;
import org.quurz.plugins.events.PluginChangeEvent;
import org.quurz.plugins.events.PluginChangeEventListener;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<PluginId, Set<PluginChangeEventListener>> idsToListeners;

    private PluginManager(final LogAdapter logAdapter,
                          final PluginRepository repository) {
        this.logAdapter
            = logAdapter;
        this.repository
            = repository;
        this.idsToListeners
            = new ConcurrentHashMap<>();
    }

    /**
     * <div>
     *     <p>
     *         Returns a {@link PluginConstructor} for the specified plugin.
     *     </p>
     *     <p>
     *         The constructor can be used to create one or more instances of the plugin
     *         that implement the contract {@code A}.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @param <A>      the type of the plugin contract
     * @return a {@link PluginConstructor} instance
     * @throws NullPointerException if {@code pluginId} is {@code null}
     *
     * @since 1.0.0
     */
    public <A> PluginConstructor<A> constructor(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        return null;    // TODO
    }

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

        this.idsToListeners.computeIfAbsent(pluginId, key -> ConcurrentHashMap.newKeySet()).add(listener);
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

        Optional.ofNullable(this.idsToListeners.get(pluginId))
                .ifPresent(set -> set.remove(listener));
    }

    private void firePluginChangeAnnouncementEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(pluginId))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginChangeAnnouncementEvent(pluginId);
                    set.forEach(listener -> listener.onPluginChangeAnnouncementEvent(event));
                });
    }

    private void firePluginInstallEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(pluginId))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginInstallEvent(pluginId);
                    set.forEach(listener -> listener.onPluginInstallEvent(event));
                });
    }

    private void firePluginUpdateEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(pluginId))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginUpdateEvent(pluginId);
                    set.forEach(listener -> listener.onPluginUpdateEvent(event));
                });
    }

    private void firePluginUninstallEvent(final PluginId pluginId) {
        Optional.ofNullable(this.idsToListeners.get(pluginId))
                .ifPresent(set -> {
                    final var event = PluginChangeEvent.pluginUninstallEvent(pluginId);
                    set.forEach(listener -> listener.onPluginUninstallEvent(event));
                });
    }

}
