package org.quurz.plugins.internal;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static java.util.Objects.requireNonNull;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A {@link ClassLoader} that loads classes and resources from a JAR {@link InputStream}.
 *     </p>
 *     <p>
 *         This class loader reads the entire content of the provided input stream during
 *         initialisation and stores the classes and resources in memory.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class StreamClassLoader
        extends ClassLoader {

    private final Map<String, byte[]> classes;
    private final Map<String, byte[]> resources;

    /**
     * <div>
     *     <p>
     *         Creates a new {@code StreamClassLoader} from the specified input stream.
     *     </p>
     * </div>
     *
     * @param inputStream the {@link InputStream} containing the JAR data; must not be {@code null}
     * @throws IOException          if an I/O error occurs while reading the stream
     * @throws NullPointerException if {@code inputStream} is {@code null}
     *
     * @since 1.0.0
     */
    public StreamClassLoader(final @NonNull InputStream inputStream) throws IOException {
        this(inputStream, StreamClassLoader.class.getClassLoader());
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code StreamClassLoader} from the specified input stream
     *         with the given parent class loader.
     *     </p>
     * </div>
     *
     * @param inputStream the {@link InputStream} containing the JAR data; must not be {@code null}
     * @param parent      the parent {@link ClassLoader}; may be {@code null}
     * @throws IOException          if an I/O error occurs while reading the stream
     * @throws NullPointerException if {@code inputStream} is {@code null}
     *
     * @since 1.0.0
     */
    public StreamClassLoader(final @NonNull InputStream inputStream,
                             final @Nullable ClassLoader parent) throws IOException {
        super(parent);
        requireNonNull(inputStream, nullValue("inputStream"));

        this.classes
            = new ConcurrentHashMap<>();
        this.resources
            = new ConcurrentHashMap<>();

        try (final var jarInputStream = new JarInputStream(inputStream)) {
            JarEntry entry;
            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                final byte[] data = jarInputStream.readAllBytes();
                final String name = entry.getName();
                if (name.endsWith(".class")) {
                    final String className = name.substring(0, name.length() - 6).replace('/', '.');
                    classes.put(className, data);
                } else {
                    resources.put(name, data);
                }
            }
        }
    }

    /**
     * <div>
     *     <p>
     *         Finds and loads the class with the specified name from the in-memory data.
     *     </p>
     * </div>
     *
     * @param name the binary name of the class; must not be {@code null}
     * @return the resulting {@link Class} object
     * @throws ClassNotFoundException if the class could not be found
     *
     * @since 1.0.0
     */
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final byte[] data
            = classes.get(name);
        if (data != null) {
            return defineClass(name, data, 0, data.length);
        }
        return super.findClass(name);
    }

    /**
     * <div>
     *     <p>
     *         Returns an input stream for reading the specified resource.
     *     </p>
     * </div>
     *
     * @param name the resource name; must not be {@code null}
     * @return an {@link InputStream} for reading the resource, or {@code null} if not found
     *
     * @since 1.0.0
     */
    @Override
    public InputStream getResourceAsStream(final String name) {
        final byte[] data
            = resources.get(name);
        if (data != null) {
            return new ByteArrayInputStream(data);
        }
        return super.getResourceAsStream(name);
    }

}
