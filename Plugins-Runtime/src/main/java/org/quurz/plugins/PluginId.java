package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.misc.SemVer;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Identifier for a plugin, uniquely defined by its name and version.
 *     </p>
 *     <p>
 *         This class is immutable and serves as a key to distinguish different plugins
 *         and their specific versions within the system.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 * @author Alexander Schell
 */
public class PluginId
        implements Serializable {

    @Serial
    private static final long serialVersionUID
        = 23L;

    /**
     * <div>
     *     <p>
     *         Creates a new {@link PluginId} instance.
     *     </p>
     * </div>
     *
     * @param name    the non-null name of the plugin
     * @param version the non-null semantic version of the plugin
     * @return a new {@link PluginId} instance
     * @throws NullPointerException if {@code name} or {@code version} is {@code null}
     * @since 1.0.0
     */
    public static PluginId pluginId(final @NonNull String name,
                                    final @NonNull SemVer version) {
        Objects.requireNonNull(name, nullValue("name"));
        Objects.requireNonNull(version, nullValue("version"));
        return new PluginId(name, version);
    }

    private final String name;
    private final SemVer version;

    private PluginId(final String name,
                     final SemVer version) {
        this.name
            = name;
        this.version
            = version;
    }

    /**
     * <div>
     *     <p>
     *         Returns the name of the plugin.
     *     </p>
     * </div>
     *
     * @return the non-null plugin name
     * @since 1.0.0
     */
    public String getName() {
        return this.name;
    }

    /**
     * <div>
     *     <p>
     *         Returns the semantic version of the plugin.
     *     </p>
     * </div>
     *
     * @return the non-null plugin version
     * @since 1.0.0
     */
    public SemVer getVersion() {
        return this.version;
    }

    /**
     * <div>
     *     <p>
     *         Compares this PluginId to another object for equality.
     *         Two PluginId instances are equal if both their name and version are equal.
     *     </p>
     * </div>
     *
     * @param o object to compare with
     * @return true if the given object is an equal PluginId; false otherwise
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluginId that)) return false;
        return name.equals(that.name) && version.equals(that.version);
    }

    /**
     * <div>
     *     <p>
     *         Returns a hash code for this PluginId based on its name and version.
     *     </p>
     * </div>
     *
     * @return computed hash code
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(version);
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Returns a string representation in the form {@code PluginId[name=..., version=...]}.
     *     </p>
     * </div>
     *
     * @return human-readable description of this PluginId
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", PluginId.class.getSimpleName() + "[", "]")
                .add("name=" + name)
                .add("version=" + version)
                .toString();
    }

}
