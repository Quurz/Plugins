package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.plugins.data.PluginId;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.*;
import static org.quurz.plugins.localisation.PluginsMessages.*;

/**
 * <div>
 *     <p>
 *         A file-based implementation of the {@link PluginRepository}.
 *     </p>
 *     <p>
 *         This repository stores plugin JAR files in a structured directory tree
 *         starting from a root directory. The structure follows a Maven-like pattern:
 *         {@code root/group_part1/group_part2/name/version/}.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class FileRepository
        implements PluginRepository {

    /**
     * <div>
     *     <p>
     *         Creates a new {@code FileRepository} with the specified root directory path.
     *     </p>
     * </div>
     *
     * @param root the path to the root directory as a string; must not be {@code null}
     * @return a new {@code FileRepository} instance
     * @throws NullPointerException if {@code root} is {@code null}
     *
     * @since 1.0.0
     */
    public static FileRepository fileRepository(final @NonNull String root) {
        Objects.requireNonNull(root, nullValue("root"));
        return fileRepository(Paths.get(root));
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code FileRepository} with the specified root directory.
     *     </p>
     * </div>
     *
     * @param root the root directory path; must not be {@code null}
     * @return a new {@code FileRepository} instance
     * @throws NullPointerException if {@code root} is {@code null}
     *
     * @since 1.0.0
     */
    public static FileRepository fileRepository(final @NonNull Path root) {
        Objects.requireNonNull(root, nullValue("root"));
        try {
            requireDirectory(root, () -> new IllegalArgumentException(unableToCreateBaseDirectory(root)));
            requireReadable(root, () -> new IllegalArgumentException(unableToCreateBaseDirectory(root)));
            requireWriteable(root, () -> new IllegalArgumentException(unableToCreateBaseDirectory(root)));
        } catch (final IllegalArgumentException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new IllegalArgumentException(unableToCreateBaseDirectory(root), exception);
        }
        return new FileRepository(root);
    }

    private final Path root;

    private FileRepository(final Path root) {
        this.root
            = root;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public void store(final @NonNull PluginId pluginId,
                      final @NonNull Supplier<InputStream> inputStreamSupplier)
            throws PluginRepositoryException {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        Objects.requireNonNull(inputStreamSupplier, nullValue("inputStreamSupplier"));

        try {
            requireWriteable(root, () -> new PluginRepositoryException(unableToStorePlugin(pluginId)));
        } catch (final PluginRepositoryException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new PluginRepositoryException(unableToStorePlugin(pluginId), exception);
        }

        final var directory = resolveDirectory(pluginId);
        try {
            Files.createDirectories(directory);
        } catch (final IOException exception) {
            throw new PluginRepositoryException(unableToCreatePluginDirectory(directory), exception);
        }

        final var jarFile = directory.resolve("plugin.jar");
        try (final var inputStream = inputStreamSupplier.get();
             final var outputStream = Files.newOutputStream(jarFile)) {
            inputStream.transferTo(outputStream);
        } catch (final IOException exception) {
            throw new PluginRepositoryException(unableToStorePlugin(pluginId), exception);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Supplier<InputStream> retrieve(final @NonNull PluginId pluginId)
            throws PluginRepositoryException {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));

        final var jarFile = resolveDirectory(pluginId).resolve("plugin.jar");
        try {
            requireRegularFile(jarFile, () -> new PluginRepositoryException(unableToFindPlugin(pluginId)));
            requireReadable(jarFile, () -> new PluginRepositoryException(unableToRetrievePlugin(pluginId)));
        } catch (final PluginRepositoryException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new PluginRepositoryException(unableToRetrievePlugin(pluginId), exception);
        }

        return () -> {
            try {
                return Files.newInputStream(jarFile);
            } catch (final IOException exception) {
                throw new RuntimeException(unableToRetrievePlugin(pluginId), exception);
            }
        };
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public boolean contains(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        final var jarFile = resolveDirectory(pluginId).resolve("plugin.jar");
        return Files.isRegularFile(jarFile);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public void remove(final @NonNull PluginId pluginId)
            throws PluginRepositoryException {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        final var jarFile = resolveDirectory(pluginId).resolve("plugin.jar");
        try {
            if (Files.exists(jarFile)) {
                Files.delete(jarFile);
            }
        } catch (final IOException exception) {
            throw new PluginRepositoryException(unableToRemovePlugin(pluginId), exception);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Set<PluginId> plugins() {
        return Set.of(); // TODO: Implement directory walking
    }

    /**
     * <div>
     *     <p>
     *         Resolves the directory path for the given plugin identifier.
     *     </p>
     * </div>
     *
     * @param pluginId the plugin identifier to resolve
     * @return the resolved path
     */
    private Path resolveDirectory(final PluginId pluginId) {
        Path path = root;
        for (final String part : pluginId.getGroup().split("\\.")) {
            path = path.resolve(toSafeFileName(part));
        }
        path = path.resolve(toSafeFileName(pluginId.getName()));
        path = path.resolve(toSafeFileName(pluginId.getVersion().toString()));
        return path;
    }

}
