package org.quurz.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.plugins.TypedValue.typedValue;
import static org.quurz.plugins.TypedValue.extractTypesAndValues;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("TypedValue")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TypedValueTest {

    private static final Logger LOGGER
        = getLogger(TypedValueTest.class);

    @SuppressWarnings("rawtypes")
    private static final TypedValue[] INVALID_ARGUMENTS_ARRAY
        = new TypedValue<?>[] {
            TypedValue.typedValue(String.class),
            null
        };

    @SuppressWarnings("rawtypes")
    private static final TypedValue[] VALID_ARGUMENTS_ARRAY
        = new TypedValue<?>[] {
            TypedValue.typedValue(String.class),
            typedValue(Integer.class, 123)
        };

    @SuppressWarnings("rawtypes")
    private static final List<TypedValue> INVALID_ARGUMENTS_LIST
        = Arrays.stream(INVALID_ARGUMENTS_ARRAY)
            .toList();

    @SuppressWarnings("rawtypes")
    private static final List<TypedValue> VALID_ARGUMENTS_LIST
        = Arrays.stream(VALID_ARGUMENTS_ARRAY)
            .toList();

    private static final TypedValue<String> ARGUMENT_WITH_VALUE
        = typedValue(String.class, "<TEST>");
    private static final TypedValue<Integer> ARGUMENT_WITHOUT_VALUE
        = TypedValue.typedValue(Integer.class);

    @Nested
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void argument_without_value_rejects_null_type() {
            LOGGER.info("Test TypedValue.typedValue(Class): rejects null type");

            assertThatThrownBy(() -> TypedValue.typedValue(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("type");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void argument_with_value_rejects_null_type() {
            LOGGER.info("Test TypedValue.typedValue(Class, A): rejects null type");

            assertThatThrownBy(() -> typedValue(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("type");
            assertThatThrownBy(() -> typedValue(null, new Object()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("type");
        }

        @Test
        void argument_without_value_accepts_valid_type() {
            LOGGER.info("Test TypedValue.typedValue(Class): accepts valid type");

            assertThatNoException()
                .isThrownBy(() -> TypedValue.typedValue(String.class));
        }

        @Test
        void argument_with_value_accepts_valid_type() {
            LOGGER.info("Test TypedValue.typedValue(Class, A): accepts valid type and non-null value");

            assertThatNoException()
                .isThrownBy(() -> typedValue(String.class, "<TEST>"));
        }

        @Test
        void argument_with_value_accepts_null_value() {
            LOGGER.info("Test TypedValue.typedValue(Class, A): accepts null value");

            assertThatNoException()
                .isThrownBy(() -> typedValue(String.class, null));
        }

    }

    @Nested
    class Extraction {

        @Test
        void extractTypesAndValues_validates_inputs_and_succeeds_otherwise_for_arrays() {
            LOGGER.info("Test TypedValue.extractTypesAndValues(TypedValue<?>[])");

            assertThatThrownBy(() -> extractTypesAndValues((TypedValue<?>[]) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("arguments");
            assertThatThrownBy(() -> extractTypesAndValues(INVALID_ARGUMENTS_ARRAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("arguments")
                .hasMessageContaining("1");

            assertThatNoException().isThrownBy(() -> extractTypesAndValues(VALID_ARGUMENTS_ARRAY));
        }

        @SuppressWarnings({"rawtypes", "DataFlowIssue"})
        @Test
        void extractTypesAndValues_validates_inputs_and_succeeds_otherwise_for_lists() {
            LOGGER.info("Test TypedValue.extractTypesAndValues(Collection<TypedValue<?>>)");

            assertThatThrownBy(() -> extractTypesAndValues((List<TypedValue>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("typedValues");
            assertThatThrownBy(() -> extractTypesAndValues(INVALID_ARGUMENTS_LIST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("typedValues")
                .hasMessageContaining("1");

            assertThatNoException()
                .isThrownBy(() -> extractTypesAndValues(VALID_ARGUMENTS_LIST));
        }

        @SuppressWarnings({"rawtypes", "DataFlowIssue"})
        @Test
        void extractTypesAndValues_validates_inputs_and_succeeds_otherwise_for_streams() {
            LOGGER.info("Test TypedValue.extractTypesAndValues(Stream<TypedValue<?>>)");

            assertThatThrownBy(() -> extractTypesAndValues((Stream<TypedValue>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("typedValues");
            assertThatThrownBy(() -> extractTypesAndValues(INVALID_ARGUMENTS_LIST.stream()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("typedValues")
                .hasMessageContaining("1");

            assertThatNoException()
                .isThrownBy(() -> extractTypesAndValues(VALID_ARGUMENTS_LIST.stream()));
        }
    }

    @Nested
    class Value_Flags {

        @Test
        void hasValue_and_hasNoValue_behave_consistently() {
            LOGGER.info("Test typedValue.hasValue() and typedValue.hasNoValue()");

            assertThat(ARGUMENT_WITH_VALUE.hasValue())
                .isTrue();
            assertThat(ARGUMENT_WITH_VALUE.hasNoValue())
                .isFalse();
            assertThat(ARGUMENT_WITHOUT_VALUE.hasValue())
                .isFalse();
            assertThat(ARGUMENT_WITHOUT_VALUE.hasNoValue())
                .isTrue();
        }
    }

    @Nested
    class Accessors {

        @Test
        void getType_returns_explicit_type() {
            LOGGER.info("Test typedValue.getType()");

            assertThat(ARGUMENT_WITH_VALUE.getType())
                .isEqualTo(String.class);
            assertThat(ARGUMENT_WITHOUT_VALUE.getType())
                .isEqualTo(Integer.class);
        }

        @Test
        void getValue_returns_value_or_null() {
            LOGGER.info("Test typedValue.getValue()");

            assertThat(ARGUMENT_WITH_VALUE.getValue())
                .isEqualTo("<TEST>");
            assertThat(ARGUMENT_WITHOUT_VALUE.getValue())
                .isNull();
        }

        @Test
        void getValueSafe_returns_some_or_none() {
            LOGGER.info("Test typedValue.getValueSafe()");

            assertThat(ARGUMENT_WITH_VALUE.getValueSafe().isSome())
                .isTrue();
            assertThat(ARGUMENT_WITHOUT_VALUE.getValueSafe().isNone())
                .isTrue();
        }
    }

    @Nested
    class Equality_and_HashCode {

        @Test
        void equals_is_reflexive() {
            LOGGER.info("Test typedValue.equals(Object): reflexivity");
            final var a
                = typedValue(String.class, "<TEST>");
            assertThat(a)
                .isEqualTo(a);
        }

        @Test
        void equals_is_symmetric() {
            LOGGER.info("Test typedValue.equals(Object): symmetry");
            final var a
                = typedValue(String.class, "<TEST>");
            final var b
                = typedValue(String.class, "<TEST>");
            assertThat(a)
                .isEqualTo(b);
            assertThat(b)
                .isEqualTo(a);
        }

        @Test
        void equals_is_transitive() {
            LOGGER.info("Test typedValue.equals(Object): transitivity");
            final var a
                = typedValue(String.class, "<TEST>");
            final var b
                = typedValue(String.class, "<TEST>");
            final var c
                = typedValue(String.class, "<TEST>");
            assertThat(a)
                .isEqualTo(b);
            assertThat(b)
                .isEqualTo(c);
            assertThat(a)
                .isEqualTo(c);
        }

        @Test
        void equals_is_consistent() {
            LOGGER.info("Test typedValue.equals(Object): consistency");
            final var a1
                = typedValue(String.class, "<TEST>");
            final var a2
                = typedValue(String.class, "<TEST>");
            assertThat(a1)
                .isEqualTo(a2);
            assertThat(a1)
                .isEqualTo(a2); // repeated comparison should yield the same result
        }

        @Test
        void equals_handles_null() {
            LOGGER.info("Test typedValue.equals(Object): null handling");
            final var a = typedValue(String.class, "<TEST>");
            assertThat(a).isNotEqualTo(null);
        }

        @Test
        void equals_differs_on_different_value_with_same_type() {
            LOGGER.info("Test typedValue.equals(Object): different value, same type");
            final var a
                = typedValue(String.class, "<TEST>");
            final var b
                = typedValue(String.class, "OTHER");
            assertThat(a)
                .isNotEqualTo(b);
        }

        @Test
        void equals_differs_on_different_type_even_if_values_match_semantically() {
            LOGGER.info("Test typedValue.equals(Object): different type");
            final var a
                = typedValue(String.class, "<123>");
            final var c
                = typedValue(Integer.class, 123);
            assertThat(a)
                .isNotEqualTo(c);
        }

        @Test
        void equals_with_null_values_respects_type() {
            LOGGER.info("Test typedValue.equals(Object): null values and type");
            final var n1
                = typedValue(String.class, null);
            final var n2
                = typedValue(String.class, null);
            final var n3
                = typedValue(Integer.class, null);

            assertThat(n1)
                .isEqualTo(n2);    // same type + both null -> equal
            assertThat(n1)
                .isNotEqualTo(n3); // different type + both null -> not equal
        }

        @Test
        void hashCode_equal_objects_have_same_hash() {
            LOGGER.info("Test typedValue.equals(Object): equal objects must have the same hash code");
            final var a1
                = typedValue(String.class, "<TEST>");
            final var a2
                = typedValue(String.class, "<TEST>");
            assertThat(a1)
                .isEqualTo(a2);
            assertThat(a1.hashCode())
                .isEqualTo(a2.hashCode());
        }

        @Test
        void hashCode_is_consistent() {
            LOGGER.info("Test typedValue.hashCode: consistency");
            final var a = typedValue(String.class, "<TEST>");
            assertThat(a.hashCode()).isEqualTo(a.hashCode());
        }

    }

    @Nested
    class ToString_Contract {

        @Test
        void format_is_stable() {
            LOGGER.info("Test typedValue.toString()");

            assertThat(ARGUMENT_WITH_VALUE.toString())
                .isEqualTo("TypedValue[type=class java.lang.String, value=<TEST>]");
            assertThat(ARGUMENT_WITHOUT_VALUE.toString())
                .isEqualTo("TypedValue[type=class java.lang.Integer, value=null]");
        }

    }

}