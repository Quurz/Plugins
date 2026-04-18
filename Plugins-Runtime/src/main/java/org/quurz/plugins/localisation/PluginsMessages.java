package org.quurz.plugins.localisation;

import java.util.Locale;
import java.util.ResourceBundle;

public class PluginsMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("PluginsMessages", Locale.getDefault());

    private PluginsMessages() {}

    public static String unableToCreatePlugin() {
        return RESOURCE_BUNDLE.getString("unableToCreatePlugin");
    }

}
