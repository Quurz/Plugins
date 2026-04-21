package org.quurz.plugins.localisation;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.plugins.PluginId;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class PluginsMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("PluginsMessages", Locale.getDefault());

    private PluginsMessages() {}

    public static String unableToCreateBaseDirectory(final Path directory) {
        Objects.requireNonNull(directory);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_CREATE_BASE_DIRECTORY").formatted(directory.toAbsolutePath().toString());
    }

    public static String unableToCreatePluginDirectory(final Path directory) {
        Objects.requireNonNull(directory);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_CREATE_PLUGIN_DIRECTORY").formatted(directory.toAbsolutePath().toString());
    }

    public static String unableToStorePlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_STORE_PLUGIN").formatted(pluginId.toString());
    }

    public static String unableToFindPlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_FIND_PLUGIN").formatted(pluginId.toString());
    }

    public static String unableToRetrievePlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_RETRIEVE_PLUGIN").formatted(pluginId.toString());
    }

    public static String unableToRemovePlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_REMOVE_PLUGIN").formatted(pluginId.toString());
    }

    public static String unableToCreatePlugin() {
        return RESOURCE_BUNDLE.getString("UNABLE_TO_CREATE_PLUGIN");
    }

}
