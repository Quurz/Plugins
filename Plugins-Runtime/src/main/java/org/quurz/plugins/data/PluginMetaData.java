package org.quurz.plugins.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.misc.SemVer.parseSemVer;
import static org.quurz.plugins.data.PluginId.pluginId;

/**
 * <div>
 *     <p>
 *         Represents the metadata of a plugin.
 *     </p>
 *     <p>
 *         Metadata includes the plugin's unique identification (name and version),
 *         the contract it implements, its implementation class, and an optional description.
 *     </p>
 *     <p>
 *         This class is designed for JSON deserialization via Jackson and is immutable.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginMetaData {

    /**
     * <div>
     *     <p>
     *         Factory method for creating {@link PluginMetaData} from JSON properties.
     *     </p>
     * </div>
     *
     * @param group           the group of the plugin; must not be {@code null}
     * @param name            the name of the plugin; must not be {@code null}
     * @param version         the semantic version string; must not be {@code null}
     * @param contract        the fully qualified name of the contract interface; must not be {@code null}
     * @param implementation  the fully qualified name of the implementation class; must not be {@code null}
     * @param description     an optional description of the plugin
     * @return a new {@link PluginMetaData} instance
     * @throws NullPointerException if any required parameter is {@code null}
     *
     * @since 1.0.0
     */
    @JsonCreator
    public static PluginMetaData pluginMetaData(final @JsonProperty("group") @NonNull String group,
                                                final @JsonProperty("name") @NonNull String name,
                                                final @JsonProperty("version") @NonNull String version,
                                                final @JsonProperty("contract") @NonNull String contract,
                                                final @JsonProperty("implementation") @NonNull String implementation,
                                                final @JsonProperty("description") String description) {
        Objects.requireNonNull(group, nullValue("group"));
        Objects.requireNonNull(name, nullValue("name"));
        Objects.requireNonNull(version, nullValue("version"));
        Objects.requireNonNull(contract, nullValue("contract"));
        Objects.requireNonNull(implementation, nullValue("implementation"));

        return new PluginMetaData(pluginId(group, name, parseSemVer(version)), contract, implementation, description == null ? "" : description);
    }

    private final PluginId id;
    private final String contract;
    private final String implementation;
    private final String description;

    private PluginMetaData(final PluginId id,
                           final String contract,
                           final String implementation,
                           final String description) {
        this.id
            = id;
        this.contract
            = contract;
        this.implementation
            = implementation;
        this.description
            = description;
    }

    /**
     * <div>
     *     <p>
     *         Returns the unique identifier of the plugin (name and version).
     *     </p>
     * </div>
     *
     * @return the plugin identifier
     *
     * @since 1.0.0
     */
    public PluginId getId() {
        return this.id;
    }

    /**
     * <div>
     *     <p>
     *         Returns the fully qualified name of the contract interface.
     *     </p>
     * </div>
     *
     * @return the contract name
     *
     * @since 1.0.0
     */
    public String getContract() {
        return this.contract;
    }

    /**
     * <div>
     *     <p>
     *         Returns the fully qualified name of the plugin's implementation class.
     *     </p>
     * </div>
     *
     * @return the implementation class name
     *
     * @since 1.0.0
     */
    public String getImplementation() {
        return this.implementation;
    }

    /**
     * <div>
     *     <p>
     *         Returns the description of the plugin.
     *     </p>
     * </div>
     *
     * @return the plugin description (empty string if not provided)
     *
     * @since 1.0.0
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PluginMetaData that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(contract, that.contract) && Objects.equals(implementation, that.implementation) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contract, implementation, description);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PluginMetaData.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("contract=" + contract)
                .add("implementation=" + implementation)
                .add("description=" + description)
                .toString();
    }

}
