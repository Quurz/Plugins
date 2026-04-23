package org.quurz.plugins.internal;

import net.bytebuddy.ByteBuddy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StreamClassLoader")
class StreamClassLoaderTest {

    @Test
    @DisplayName("loads class from InputStream")
    void loadsClassFromInputStream() throws Exception {
        String className = "test.GeneratedClass";
        byte[] classBytes = new ByteBuddy()
                .subclass(Object.class)
                .name(className)
                .make()
                .getBytes();

        byte[] jarData = createTestJar(className.replace('.', '/') + ".class", classBytes);
        
        try (InputStream is = new ByteArrayInputStream(jarData)) {
            StreamClassLoader loader = new StreamClassLoader(is);
            Class<?> clazz = loader.loadClass(className);
            
            assertNotNull(clazz);
            assertEquals(className, clazz.getName());
            assertSame(loader, clazz.getClassLoader());
        }
    }

    @Test
    @DisplayName("loads resource from InputStream")
    void loadsResourceFromInputStream() throws Exception {
        byte[] jarData = createTestJar("resource.txt", "Hello World".getBytes());
        
        try (InputStream is = new ByteArrayInputStream(jarData)) {
            StreamClassLoader loader = new StreamClassLoader(is);
            InputStream resourceStream = loader.getResourceAsStream("resource.txt");
            
            assertNotNull(resourceStream);
            String content = new String(resourceStream.readAllBytes());
            assertEquals("Hello World", content);
        }
    }

    private byte[] createTestJar(String entryName, byte[] content) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (JarOutputStream jos = new JarOutputStream(baos)) {
            jos.putNextEntry(new JarEntry(entryName));
            jos.write(content);
            jos.closeEntry();
        }
        return baos.toByteArray();
    }
}
