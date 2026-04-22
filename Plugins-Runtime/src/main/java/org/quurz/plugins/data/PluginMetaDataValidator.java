package org.quurz.plugins.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.Error;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Attempt;
import org.quurz.foomp.base.util.Either;
import org.quurz.foomp.base.util.Result;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Attempt.attempt;
import static org.quurz.foomp.base.util.Util.requireReadable;
import static org.quurz.foomp.base.util.Util.requireRegularFile;

/**
 * <div>
 *     <p>
 *         Validates plugin metadata JSON strings against the defined JSON schema.
 *     </p>
 *     <p>
 *         The class provides static methods to load and
 *         validate metadata from various sources.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginMetaDataValidator {

    private static final String SCHEMA_FILE
        = "PluginMetaDataSchema.json";
    private static final Schema SCHEMA
        = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7)
            .getSchema(PluginMetaDataValidator.class.getResourceAsStream(SCHEMA_FILE));
    private static final ObjectMapper OBJECT_MAPPER
        = new ObjectMapper();

    /**
     * <div>
     *     <p>
     *         Loads and validates plugin metadata from a file via its filename.
     *     </p>
     * </div>
     *
     * @param fileName the name of the file to load; must not be {@code null}
     * @return a {@link Result} containing either an {@link Either} with a list of {@link Error}s
     *         (in case of validation errors) or the validated {@link PluginMetaData} object
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if {@code fileName} is {@code null}
     *
     * @since 1.0.0
     */
    public static Result<Either<List<Error>, PluginMetaData>> loadAndValidatePluginMetadata(final @NonNull String fileName) {
        Objects.requireNonNull(fileName, nullValue("fileName"));
        requireRegularFile(fileName, () -> new IllegalArgumentException(notARegularFile("fileName")));
        requireReadable(fileName, () -> new IllegalArgumentException(notReadable("fileName")));
        return loadAndValidatePluginMetadata(Paths.get(fileName));
    }

    /**
     * <div>
     *     <p>
     *         Loads and validates plugin metadata from a file via its path.
     *     </p>
     * </div>
     *
     * @param fileName the {@link Path} to the file to load; must not be {@code null}
     * @return a {@link Result} containing either an {@link Either} with a list of {@link Error}s
     *         (in case of validation errors) or the validated {@link PluginMetaData} object
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if {@code fileName} is {@code null}
     *
     * @since 1.0.0
     */
    public static Result<Either<List<Error>, PluginMetaData>> loadAndValidatePluginMetadata(final @NonNull Path fileName) {
        Objects.requireNonNull(fileName, nullValue("fileName"));
        requireRegularFile(fileName, () -> new IllegalArgumentException(notARegularFile("fileName")));
        requireReadable(fileName, () -> new IllegalArgumentException(notReadable("fileName")));
        return loadAndValidate(
            attempt(fileName)
                .mapUnsafe(Files::newInputStream)
        );
    }

    /**
     * <div>
     *     <p>
     *         Loads and validates plugin metadata from an {@link InputStream}.
     *     </p>
     * </div>
     *
     * @param inputStream the {@link InputStream} containing the JSON; must not be {@code null}
     * @return a {@link Result} containing either an {@link Either} with a list of {@link Error}s
     *         (in case of validation errors) or the validated {@link PluginMetaData} object
     * @throws NullPointerException if {@code inputStream} is {@code null}
     *
     * @since 1.0.0
     */
    public static Result<Either<List<Error>, PluginMetaData>> loadAndValidatePluginMetadata(final @NonNull InputStream inputStream) {
        Objects.requireNonNull(inputStream, nullValue("inputStream"));
        return loadAndValidate(attempt(inputStream));

    }

    private static Result<Either<List<Error>, PluginMetaData>> loadAndValidate(final Attempt<InputStream> attempt) {
        return attempt.mapUnsafe(OBJECT_MAPPER::readTree)
                .map(node -> {
                    List<Error> errors = SCHEMA.validate(node);
                    if (errors.isEmpty()) {
                        return Either.<List<Error>, PluginMetaData>right(OBJECT_MAPPER.convertValue(node, PluginMetaData.class));
                    } else {
                        return Either.<List<Error>, PluginMetaData>left(errors);
                    }
                })
                .tryIt();
    }

}