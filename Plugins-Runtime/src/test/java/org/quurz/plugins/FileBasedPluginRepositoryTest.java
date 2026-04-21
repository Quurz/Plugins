package org.quurz.plugins;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.misc.SemVer;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;
import static org.quurz.foomp.base.util.Util.toSafeFileName;
import static org.quurz.plugins.FileBasedPluginRepository.fileBasedPluginRepository;
import static org.quurz.plugins.PluginId.pluginId;

class FileBasedPluginRepositoryTest {

    private FileSystem fileSystem;
    private Path baseDir;
    private PluginRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
        baseDir = fileSystem.getPath("/plugins");
        repository = fileBasedPluginRepository(baseDir);
    }

    @AfterEach
    void tearDown() throws Exception {
        fileSystem.close();
    }

    @Test
    void shouldStoreAndRetrievePlugin() throws Exception {
        // Given
        final var pluginId = pluginId("org.quurz.test", "my-plugin", SemVer.semVer(1, 0, 0));
        final var content = "test-content";
        final var inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // When
        repository.store(pluginId, inputStream);

        // Then
        try (final var retrievedStream = repository.retrieve(pluginId)) {
            assertThat(retrievedStream).isNotNull();
            final var retrievedContent = new String(retrievedStream.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(retrievedContent).isEqualTo(content);
        }
    }

    @Test
    void shouldCreateCorrectPathStructure() throws Exception {
        // Given
        final var group = "org.quurz.test";
        final var name = "my-plugin";
        final var version = SemVer.semVer(1, 2, 3);
        final var pluginId = pluginId(group, name, version);
        final var inputStream = new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8));

        // When
        repository.store(pluginId, inputStream);

        // Then
        // We verify that the file is stored and can be retrieved
        assertThat(repository.retrieve(pluginId)).isNotNull();

        // We check the hierarchy: base / group1 / group2 / group3 / name / version / plugin.jar
        Path current = baseDir;
        // The components are split by dots for the group, then name, then version.toString()
        final String[] expectedParts = {"org", "quurz", "test", name, version.toString()};
        
        for (String part : expectedParts) {
            try (var stream = Files.list(current)) {
                var subDirs = stream.filter(Files::isDirectory).toList();
                assertThat(subDirs).hasSize(1);
                current = subDirs.get(0);
                // The directory name should match the safe version of our part
                assertThat(current.getFileName().toString()).isEqualTo(toSafeFileName(part));
            }
        }
        
        assertThat(Files.exists(current.resolve("plugin.jar"))).isTrue();
    }

    @Test
    void shouldThrowExceptionForNonExistentPlugin() throws Exception {
        // Given
        final var pluginId = pluginId("org.test", "none", SemVer.semVer(1, 0, 0));

        // When / Then
        assertThatThrownBy(() -> repository.retrieve(pluginId))
            .isInstanceOf(PluginRepositoryException.class)
            .hasMessageContaining("Unable to find plugin");
    }

    @Test
    void shouldRemovePlugin() throws Exception {
        // Given
        final var pluginId = pluginId("org.test", "remove-me", SemVer.semVer(1, 0, 0));
        repository.store(pluginId, new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8)));
        assertThat(repository.retrieve(pluginId)).isNotNull();

        // When
        repository.remove(pluginId);

        // Then
        assertThatThrownBy(() -> repository.retrieve(pluginId))
            .isInstanceOf(PluginRepositoryException.class);
    }

    @Test
    void shouldThrowExceptionWhenStoringNullPluginId() {
        assertThatThrownBy(() -> repository.store(null, new ByteArrayInputStream(new byte[0])))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenStoringNullInputStream() {
        final var pluginId = pluginId("org.test", "test", SemVer.semVer(1, 0, 0));
        assertThatThrownBy(() -> repository.store(pluginId, null))
            .isInstanceOf(NullPointerException.class);
    }
}
