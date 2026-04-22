package org.quurz.plugins.data;

import org.junit.jupiter.api.*;
import org.quurz.foomp.base.misc.SemVer;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.quurz.foomp.base.misc.SemVer.semVer;
import static org.quurz.plugins.data.PluginId.pluginId;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("PluginId")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PluginIdTest {

    private static final Logger LOGGER
        = getLogger(PluginIdTest.class);

    private static final String GROUP_1
        = "org.quurz.plugins";
    private static final String GROUP_2
        = "org.other.plugins";

    private static final String NAME_1
        = "my-plugin";
    private static final String NAME_2
        = "other-plugin";

    private static final SemVer VERSION_1
        = semVer(1, 0, 0);
    private static final SemVer VERSION_2
        = semVer(2, 0, 0);

    private static final PluginId ID_1_V1
        = pluginId(GROUP_1, NAME_1, VERSION_1);

    @Nested
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void rejects_null_group() {
            LOGGER.info("Test PluginId.pluginId(String, String, SemVer): rejects null group");

            assertThatThrownBy(() -> pluginId(null, NAME_1, VERSION_1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("group");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void rejects_null_name() {
            LOGGER.info("Test PluginId.pluginId(String, String, SemVer): rejects null name");

            assertThatThrownBy(() -> pluginId(GROUP_1, null, VERSION_1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("name");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void rejects_null_version() {
            LOGGER.info("Test PluginId.pluginId(String, String, SemVer): rejects null version");

            assertThatThrownBy(() -> pluginId(GROUP_1, NAME_1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("version");
        }

        @Test
        void accepts_valid_arguments() {
            LOGGER.info("Test PluginId.pluginId(String, String, SemVer): accepts valid arguments");

            assertThatNoException()
                .isThrownBy(() -> pluginId(GROUP_1, NAME_1, VERSION_1));
        }

    }

    @Nested
    class Accessors {

        @Test
        void getGroup_returns_group() {
            LOGGER.info("Test pluginId.getGroup()");

            assertThat(ID_1_V1.getGroup())
                .isEqualTo(GROUP_1);
        }

        @Test
        void getName_returns_name() {
            LOGGER.info("Test pluginId.getName()");

            assertThat(ID_1_V1.getName())
                .isEqualTo(NAME_1);
        }

        @Test
        void getVersion_returns_version() {
            LOGGER.info("Test pluginId.getVersion()");

            assertThat(ID_1_V1.getVersion())
                .isEqualTo(VERSION_1);
        }

    }

    @Nested
    class Equality_and_HashCode {

        @Test
        void equals_is_reflexive() {
            LOGGER.info("Test pluginId.equals(Object): reflexivity");
            final var a
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            assertThat(a)
                .isEqualTo(a);
        }

        @Test
        void equals_is_symmetric() {
            LOGGER.info("Test pluginId.equals(Object): symmetry");
            final var a
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            assertThat(a)
                .isEqualTo(b);
            assertThat(b)
                .isEqualTo(a);
        }

        @Test
        void equals_is_transitive() {
            LOGGER.info("Test pluginId.equals(Object): transitivity");
            final var a
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var c
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            assertThat(a)
                .isEqualTo(b);
            assertThat(b)
                .isEqualTo(c);
            assertThat(a)
                .isEqualTo(c);
        }

        @Test
        void equals_is_consistent() {
            LOGGER.info("Test pluginId.equals(Object): consistency");
            final var a1
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var a2
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            assertThat(a1)
                .isEqualTo(a2);
            assertThat(a1)
                .isEqualTo(a2);
        }

        @Test
        void equals_handles_null() {
            LOGGER.info("Test pluginId.equals(Object): null handling");
            assertThat(ID_1_V1)
                .isNotEqualTo(null);
        }

        @Test
        void equals_differs_on_different_group() {
            LOGGER.info("Test pluginId.equals(Object): different group");
            final var a
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b
                = pluginId(GROUP_2, NAME_1, VERSION_1);
            assertThat(a)
                .isNotEqualTo(b);
        }

        @Test
        void equals_differs_on_different_name() {
            LOGGER.info("Test pluginId.equals(Object): different name");
            final var a
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b
                = pluginId(GROUP_1, NAME_2, VERSION_1);
            assertThat(a)
                .isNotEqualTo(b);
        }

        @Test
        void equals_differs_on_different_version() {
            LOGGER.info("Test pluginId.equals(Object): different version");
            final var a
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b
                = pluginId(GROUP_1, NAME_1, VERSION_2);
            assertThat(a)
                .isNotEqualTo(b);
        }

        @Test
        void hashCode_equal_objects_have_same_hash() {
            LOGGER.info("Test pluginId.hashCode(): equal objects must have the same hash code");
            final var a1
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var a2
                = pluginId(GROUP_1, NAME_1, VERSION_1);
            assertThat(a1)
                .isEqualTo(a2);
            assertThat(a1.hashCode())
                .isEqualTo(a2.hashCode());
        }

        @Test
        void hashCode_is_consistent() {
            LOGGER.info("Test pluginId.hashCode(): consistency");
            assertThat(ID_1_V1.hashCode())
                .isEqualTo(ID_1_V1.hashCode());
        }

    }

    @Nested
    class Eq_Contract {

        @Test
        void eq_returns_true_for_same_group_and_name_even_if_version_differs() {
            LOGGER.info("Test pluginId.eq(PluginId): same group and name, different version");
            final var a = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b = pluginId(GROUP_1, NAME_1, VERSION_2);
            
            assertThat(a.eq(b)).isTrue();
        }

        @Test
        void eq_returns_false_for_different_group() {
            LOGGER.info("Test pluginId.eq(PluginId): different group");
            final var a = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b = pluginId(GROUP_2, NAME_1, VERSION_1);
            
            assertThat(a.eq(b)).isFalse();
        }

        @Test
        void eq_returns_false_for_different_name() {
            LOGGER.info("Test pluginId.eq(PluginId): different name");
            final var a = pluginId(GROUP_1, NAME_1, VERSION_1);
            final var b = pluginId(GROUP_1, NAME_2, VERSION_1);
            
            assertThat(a.eq(b)).isFalse();
        }

        @Test
        void eq_rejects_null() {
            LOGGER.info("Test pluginId.eq(PluginId): null handling");
            assertThatThrownBy(() -> ID_1_V1.eq(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("other");
        }

    }

    @Nested
    class ToString_Contract {

        @Test
        void format_is_stable() {
            LOGGER.info("Test pluginId.toString()");

            // Expects: PluginId[group=org.quurz.plugins, name=my-plugin, version=1.0.0]
            // Note: dependent on the mock setup for SemVer.toString()
            assertThat(ID_1_V1.toString())
                .isEqualTo("PluginId[group=%s, name=%s, version=SemVer[major=1, minor=0, patch=0, preRelease=None[], buildMetadata=None[]]]".formatted(GROUP_1, NAME_1));
        }

    }

}
