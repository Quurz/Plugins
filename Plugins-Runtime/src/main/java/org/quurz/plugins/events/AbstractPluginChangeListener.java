package org.quurz.plugins.events;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         An abstract adapter class for receiving plugin change events.
 *         The methods in this class are empty. This class exists as
 *         convenience for creating listener objects.
 *     </p>
 *     <p>
 *         Extend this class to create a {@link PluginChangeEventListener}
 *         and override only the methods for the events of interest.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public abstract class AbstractPluginChangeListener
        implements PluginChangeEventListener {

    /**
     * <div>
     *     <p>
     *         {@inheritDoc}
     *     </p>
     *     <p>
     *         The default implementation does nothing.
     *     </p>
     * </div>
     *
     * @param event the plugin change event; must not be {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public void onPluginChangeAnnouncementEvent(final PluginChangeEvent.@NonNull PluginChangeAnnouncementEvent event) {}

    /**
     * <div>
     *     <p>
     *         {@inheritDoc}
     *     </p>
     *     <p>
     *         The default implementation does nothing.
     *     </p>
     * </div>
     *
     * @param event the announcement event; must not be {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public void onPluginInstallEvent(final PluginChangeEvent.@NonNull PluginInstallEvent event) {}

    /**
     * <div>
     *     <p>
     *         {@inheritDoc}
     *     </p>
     *     <p>
     *         The default implementation does nothing.
     *     </p>
     * </div>
     *
     * @param event the install event; must not be {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public void onPluginUpdateEvent(final PluginChangeEvent.@NonNull PluginUpdateEvent event) {}

    /**
     * <div>
     *     <p>
     *         {@inheritDoc}
     *     </p>
     *     <p>
     *         The default implementation does nothing.
     *     </p>
     * </div>
     *
     * @param event the update event; must not be {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public void onPluginUninstallEvent(final PluginChangeEvent.@NonNull PluginUninstallEvent event) {}

}
