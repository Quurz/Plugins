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
 *         Identifier for a plugin, uniquely defined by its group, name and version.
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
     * @param group   the non-null group of the plugin
     * @param name    the non-null name of the plugin
     * @param version the non-null semantic version of the plugin
     * @return a new {@link PluginId} instance
     * @throws NullPointerException if {@code group}, {@code name} or {@code version} is {@code null}
     * @since 1.0.0
     */
    public static PluginId pluginId(final @NonNull String group,
                                    final @NonNull String name,
                                    final @NonNull SemVer version) {
        Objects.requireNonNull(name, nullValue("name"));
        Objects.requireNonNull(version, nullValue("version"));
        Objects.requireNonNull(group, nullValue("group"));
        return new PluginId(group, name, version);
    }

    private final String group;
    private final String name;
    private final SemVer version;

    private PluginId(final String group,
                     final String name,
                     final SemVer version) {
        this.group
            = group;
        this.name
            = name;
        this.version
            = version;
    }

    /**
     * <div>
     *     <p>
     *         Returns the group of the plugin.
     *     </p>
     * </div>
     *
     * @return the non-null plugin group
     * @since 1.0.0
     */
    public String getGroup() {
        return this.group;
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
     *         Two PluginId instances are equal if their group, name and version are equal.
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
        return Objects.equals(group, that.group) && Objects.equals(name, that.name) && Objects.equals(version, that.version);
    }

    /**
     * <div>
     *     <p>
     *         Returns a hash code for this PluginId based on its group, name and version.
     *     </p>
     * </div>
     *
     * @return computed hash code
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(group, name, version);
    }

    /**
     * <div>
     *     <p>
     *         Returns a string representation in the form {@code PluginId[group=..., name=..., version=...]}.
     *     </p>
     * </div>
     *
     * @return human-readable description of this PluginId
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", PluginId.class.getSimpleName() + "[", "]")
                .add("group=" + group)
                .add("name=" + name)
                .add("version=" + version)
                .toString();
    }

}
