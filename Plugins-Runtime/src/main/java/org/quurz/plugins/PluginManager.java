package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.InputStream;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         The central manager for handling the plugin lifecycle.
 *     </p>
 *     <p>
 *         The {@code PluginManager} uses a {@link PluginRepository} to manage plugin data
 *         and provides methods for installing, updating, and uninstalling plugins.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginManager
        implements PluginRepository {

    /**
     * <div>
     *     <p>
     *         Factory method for creating a new {@code PluginManager} with the given repository.
     *     </p>
     * </div>
     *
     * @param repository the repository to use for plugin data; must not be {@code null}
     * @return a new {@code PluginManager} instance
     * @throws NullPointerException if {@code repository} is {@code null}
     *
     * @since 1.0.0
     */
    public static PluginManager pluginManager(final @NonNull PluginRepository repository) {
        Objects.requireNonNull(repository, nullValue("repository"));
        return new PluginManager(repository);
    }

    private final PluginRepository repository;

    private PluginManager(final PluginRepository repository) {
        this.repository
            = repository;
    }

    @Override
    public void store(final @NonNull PluginId pluginId,
                      final @NonNull InputStream inputStream) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        Objects.requireNonNull(inputStream, nullValue("inputStream"));
        // TODO
    }

    @Override
    public InputStream retrieve(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        return null;    // TODO
    }


    @Override
    public void remove(@NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        // TODO
    }

}
