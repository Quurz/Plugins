package org.quurz.plugins.events;

import org.quurz.plugins.data.PluginId;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Represents an event that occurs when a plugin's state changes.
 *     </p>
 *     <p>
 *         This is a sealed class that defines the various types of lifecycle events
 *         that can occur for a plugin, such as installation, update, or removal.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public abstract sealed class PluginChangeEvent
        permits PluginChangeEvent.PluginChangeAnnouncementEvent,
                PluginChangeEvent.PluginInstallEvent,
                PluginChangeEvent.PluginUpdateEvent,
                PluginChangeEvent.PluginUninstallEvent {

    /**
     * <div>
     *     <p>
     *         Creates a new announcement event for the given plugin ID.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin
     *
     * @return a new announcement event instance
     *
     * @since 1.0.0
     */
    public static PluginChangeAnnouncementEvent pluginChangeAnnouncementEvent(final PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        return new PluginChangeAnnouncementEvent(pluginId);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new install event for the given plugin ID.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin
     *
     * @return a new install event instance
     *
     * @since 1.0.0
     */
    public static PluginInstallEvent pluginInstallEvent(final PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        return new PluginInstallEvent(pluginId);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new update event for the given plugin ID.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin
     *
     * @return a new update event instance
     *
     * @since 1.0.0
     */
    public static PluginUpdateEvent pluginUpdateEvent(final PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        return new PluginUpdateEvent(pluginId);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new uninstall event for the given plugin ID.
     *     </p>
     * </div>
     *
     * @param pluginId the unique identifier of the plugin
     *
     * @return a new uninstall event instance
     *
     * @since 1.0.0
     */
    public static PluginUninstallEvent pluginUninstallEvent(final PluginId pluginId) {
        Objects.requireNonNull(pluginId, nullValue("pluginId"));
        return new PluginUninstallEvent(pluginId);
    }

    /**
     * <div>
     *     <p>
     *         The unique identifier of the plugin associated with this event.
     *     </p>
     * </div>
     */
    protected final PluginId pluginId;

    /**
     * <div>
     *     <p>
     *         Initializes a new event for the specified plugin.
     *     </p>
     * </div>
     *
     * @param pluginId the identifier of the affected plugin
     */
    protected PluginChangeEvent(final PluginId pluginId) {
        this.pluginId
        = pluginId;
    }

    /**
     * <div>
     *     <p>
     *         Returns the unique identifier of the plugin that triggered this event.
     *     </p>
     * </div>
     *
     * @return the plugin identifier
     */
    protected PluginId getPluginId() {
        return this.pluginId;
    }

    /**
     * <div>
     *     <p>
     *         Event indicating that a plugin change is about to be announced.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public final static class PluginChangeAnnouncementEvent
            extends PluginChangeEvent {

        private final AtomicBoolean veto;

        private PluginChangeAnnouncementEvent(final PluginId pluginId) {
            super(pluginId);
            this.veto
                = new AtomicBoolean(false);
        }

        /**
         * <div>
         *     <p>
         *         Checks whether the announced change has been vetoed.
         *     </p>
         * </div>
         *
         * @return {@code true} if the change is vetoed, {@code false} otherwise
         */
        public boolean isVetoed() {
            return veto.get();
        }

        /**
         * <div>
         *     <p>
         *         Vetoes the announced change, preventing it from proceeding.
         *     </p>
         * </div>
         */
        public void setVetoed() {
            veto.set(true);
        }

    }

    /**
     * <div>
     *     <p>
     *         Event indicating that a new plugin has been installed.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class PluginInstallEvent
            extends PluginChangeEvent {

        private PluginInstallEvent(final PluginId pluginId) {
            super(pluginId);
        }

    }

    /**
     * <div>
     *     <p>
     *         Event indicating that an existing plugin has been updated to a new version.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class PluginUpdateEvent
            extends PluginChangeEvent {

        private PluginUpdateEvent(final PluginId pluginId) {
            super(pluginId);
        }

    }

    /**
     * <div>
     *     <p>
     *         Event indicating that a plugin has been uninstalled.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class PluginUninstallEvent
            extends PluginChangeEvent {

        private PluginUninstallEvent(final PluginId pluginId) {
            super(pluginId);
        }

    }

}
