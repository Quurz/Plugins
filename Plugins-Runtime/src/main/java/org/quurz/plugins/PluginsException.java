package org.quurz.plugins;

/**
 * <div>
 *     <p>
 *         Base exception for all plugin-related errors in the runtime system.
 *     </p>
 * </div>
 *
 * @author Alexander
 * @since 1.0.0
 */
public class PluginsException
        extends RuntimeException {

    /**
     * <div>
     *     <p>
     *         Constructs a new {@code PluginsException} with {@code null} as its detail message.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public PluginsException() {
        super();
    }

    /**
     * <div>
     *     <p>
     *         Constructs a new {@code PluginsException} with the specified detail message.
     *     </p>
     * </div>
     *
     * @param message the detail message
     * @since 1.0.0
     */
    public PluginsException(final String message) {
        super(message);
    }

    /**
     * <div>
     *     <p>
     *         Constructs a new {@code PluginsException} with the specified detail message and cause.
     *     </p>
     * </div>
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     * @since 1.0.0
     */
    public PluginsException(final String message,
                            final Throwable cause) {
        super(message, cause);
    }

    /**
     * <div>
     *     <p>
     *         Constructs a new {@code PluginsException} with the specified cause and a detail message
     *         of {@code (cause==null ? null : cause.toString())}.
     *     </p>
     * </div>
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method)
     * @since 1.0.0
     */
    public PluginsException(Throwable cause) {
        super(cause);
    }

    /**
     * <div>
     *     <p>
     *         Constructs a new {@code PluginsException} with the specified detail message,
     *         cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     *     </p>
     * </div>
     *
     * @param message            the detail message
     * @param cause              the cause
     * @param enableSuppression  whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     * @since 1.0.0
     */
    protected PluginsException(final String message,
                               final Throwable cause,
                               final boolean enableSuppression,
                               final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
