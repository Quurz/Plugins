package org.quurz.plugins.localisation;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.plugins.data.PluginId;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * <div>
 *     <p>
 *         Provides localized messages for the plugin system.
 *     </p>
 *     <p>
 *         This class uses a {@link ResourceBundle} to retrieve messages in the
 *         current locale.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginsMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("org.quurz.plugins.localisation.PluginsMessages", Locale.getDefault());

    private PluginsMessages() {}

    public static String pluginManagerAlreadyInitialised() {
        return RESOURCE_BUNDLE.getString("PLUGIN_MANAGER_ALREADY_INITIALISED");
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that the plugin manager has not been initialised.
     *     </p>
     * </div>
     *
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String pluginManagerNotInitialised() {
        return RESOURCE_BUNDLE.getString("PLUGIN_MANAGER_NOT_INITIALISED");
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that the base directory could not be created.
     *     </p>
     * </div>
     *
     * @param directory the path of the directory that could not be created; must not be {@code null}
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToCreateBaseDirectory(final Path directory) {
        Objects.requireNonNull(directory);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_CREATE_BASE_DIRECTORY").formatted(directory.toAbsolutePath().toString());
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that a plugin-specific directory could not be created.
     *     </p>
     * </div>
     *
     * @param directory the path of the directory that could not be created; must not be {@code null}
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToCreatePluginDirectory(final Path directory) {
        Objects.requireNonNull(directory);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_CREATE_PLUGIN_DIRECTORY").formatted(directory.toAbsolutePath().toString());
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that a plugin could not be stored.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToStorePlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_STORE_PLUGIN").formatted(pluginId.toString());
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that a plugin could not be found.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToFindPlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_FIND_PLUGIN").formatted(pluginId.toString());
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that a plugin could not be retrieved from the repository.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToRetrievePlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_RETRIEVE_PLUGIN").formatted(pluginId.toString());
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that a plugin could not be removed from the repository.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin; must not be {@code null}
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToRemovePlugin(final @NonNull PluginId pluginId) {
        Objects.requireNonNull(pluginId);
        return RESOURCE_BUNDLE.getString("UNABLE_TO_REMOVE_PLUGIN").formatted(pluginId.toString());
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that a class loader for the plugin could not be created.
     *     </p>
     * </div>
     *
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToCreateClassLoader() {
        return RESOURCE_BUNDLE.getString("UNABLE_TO_CREATE_CLASS_LOADER");
    }

    /**
     * <div>
     *     <p>
     *         Returns a message indicating that a plugin instance could not be created.
     *     </p>
     * </div>
     *
     * @return the localized error message
     *
     * @since 1.0.0
     */
    public static String unableToCreatePlugin() {
        return RESOURCE_BUNDLE.getString("UNABLE_TO_CREATE_PLUGIN");
    }

}
