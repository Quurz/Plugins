package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.plugins.data.PluginId;

import java.io.InputStream;
import java.util.Set;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Abstraction of a storage for plugin data.
 *     </p>
 *     <p>
 *         A repository handles the persistence of plugin binaries and provides
 *         access to their contents via {@link InputStream} suppliers.
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
     *         Stores a plugin's data in the repository.
     *     </p>
     * </div>
     *
     * @param pluginId            the unique identifier of the plugin; must not be {@code null}
     * @param inputStreamSupplier the {@link Supplier} providing the JAR content as an {@link InputStream};
     *                            must not be {@code null}
     * @throws PluginRepositoryException if the plugin could not be stored
     * @throws NullPointerException      if {@code pluginId} or {@code inputStreamSupplier} is {@code null}
     *
     * @since 1.0.0
     */
    void store(final @NonNull PluginId pluginId,
               final @NonNull Supplier<InputStream> inputStreamSupplier) throws PluginRepositoryException;

    /**
     * <div>
     *     <p>
     *         Retrieves a plugin's data from the repository.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return a {@link Supplier} for the plugin's JAR as an {@link InputStream}
     * @throws PluginRepositoryException if the plugin could not be retrieved
     * @throws NullPointerException      if {@code pluginId} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull Supplier<InputStream> retrieve(final @NonNull PluginId pluginId) throws PluginRepositoryException;

    /**
     * <div>
     *     <p>
     *         Checks if a plugin with the specified ID is stored in the repository.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return {@code true} if the plugin is present, {@code false} otherwise
     * @throws NullPointerException if {@code pluginId} is {@code null}
     *
     * @since 1.0.0
     */
    boolean contains(final @NonNull PluginId pluginId);

    /**
     * <div>
     *     <p>
     *         Removes a plugin from the repository.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @throws PluginRepositoryException if the plugin could not be removed
     * @throws NullPointerException      if {@code pluginId} is {@code null}
     *
     * @since 1.0.0
     */
    void remove(final @NonNull PluginId pluginId) throws PluginRepositoryException;

    /**
     * <div>
     *     <p>
     *         Returns a set of all plugin identifiers currently stored in the repository.
     *     </p>
     * </div>
     *
     * @return an unmodifiable set of {@link PluginId}s
     *
     * @since 1.0.0
     */
    @NonNull Set<PluginId> plugins();

}
