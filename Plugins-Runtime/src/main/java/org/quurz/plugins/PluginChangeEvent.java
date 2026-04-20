package org.quurz.plugins;

public sealed interface PluginChangeEvent
        permits PluginChangeEvent.PluginChangeAnnouncementEvent,
                PluginChangeEvent.PluginInstallEvent,
                PluginChangeEvent.PluginUpdateEvent,
                PluginChangeEvent.PluginUninstallEvent {

    final class PluginChangeAnnouncementEvent
            implements PluginChangeEvent {

    }

    final class PluginInstallEvent
            implements PluginChangeEvent {

    }

    final class PluginUpdateEvent
            implements PluginChangeEvent {

    }

    final class PluginUninstallEvent
            implements PluginChangeEvent {

    }

}
