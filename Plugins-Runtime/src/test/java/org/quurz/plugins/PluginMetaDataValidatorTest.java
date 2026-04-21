package org.quurz.plugins;

import com.networknt.schema.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.quurz.foomp.base.util.Either;
import org.quurz.foomp.base.util.Result;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("PluginMetadataValidator")
class PluginMetaDataValidatorTest {

    private static final Logger LOGGER = getLogger(PluginMetaDataValidatorTest.class);

    @Nested
    @DisplayName("Contracts")
    class Contracts {

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("loadAndValidatePluginMetadata throws NPE if content is null")
        void validate_throws_NPE_on_null() {
            LOGGER.info("Testing loadAndValidatePluginMetadata with null input");
            assertThatThrownBy(() -> PluginMetaDataValidator.loadAndValidatePluginMetadata((java.io.InputStream) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("inputStream");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("loadAndValidatePluginMetadata throws NPE if Path is null")
        void validate_throws_NPE_on_null_path() {
            assertThatThrownBy(() -> PluginMetaDataValidator.loadAndValidatePluginMetadata((java.nio.file.Path) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("fileName");
        }

        @Test
        @DisplayName("loadAndValidatePluginMetadata throws IllegalArgumentException if file does not exist")
        void validate_throws_IAE_on_non_existent_file() {
            assertThatThrownBy(() -> PluginMetaDataValidator.loadAndValidatePluginMetadata("non_existent_file.json"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not a regular file");
        }
    }

    @Nested
    @DisplayName("Valid Scenarios")
    class ValidScenarios {

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns metadata for minimal valid JSON")
        void validate_accepts_minimal_json() throws IOException {
            LOGGER.info("Testing minimal valid JSON from resource");
            try (var is = getClass().getResourceAsStream("/metadata/valid/minimal.json")) {
                assertThat(is).isNotNull();
                Result<Either<List<Error>, PluginMetaData>> result =
                        PluginMetaDataValidator.loadAndValidatePluginMetadata(is);

                assertThat(result.isSuccess()).isTrue();
                Either<List<Error>, PluginMetaData> either = result.get();
                assertThat(either.isRight()).isTrue();

                PluginMetaData metaData = either.getRight();
                assertThat(metaData.getId().getName()).isEqualTo("MinimalPlugin");
                assertThat(metaData.getId().getVersion().toString()).contains("major=1").contains("minor=0").contains("patch=0");
            }
        }

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns metadata for full valid JSON")
        void validate_accepts_full_json() throws IOException {
            LOGGER.info("Testing full valid JSON from resource");
            try (var is = getClass().getResourceAsStream("/metadata/valid/full.json")) {
                assertThat(is).isNotNull();
                Result<Either<List<Error>, PluginMetaData>> result =
                        PluginMetaDataValidator.loadAndValidatePluginMetadata(is);

                assertThat(result.isSuccess()).isTrue();
                Either<List<Error>, PluginMetaData> either = result.get();
                assertThat(either.isRight()).isTrue();

                PluginMetaData metaData = either.getRight();
                assertThat(metaData.getId().getName()).isEqualTo("FullPlugin");
                assertThat(metaData.getDescription()).isEqualTo("This is a description");
            }
        }

        @Test
        @DisplayName("loadAndValidatePluginMetadata works with Path")
        void validate_works_with_path(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("plugin.json");
            final var json = """
                    {
                      "group": "org.quurz.plugins",
                      "name": "PathPlugin",
                      "version": "1.0.0",
                      "contract": "com.example.Contract",
                      "implementation": "com.example.Impl"
                    }
                    """;
            Files.writeString(file, json);

            Result<Either<List<Error>, PluginMetaData>> result = 
                PluginMetaDataValidator.loadAndValidatePluginMetadata(file);

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get().isRight()).isTrue();
            assertThat(result.get().getRight().getId().getName()).isEqualTo("PathPlugin");
        }
    }

    @Nested
    @DisplayName("Invalid Scenarios (Schema Violation)")
    class InvalidScenarios {

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns left with errors on missing required field")
        void validate_reports_missing_field() throws IOException {
            LOGGER.info("Testing missing required field from resource");
            try (var is = getClass().getResourceAsStream("/metadata/invalid/missing_version.json")) {
                assertThat(is).isNotNull();
                Result<Either<List<Error>, PluginMetaData>> result =
                        PluginMetaDataValidator.loadAndValidatePluginMetadata(is);

                assertThat(result.isSuccess()).isTrue();
                Either<List<Error>, PluginMetaData> either = result.get();
                assertThat(either.isLeft()).isTrue();

                List<Error> errors = either.getLeft();
                assertThat(errors).isNotEmpty();
                assertThat(errors.getFirst().getMessage()).contains("version");
            }
        }

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns left with errors on empty name")
        void validate_reports_empty_name() throws IOException {
            LOGGER.info("Testing empty name from resource");
            try (var is = getClass().getResourceAsStream("/metadata/invalid/empty_name.json")) {
                assertThat(is).isNotNull();
                Result<Either<List<Error>, PluginMetaData>> result =
                        PluginMetaDataValidator.loadAndValidatePluginMetadata(is);

                assertThat(result.isSuccess()).isTrue();
                assertThat(result.get().isLeft()).isTrue();
                assertThat(result.get().getLeft().getFirst().getInstanceLocation().toString()).contains("name");
            }
        }

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns left with errors on invalid class names")
        void validate_reports_bad_class_names() throws IOException {
            LOGGER.info("Testing invalid class names");
            final var json = """
                    {
                      "group": "org.quurz.plugins",
                      "name": "BadClassPlugin",
                      "version": "1.0.0",
                      "contract": "Invalid-Class!",
                      "implementation": "123.Bad"
                    }
                    """;

            Result<Either<List<Error>, PluginMetaData>> result = 
                PluginMetaDataValidator.loadAndValidatePluginMetadata(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get().isLeft()).isTrue();
            List<Error> errors = result.get().getLeft();
            assertThat(errors).hasSizeGreaterThanOrEqualTo(2);
        }

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns left with errors on additional properties")
        void validate_reports_additional_properties() throws IOException {
            LOGGER.info("Testing additional properties");
            final var json = """
                    {
                      "group": "org.quurz.plugins",
                      "name": "ExtraPlugin",
                      "version": "1.0.0",
                      "contract": "com.example.Contract",
                      "implementation": "com.example.Impl",
                      "somethingExtra": "not allowed"
                    }
                    """;

            Result<Either<List<Error>, PluginMetaData>> result = 
                PluginMetaDataValidator.loadAndValidatePluginMetadata(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get().isLeft()).isTrue();
            assertThat(result.get().getLeft().getFirst().getMessage()).contains("somethingExtra");
        }

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns left with errors on invalid version format")
        void validate_reports_bad_version() throws IOException {
            LOGGER.info("Testing invalid version format from resource");
            try (var is = getClass().getResourceAsStream("/metadata/invalid/bad_version.json")) {
                assertThat(is).isNotNull();
                Result<Either<List<Error>, PluginMetaData>> result =
                        PluginMetaDataValidator.loadAndValidatePluginMetadata(is);

                assertThat(result.isSuccess()).isTrue();
                Either<List<Error>, PluginMetaData> either = result.get();
                assertThat(either.isLeft()).isTrue();

                List<Error> errors = either.getLeft();
                assertThat(errors).isNotEmpty();
                assertThat(errors.getFirst().getInstanceLocation().toString()).contains("version");
            }
        }
    }

    @Nested
    @DisplayName("Malformed JSON")
    class MalformedJson {

        @Test
        @DisplayName("loadAndValidatePluginMetadata returns failure on malformed JSON")
        void validate_returns_failure_on_malformed_json() {
            LOGGER.info("Testing malformed JSON");
            final var json = "{ \"name\": \"Broken\" ";

            Result<Either<List<Error>, PluginMetaData>> result = 
                PluginMetaDataValidator.loadAndValidatePluginMetadata(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
            
            assertThat(result.isFailure()).isTrue();
        }
    }
}
