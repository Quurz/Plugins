package org.quurz.plugins.internal;

import  net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.plugins.PluginConstructor;
import org.quurz.plugins.TypedValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Util.*;
import static org.quurz.plugins.TypedValue.extractTypesAndValues;
import static org.quurz.plugins.localisation.PluginsMessages.unableToCreatePlugin;

/**
 * <div>
 *     <p>
 *         A factory for creating plugin instances that are wrapped in a thread-safe proxy.
 *     </p>
 *     <p>
 *         The factory uses ByteBuddy to generate a proxy class at runtime that implements the
 *         specified contract and delegates all calls to an underlying implementation instance.
 *         Access to the implementation is synchronized using a {@link java.util.concurrent.locks.ReadWriteLock}.
 *     </p>
 * </div>
 *
 * @param <A> the type of the plugin contract (interface)
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface PluginFactory<A> extends PluginConstructor<A> {

    /**
     * <div>
     *     <p>
     *         Creates a new {@code PluginFactory} for the given contract and implementation.
     *     </p>
     * </div>
     *
     * @param contract           the interface that the plugin must implement; must not be {@code null}
     * @param implementation     the concrete class providing the implementation; must not be {@code null}
     * @param fullyQualifiedName the fully qualified name for the generated proxy class; must not be {@code null}
     * @param classLoader        the {@link ClassLoader} to use for loading the proxy class; must not be {@code null}
     * @param <A>                the type of the plugin contract
     * @return a new {@code PluginFactory} instance
     * @throws IllegalArgumentException if {@code contract} is not an interface, {@code implementation} is not a concrete class,
     *                                  or if the proxy class cannot be generated
     * @throws NullPointerException     if any of the parameters is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"unchecked", "resource"})
    static <A> PluginFactory<A> pluginFactory(final @NonNull Class<A> contract,
                                              final @NonNull Class<? extends A> implementation,
                                              final @NonNull String fullyQualifiedName,
                                              final @NonNull ClassLoader classLoader) {
        requireInterfaceType(
            Objects.requireNonNull(contract, nullValue("contract")),
            () -> new IllegalArgumentException(notAnInterface(contract))
        );
        requireConcreteType(
            Objects.requireNonNull(implementation, nullValue("implementation")),
            () -> new IllegalArgumentException(notAConcreteClass(implementation))
        );
        Objects.requireNonNull(fullyQualifiedName, nullValue("fullyQualifiedName"));
        Objects.requireNonNull(classLoader, nullValue("classLoader"));

        final var builder
            = new ByteBuddy()
                .subclass(Proxy.class)
                .implement(contract)
                .method(net.bytebuddy.matcher.ElementMatchers.isDeclaredBy(contract))
                .intercept(net.bytebuddy.implementation.MethodDelegation.to(LockedDelegator.class))
                .name(fullyQualifiedName);

        final var unloadedType
            = builder.make();
        final var loadedType
            = unloadedType.load(classLoader, net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
        return arguments -> {
            try {
                final var typesAndValues
                    = TypedValue.extractTypesAndValues(arguments);
                final var pluginConstructor
                    = implementation.getDeclaredConstructor(typesAndValues.get1());
                final var plugin
                    = pluginConstructor.newInstance(typesAndValues.get2());
                final var proxy
                    = loadedType.getDeclaredConstructor().newInstance();
                proxy.$__set_implementation(plugin);
                return (A) proxy;
            } catch (final NoSuchMethodException
                         | InstantiationException
                         | IllegalAccessException
                         | IllegalArgumentException
                         | InvocationTargetException exception) {
                throw new IllegalArgumentException(unableToCreatePlugin(), exception);
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Constructs a new instance of the plugin using the provided arguments.
     *     </p>
     * </div>
     *
     * @param arguments the arguments to pass to the implementation's constructor; must not be {@code null}
     * @return a thread-safe proxy instance implementing the contract
     * @throws IllegalArgumentException if the implementation cannot be instantiated (e.g., matching constructor not found)
     * @throws NullPointerException     if {@code arguments} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    A construct(final @NonNull TypedValue<?>... arguments);

    /**
     * <div>
     *     <p>
     *         An internal interceptor class used by ByteBuddy for method delegation.
     *     </p>
     *     <p>
     *         This class ensures that all method calls on the proxy are thread-safe by acquiring
     *         a write lock before delegating the call to the actual implementation.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    class LockedDelegator {

        /**
         * <div>
         *     <p>
         *         Intercepts method calls on the proxy and delegates them to the underlying implementation.
         *     </p>
         * </div>
         *
         * @param plugin the proxy instance
         * @param method the method being called
         * @param args   the arguments passed to the method
         * @return the result of the method invocation
         * @throws Throwable if the underlying method call throws an exception
         *
         * @since 1.0.0
         */
        @RuntimeType
        public static Object intercept(final @This Proxy<?> plugin,
                                       final @Origin Method method,
                                       final @AllArguments Object[] args) throws Throwable {
            plugin.$__access_lock.writeLock().lock();
            try {
                final var implementation
                    = plugin.$__get_implementation();
                return method.invoke(implementation, args);
            } finally {
                plugin.$__access_lock.writeLock().unlock();
            }
        }
    }

}
