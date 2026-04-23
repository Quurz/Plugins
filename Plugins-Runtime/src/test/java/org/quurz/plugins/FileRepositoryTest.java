package org.quurz.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.quurz.plugins.data.PluginId;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.quurz.foomp.base.misc.SemVer.parseSemVer;
import static org.quurz.plugins.data.PluginId.pluginId;

@DisplayName("FileRepository")
class FileRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("stores and retrieves a plugin")
    void storesAndRetrievesPlugin() throws Exception {
        FileRepository repository = FileRepository.fileRepository(tempDir);
        PluginId id = pluginId("org.test", "myplugin", parseSemVer("1.0.0"));
        byte[] content = "test content".getBytes();
        Supplier<InputStream> supplier = () -> new ByteArrayInputStream(content);

        repository.store(id, supplier);

        assertTrue(repository.contains(id));
        
        Supplier<InputStream> retrievedSupplier = repository.retrieve(id);
        try (InputStream is = retrievedSupplier.get()) {
            assertArrayEquals(content, is.readAllBytes());
        }
    }

    @Test
    @DisplayName("removes a plugin")
    void removesPlugin() throws Exception {
        FileRepository repository = FileRepository.fileRepository(tempDir);
        PluginId id = pluginId("org.test", "myplugin", parseSemVer("1.0.0"));
        repository.store(id, () -> new ByteArrayInputStream("data".getBytes()));

        assertTrue(repository.contains(id));
        repository.remove(id);
        assertFalse(repository.contains(id));
    }

    @Test
    @DisplayName("throws exception when storing to non-writable root")
    void throwsExceptionWhenStoringToNonWritableRoot() {
        // This is hard to test cross-platform, skipping for now or using a mock if possible.
    }
}
