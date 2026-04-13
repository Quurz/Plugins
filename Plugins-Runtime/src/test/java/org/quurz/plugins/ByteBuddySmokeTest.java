package org.quurz.plugins;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Supplier;

import static org.quurz.plugins.PluginFactory.pluginFactory;
import static org.slf4j.LoggerFactory.getLogger;

class ByteBuddySmokeTest {

    private static final Logger LOGGER
        = getLogger(ByteBuddySmokeTest.class);

    public static final class TestSupplier
            implements Supplier<String> {

        @Override
        public String get() {
            return "<TEST-SUPPLIER>";
        }

    }

    @Test
    void smokeTest() {
        LOGGER.info("Smoke test: Make sure byte-buddy is working correctly");

        final var plugin
            = pluginFactory(
                Supplier.class,
                TestSupplier.class,
                "org.quurz.plugins.test.TestSupplier",
                this.getClass().getClassLoader()
            )
            .construct();

        LOGGER.info("TestSupplier.get: {}", plugin.get());
    }

}
