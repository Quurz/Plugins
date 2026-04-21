package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Util.*;
import static org.quurz.plugins.localisation.PluginsMessages.*;

public class FileBasedPluginRepository
        implements PluginRepository {

    /**
     * <div>
     *     <p>
     *         Creates a new {@code FileBasedPluginRepository} with the specified base directory path.
     *     </p>
     * </div>
     *
     * @param baseDirectory the path to the base directory where plugins will be stored; must not be {@code null}
     * @return a new {@code PluginRepository} instance
     * @throws NullPointerException      if {@code baseDirectory} is {@code null}
     * @throws PluginRepositoryException if the base directory cannot be created or accessed
     *
     * @since 1.0.0
     */
    public static PluginRepository fileBasedPluginRepository(final @NonNull String baseDirectory) {
        Objects.requireNonNull(baseDirectory, nullValue("baseDirectory"));

        final var baseDirectoryPath
            = Path.of(baseDirectory);

        ensureBaseDirectoryExists(baseDirectoryPath);

        requireDirectory(baseDirectoryPath, () -> new IllegalArgumentException(notADirectory("baseDirectory")));
        requireReadable(baseDirectoryPath, () -> new IllegalArgumentException(notReadable("baseDirectory")));
        requireWriteable(baseDirectoryPath, () -> new IllegalArgumentException(notWritable("baseDirectory")));

        return new FileBasedPluginRepository(baseDirectoryPath);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code FileBasedPluginRepository} with the specified base directory path.
     *     </p>
     * </div>
     *
     * @param baseDirectory the base directory where plugins will be stored; must not be {@code null}
     * @return a new {@code PluginRepository} instance
     * @throws NullPointerException      if {@code baseDirectory} is {@code null}
     * @throws PluginRepositoryException if the base directory cannot be created or accessed
     *
     * @since 1.0.0
     */
    public static PluginRepository fileBasedPluginRepository(final @NonNull Path baseDirectory) {
        Objects.requireNonNull(baseDirectory, nullValue("baseDirectory"));

        ensureBaseDirectoryExists(baseDirectory);

        requireDirectory(baseDirectory, () -> new IllegalArgumentException(notADirectory("baseDirectory")));
        requireReadable(baseDirectory, () -> new IllegalArgumentException(notReadable("baseDirectory")));
        requireWriteable(baseDirectory, () -> new IllegalArgumentException(notWritable("baseDirectory")));

        return new FileBasedPluginRepository(baseDirectory);
    }
    
    /**
     * <div>
     *     <p>
     *         Ensures that the base directory exists. If it does not exist, it will be created
     *         including any necessary but nonexistent parent directories.
     *     </p>
     * </div>
     *
     * @param baseDirectory the path to the base directory; must not be {@code null}
     * @throws PluginRepositoryException if the directory cannot be created
     */
    private static void ensureBaseDirectoryExists(final Path baseDirectory) {
        if (!Files.exists(baseDirectory)) {
            try {
                Files.createDirectories(baseDirectory);
            } catch (final IOException ioException) {
                throw new PluginRepositoryException(unableToCreateBaseDirectory(baseDirectory));
            }
        }
    }

    /**
     * The base directory where plugins are stored.
     */
    private final Path baseDirectory;

    /**
     * <div>
     *     <p>
     *         Creates a new {@code FileBasedPluginRepository} with the specified base directory.
     *     </p>
     * </div>
     *
     * @param baseDirectory the base directory; must not be {@code null}
     */
    private FileBasedPluginRepository(final Path baseDirectory) {
        this.baseDirectory
            = baseDirectory;
    }

    /**
     * <div>
     *     <p>
     *         Stores the plugin data from the provided input stream into the file system.
     *     </p>
     *     <p>
     *         The plugin is stored in a hierarchical directory structure based on its
     *         group, name, and version.
     *     </p>
     * </div>
     *
     * @param pluginId    the unique identifier of the plugin; must not be {@code null}
     * @param inputStream the input stream containing the plugin data; must not be {@code null}
     * @throws NullPointerException      if {@code pluginId} or {@code inputStream} is {@code null}
     * @throws PluginRepositoryException if an error occurs while storing the plugin
     */
    @Override
    public void store(final @NonNull PluginId pluginId,
                      final @NonNull InputStream inputStream) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        Objects.requireNonNull(inputStream, nullValue("inputStream"));

        final var pluginPath
            = createPluginPath(pluginId);

        try {
            Files.copy(inputStream, pluginPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException ioException) {
            throw new PluginRepositoryException(unableToStorePlugin(pluginId), ioException);
        }
    }

    /**
     * <div>
     *     <p>
     *         Retrieves an {@link InputStream} for the plugin data from the file system.
     *     </p>
     *     <p>
     *         The caller is responsible for closing the returned stream.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return an {@link InputStream} for reading the plugin data; never {@code null}
     * @throws NullPointerException      if {@code pluginId} is {@code null}
     * @throws PluginRepositoryException if the plugin data is not found or cannot be retrieved
     */
    @Override
    public @NonNull InputStream retrieve(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));

        final var pluginPath
            = getPluginPath(pluginId);

        if (Files.exists(pluginPath)) {
            try {
                return Files.newInputStream(pluginPath);
            } catch (final IOException ioException) {
                throw new PluginRepositoryException(unableToRetrievePlugin(pluginId), ioException);
            }
        } else {
            throw new PluginRepositoryException(unableToFindPlugin(pluginId));
        }
    }

    /**
     * <div>
     *     <p>
     *         Removes the plugin data from the file system.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @throws NullPointerException      if {@code pluginId} is {@code null}
     * @throws PluginRepositoryException if an error occurs while removing the plugin
     */
    @Override
    public void remove(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));

        final var pluginPath
            = getPluginPath(pluginId);

        try {
            Files.deleteIfExists(pluginPath);
        } catch (final IOException ioException) {
            throw new PluginRepositoryException(unableToRemovePlugin(pluginId), ioException);
        }
    }

    /**
     * <div>
     *     <p>
     *         Creates the file system path for a given plugin ID.
     *     </p>
     *     <p>
     *         The path is constructed hierarchically:
     *         <ul>
     *             <li>The group parts (split by dots) are converted to safe directory names.</li>
     *             <li>The plugin name is converted to a safe directory name.</li>
     *             <li>The plugin version is converted to a safe directory name.</li>
     *             <li>The final file name is {@code plugin.jar}.</li>
     *         </ul>
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return the {@link Path} where the plugin is (or should be) stored
     */
    private Path getPluginPath(final PluginId pluginId) {
        var currentPathEnd
            = baseDirectory;

        final var groupParts = pluginId.getGroup().split("\\.");
        for (final var part : groupParts) {
            currentPathEnd
                = currentPathEnd.resolve(toSafeFileName(part));
        }

        currentPathEnd
            = currentPathEnd.resolve(toSafeFileName(pluginId.getName()));
        currentPathEnd
            = currentPathEnd.resolve(toSafeFileName(pluginId.getVersion().toString()));

        return currentPathEnd.resolve("plugin.jar");
    }

    /**
     * <div>
     *     <p>
     *         Returns the path for the plugin and ensures that all parent directories exist
     *         on the file system.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return the {@link Path} to the plugin file
     * @throws PluginRepositoryException if the parent directories cannot be created
     */
    private Path createPluginPath(final PluginId pluginId) {
        final var pluginPath
            = getPluginPath(pluginId);
        final var parentDirectory
            = pluginPath.getParent();

        try {
            Files.createDirectories(parentDirectory);
        } catch (final IOException ioException) {
            throw new PluginRepositoryException(unableToCreatePluginDirectory(parentDirectory), ioException);
        }

        return pluginPath;
    }

}
