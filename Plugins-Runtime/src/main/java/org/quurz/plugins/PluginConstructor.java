package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.plugins.data.PluginId;

/**
 * <div>
 *     <p>
 *         A functional interface for constructing plugin instances.
 *     </p>
 *     <p>
 *         A {@code PluginConstructor} is typically obtained from the {@link PluginManager}
 *         for a specific plugin and allows creating one or more instances of that plugin
 *         with different arguments.
 *     </p>
 * </div>
 *
 * @param <A> the type of the plugin contract
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface PluginConstructor<A> {

    /**
     * <div>
     *     <p>
     *         Constructs a new instance of the plugin using the provided arguments.
     *     </p>
     * </div>
     *
     * @param arguments the arguments to pass to the plugin's constructor; must not be {@code null}
     * @return a new plugin instance, typically wrapped in a thread-safe proxy
     * @throws IllegalArgumentException if the plugin cannot be instantiated
     * @throws NullPointerException     if {@code arguments} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull A construct(final @NonNull TypedValue<?>... arguments);

}
