package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.InputStream;

/**
 * <div>
 *     <p>
 *         A repository for storing and retrieving plugin binary data.
 *     </p>
 *     <p>
 *         The repository manages the physical storage of plugin files (e.g., JARs)
 *         and associates them with their unique {@link PluginId}.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface PluginRepository {

    /**
     * <div>
     *     <p>
     *         Stores the plugin data from the provided input stream.
     *     </p>
     * </div>
     *
     * @param pluginId    the unique identifier of the plugin; must not be {@code null}
     * @param inputStream the stream containing the plugin binary data; must not be {@code null}
     * @throws NullPointerException      if {@code pluginId} or {@code inputStream} is {@code null}
     * @throws PluginRepositoryException if storing the plugin data fails
     *
     * @since 1.0.0
     */
    void store(final @NonNull PluginId pluginId,
               final @NonNull InputStream inputStream);

    /**
     * <div>
     *     <p>
     *         Retrieves the plugin data for the given identifier.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin to retrieve; must not be {@code null}
     * @return an {@link InputStream} for reading the plugin data
     * @throws NullPointerException      if {@code pluginId} is {@code null}
     * @throws PluginRepositoryException if retrieving the plugin data fails
     *
     * @since 1.0.0
     */
    @NonNull InputStream retrieve(final @NonNull PluginId pluginId);

    /**
     * <div>
     *     <p>
     *         Removes the plugin data associated with the given identifier from the repository.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin to remove; must not be {@code null}
     * @throws NullPointerException      if {@code pluginId} is {@code null}
     * @throws PluginRepositoryException if removing the plugin data fails
     *
     * @since 1.0.0
     */
    void remove(final @NonNull PluginId pluginId);

}
