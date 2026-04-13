package org.quurz.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("PluginMetadataValidator")
class PluginMetadataValidatorTest {

    private static final Logger LOGGER = getLogger(PluginMetadataValidatorTest.class);

    private final PluginMetadataValidator validator = new PluginMetadataValidator();

    @Nested
    @DisplayName("Contracts")
    class Contracts {

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("validate throws NPE if content is null")
        void validate_throws_NPE_on_null() {
            LOGGER.info("Testing validate with null input");
            assertThatThrownBy(() -> validator.validate(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("inputStream"); // Dein Parameter-Name
        }
    }

    @Nested
    @DisplayName("Valid Scenarios")
    class ValidScenarios {

        @Test
        @DisplayName("validate returns empty list for minimal valid JSON")
        void validate_accepts_minimal_json() throws IOException {
            LOGGER.info("Testing minimal valid JSON");
            final var json = """
                    {
                      "name": "MinimalPlugin",
                      "version": "1.0.0",
                      "contract": "com.example.Contract",
                      "implementation": "com.example.Impl"
                    }
                    """;

            final var errors = validator.validate(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("validate returns empty list for full valid JSON")
        void validate_accepts_full_json() throws IOException {
            LOGGER.info("Testing full valid JSON");
            final var json = """
                    {
                      "name": "FullPlugin",
                      "version": "2.1.0-beta+build",
                      "contract": "org.quurz.Contract",
                      "implementation": "org.quurz.Impl",
                      "description": "This is a description"
                    }
                    """;

            final var errors = validator.validate(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
            assertThat(errors).isEmpty();
        }
    }

    @Nested
    @DisplayName("Invalid Scenarios (Schema Violation)")
    class InvalidScenarios {

        @Test
        @DisplayName("validate returns error on missing required field")
        void validate_reports_missing_field() throws IOException {
            LOGGER.info("Testing missing required field");
            final var json = """
                    {
                      "name": "BadPlugin",
                      "contract": "com.example.Contract",
                      "implementation": "com.example.Impl"
                    }
                    """; // 'version' fehlt

            final var errors = validator.validate(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

            assertThat(errors).isNotEmpty();
            // Prüfung, ob der Fehler das Wort "version" enthält (abhängig von der Lib-Implementierung)
            assertThat(errors.getFirst().getMessage()).contains("version");
        }

        @Test
        @DisplayName("validate returns error on invalid version format")
        void validate_reports_bad_version() throws IOException {
            LOGGER.info("Testing invalid version format");
            final var json = """
                    {
                      "name": "BadVerPlugin",
                      "version": "v1.0",
                      "contract": "com.example.Contract",
                      "implementation": "com.example.Impl"
                    }
                    """;

            final var errors = validator.validate(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

            assertThat(errors).isNotEmpty();
            assertThat(errors.getFirst().getInstanceLocation().toString()).contains("version");
        }

        @Test
        @DisplayName("validate returns error on unknown field")
        void validate_reports_unknown_field() throws IOException {
            LOGGER.info("Testing unknown field");
            final var json = """
                    {
                      "name": "UnknownFieldPlugin",
                      "version": "1.0.0",
                      "contract": "com.example.Contract",
                      "implementation": "com.example.Impl",
                      "foobar": "baz"
                    }
                    """;

            final var errors = validator.validate(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

            assertThat(errors).isNotEmpty();
            // Meistens ist das Keyword "additionalProperties" im Fehlertext
            assertThat(errors.getFirst().getMessage()).contains("foobar");
        }
    }

    @Nested
    @DisplayName("Malformed JSON")
    class MalformedJson {

        @Test
        @DisplayName("validate throws IOException (Jackson) on malformed JSON")
        void validate_throws_on_malformed_json() {
            LOGGER.info("Testing malformed JSON");
            final var json = "{ \"name\": \"Broken\" ";

            // Hier wirft Jackson beim readTree() eine Exception, bevor validiert wird
            assertThatThrownBy(() -> validator.validate(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))))
                    .isInstanceOf(IOException.class);
        }
    }
}