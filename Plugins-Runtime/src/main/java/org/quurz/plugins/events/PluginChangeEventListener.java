package org.quurz.plugins.events;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Listener interface for receiving plugin change events.
 *     </p>
 *     <p>
 *         Classes that are interested in processing a plugin change event
 *         implement this interface, and the object created with that class
 *         is registered with a component, using the component's
 *         {@code addPluginChangeEventListener} method. When the plugin change
 *         event occurs, that object's {@code onEvent} method is invoked.
 *     </p>
 * </div>
 *
 * @see PluginChangeEvent
 *
 * @since 1.0.0
 * @author Alexander
 */
public interface PluginChangeEventListener {

    /**
     * <div>
     *     <p>
     *         Invoked when a plugin change announcement event occurs.
     *     </p>
     * </div>
     *
     * @param event the announcement event; must not be {@code null}
     *
     * @since 1.0.0
     */
    void onPluginChangeAnnouncementEvent(final PluginChangeEvent.@NonNull PluginChangeAnnouncementEvent event);

    /**
     * <div>
     *     <p>
     *         Invoked when a plugin install event occurs.
     *     </p>
     * </div>
     *
     * @param event the install event; must not be {@code null}
     *
     * @since 1.0.0
     */
    void onPluginInstallEvent(final PluginChangeEvent.@NonNull PluginInstallEvent event);

    /**
     * <div>
     *     <p>
     *         Invoked when a plugin update event occurs.
     *     </p>
     * </div>
     *
     * @param event the update event; must not be {@code null}
     *
     * @since 1.0.0
     */
    void onPluginUpdateEvent(final PluginChangeEvent.@NonNull PluginUpdateEvent event);

    /**
     * <div>
     *     <p>
     *         Invoked when a plugin uninstall event occurs.
     *     </p>
     * </div>
     *
     * @param event the uninstall event; must not be {@code null}
     *
     * @since 1.0.0
     */
    void onPluginUninstallEvent(final PluginChangeEvent.@NonNull PluginUninstallEvent event);

}
