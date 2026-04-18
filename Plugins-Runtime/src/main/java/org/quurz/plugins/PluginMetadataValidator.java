package org.quurz.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.Error;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Validates plugin metadata JSON strings against the defined JSON Schema.
 *     </p>
 * </div>
 */
class PluginMetadataValidator {

    private static final String SCHEMA_FILE = "/PluginDescriptionSchema.json";

    private final Schema schema;
    private final ObjectMapper objectMapper;

    PluginMetadataValidator() {
        this.objectMapper
            = new ObjectMapper();
        final SchemaRegistry schemaRegistry
            = SchemaRegistry
                .withDefaultDialect(SpecificationVersion.DRAFT_7);
        this.schema
            = schemaRegistry.getSchema(this.getClass().getResourceAsStream(SCHEMA_FILE));
    }

    /**
     * <div>
     *     <p>
     *         Validates the JSON content from the given input stream.
     *     </p>
     *     <p>
     *         Returns a list of validation errors. If the list is empty, the JSON content
     *         is considered valid according to the schema.
     *     </p>
     * </div>
     *
     * @param inputStream the input stream containing the JSON to validate; must not be {@code null}
     * @return a list of validation errors; empty if valid
     * @throws NullPointerException if {@code inputStream} is {@code null}
     * @throws IOException          if an I/O error occurs or the JSON content is malformed
     */
    List<Error> validate(final @NonNull InputStream inputStream)
            throws IOException {
        Objects.requireNonNull(inputStream, nullValue("inputStream"));
        return this.schema.validate(this.objectMapper.readTree(inputStream));
    }

}