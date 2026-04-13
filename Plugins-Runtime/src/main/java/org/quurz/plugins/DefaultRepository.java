package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarFile;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.*;

public class DefaultRepository
        implements Repository {

    private static final String SCHEMA_FILE
        = "/PluginMetaInfSchema.json";
    private static final String PLUGIN_META_INF_FILE_NAME
        = "PluginMetaInf";

    public static Repository defaultRepository(final @NonNull Path rootDirectory) {
        Objects.requireNonNull(rootDirectory, nullValue("rootDirectory"));
        // TODO: Lokalisierung in Base-Modul
        mustBeDirectory(rootDirectory, () -> new IllegalArgumentException("rootDirectory must be a directory (rootDirectory = '%s' )".formatted(rootDirectory)));
        mustBeReadable(rootDirectory, () -> new IllegalArgumentException("rootDirectory must be readable (rootDirectory = '%s' )".formatted(rootDirectory)));
        mustBeWriteable(rootDirectory, () -> new IllegalArgumentException("rootDirectory must be writeable (rootDirectory = '%s' )".formatted(rootDirectory)));

        return new DefaultRepository(rootDirectory);
    }

    private final Path rootDirectory;
    private final PluginMetadataValidator pluginMetadataValidator;
    private final Lock accessLock;

    private DefaultRepository(final Path rootDirectory) {
        this.rootDirectory
            = rootDirectory;
        this.pluginMetadataValidator
            = new PluginMetadataValidator();
        this.accessLock
            = new ReentrantLock(true);
    }

    @Override
    @NonNull
    public PluginHandle push(final @NonNull PluginId id,
                             final @NonNull InputStream inputStream)
            throws IOException {
        Objects.requireNonNull(id, nullValue("id"));
        Objects.requireNonNull(inputStream, nullValue("inputStream"));

        this.accessLock.lock();
        try {
            final var pluginPath
                = this.rootDirectory.resolve(toSafeFileName(id.getName()) + ".jar");
            Files.copy(inputStream, pluginPath, StandardCopyOption.REPLACE_EXISTING);

            try(final var pluginJarFile
                    = new JarFile(pluginPath.toFile())) {
                final var entry
                    = Objects.requireNonNull(pluginJarFile.getEntry(PLUGIN_META_INF_FILE_NAME), "No plugin meta inf file found");    // TODO: Lokalisierung
                try (final var pluginMetaInfInputStream
                         = pluginJarFile.getInputStream(entry)) {
                    final var possibleErrors
                        = this.pluginMetadataValidator.validate(pluginMetaInfInputStream);
                    if (possibleErrors.isEmpty()) {
                        // TODO
                    } else {
                        // TODO: Exception
                    }
                    return null;    // TODO
                }
            }
        } finally {
            this.accessLock.unlock();
        }
    }

    @Override
    public void delete(final @NonNull PluginId id) throws Exception {
        Objects.requireNonNull(id, nullValue("id"));

        this.accessLock.lock();
        try {
            // TODO
            // final var pluginPath = this.rootDirectory.resolve(toSafeFileName(id.getName()) + ".jar");
            // Files.deleteIfExists(pluginPath);
        } finally {
            this.accessLock.unlock();
        }
    }

}
