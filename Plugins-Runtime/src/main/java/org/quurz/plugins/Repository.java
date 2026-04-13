package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.InputStream;

public interface Repository {

    @NonNull
    PluginHandle push(final @NonNull PluginId id,
                      final @NonNull InputStream inputStream)
            throws Exception;

    void delete(final @NonNull PluginId id)
            throws Exception;

}
