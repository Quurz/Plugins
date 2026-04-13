package org.quurz.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.quurz.foomp.base.util.Util.requireNonNullElements;

/**
 * <div>
 *     <p>
 *         A typed value wrapper that holds a value together with its explicit runtime type.
 *     </p>
 *     <p>
 *         This class is particularly useful for reflective invocations where {@code null} values
 *         must be passed and the type information needs to be preserved at runtime. It can also
 *         be used to return values from reflective operations with their runtime type information.
 *     </p>
 *     <p>
 *         Examples:
 *         <pre>
 *             {@code // Passing null with explicit type information
 * TypedValue<String> nullString = TypedValue.typedValue(String.class, null);
 * 
 * // Wrapping a value with its type for reflective operations
 * TypedValue<Integer> result = TypedValue.typedValue(Integer.class, 42);
 * Class<?> resultType = result.getType();  // Integer.class
 * Integer value = result.getValue();        // 42}
 *         </pre>
 *     </p>
 * </div>
 *
 * @param <A> the compile-time type of the typed value's value
 * @since 1.0.0
 * @author Alexander Schell
 */
public final class TypedValue<A> {

    /**
     * <div>
     *     <p>
     *         Creates a new {@link TypedValue} instance with an explicit type and without a value.
     *     </p>
     *     <p>
     *         Useful when only the parameter type needs to be provided for a reflective call
     *         and the value will be set later or {@code null} must be passed.
     *     </p>
     * </div>
     *
     * @param type the explicit, non-null type of the typedValue
     * @param <A>  the generic value type
     * @return a new {@link TypedValue} instance with the type set and no value
     * @throws NullPointerException if {@code type} is {@code null}
     * @since 1.0.0
     */
    public static <A> TypedValue<A> typedValue(final @NonNull Class<? extends A> type) {
        Objects.requireNonNull(type, nullValue("type"));
        return typedValue(type, null);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@link TypedValue} instance with an explicit type and an optional value.
     *     </p>
     *     <p>
     *         This factory method is the general variant to set both type and value.
     *         The type must not be {@code null}; the value may be {@code null}.
     *     </p>
     * </div>
     *
     * @param type  the explicit, non-null type of the typed value
     * @param value the typed value's value; may be {@code null}
     * @param <A>   the generic value type
     * @return a new {@link TypedValue} instance with value and type
     * @throws NullPointerException if {@code type} is {@code null}
     * @since 1.0.0
     */
    public static <A> TypedValue<A> typedValue(final @NonNull Class<? extends A> type,
                                               final @Nullable A value) {
        Objects.requireNonNull(type, nullValue("type"));
        return new TypedValue<>(type, value);
    }

    /**
     * <div>
     *     <p>
     *         Extracts the types and values from an array of {@link TypedValue} objects.
     *     </p>
     *     <p>
     *         The result is a {@link Tuple2} where the first element is an array of the types
     *         and the second element is an array of the corresponding values.
     *     </p>
     *     <p>
     *         This method is suitable for preparing reflective invocations (e.g., constructors
     *         or methods). The order of elements is preserved.
     *     </p>
     * </div>
     *
     * @param arguments a non-null array of {@code TypedValue} objects; must not contain {@code null} elements
     * @return a tuple with two arrays: one of the types and one of the values
     * @throws NullPointerException if {@code arguments} is {@code null}
     * @throws IllegalArgumentException if any element of the array is {@code null}
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public static Tuple2<Class<?>[], Object[]> extractTypesAndValues(final @NonNull TypedValue[] arguments) {
        Objects.requireNonNull(arguments, nullValue("arguments"));
        requireNonNullElements(arguments, "arguments", IllegalArgumentException::new);

        final var types
            = Arrays.stream(arguments)
                .map(TypedValue::getType)
                .toArray(Class<?>[]::new);
        final var values
            = Arrays.stream(arguments)
                .map(TypedValue::getValue)
                .toArray(Object[]::new);

        return tuple2(types, values);
    }

    /**
     * <div>
     *     <p>
     *         Extracts types and values from a list of {@link TypedValue} objects, preserving order.
     *     </p>
     * </div>
     *
     * @param typedValues non-null list without null elements
     * @return tuple of (types[], values[])
     * @throws NullPointerException if typedValues is null
     * @throws IllegalArgumentException if any element is null
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public static Tuple2<Class<?>[], Object[]> extractTypesAndValues(final @NonNull List<TypedValue> typedValues) {
        Objects.requireNonNull(typedValues, nullValue("typedValues"));
        requireNonNullElements(typedValues,"typedValues", IllegalArgumentException::new);

        final var types
            = typedValues.stream()
                .map(TypedValue::getType)
                .toArray(Class<?>[]::new);
        final var values
            = typedValues.stream()
                .map(TypedValue::getValue)
                .toArray(Object[]::new);

        return tuple2(types, values);
    }

    /**
     * <div>
     *     <p>
     *         Extracts types and values from a stream of {@link TypedValue} objects, preserving encounter order.
     *         The stream is fully consumed and processed sequentially.
     *     </p>
     * </div>
     *
     * @param typedValuesStream non-null stream without null elements
     * @return tuple of (types[], values[])
     * @throws NullPointerException if typedValuesStream is null
     * @throws IllegalArgumentException if any element is null
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public static Tuple2<Class<?>[], Object[]> extractTypesAndValues(final @NonNull Stream<TypedValue> typedValuesStream) {
        Objects.requireNonNull(typedValuesStream, nullValue("typedValuesStream"));
        final var argumentsList
            = typedValuesStream.sequential().toList();
        requireNonNullElements(argumentsList, "typedValuesStream", IllegalArgumentException::new);
        return extractTypesAndValues(argumentsList);
    }

    private final Class<? extends A> type;
    private final A value;

    private TypedValue(final Class<? extends A> type,
                       final A value) {
        this.type
            = type;
        this.value
            = value;
    }

    /**
     * <div>
     *     <p>
     *         Returns the explicitly specified type of this typed value.
     *     </p>
     * </div>
     *
     * @return non-null type
     * @since 1.0.0
     */
    @NonNull
    public Class<? extends A> getType() {
        return this.type;
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether a value is set.
     *     </p>
     * </div>
     *
     * @return true if a value is present; false otherwise
     * @since 1.0.0
     */
    public boolean hasValue() {
        return this.value != null;
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether no value is set.
     *     </p>
     * </div>
     *
     * @return true if no value is present; false otherwise
     * @since 1.0.0
     */
    public boolean hasNoValue() {
        return this.value == null;
    }

    /**
     * <div>
     *     <p>
     *         Returns the value held by this typed value (may be {@code null}).
     *     </p>
     * </div>
     *
     * @return the stored value, possibly null
     * @since 1.0.0
     */
    @Nullable
    public A getValue() {
        return this.value;
    }

    /**
     * <div>
     *     <p>
     *         Returns the value held by this typed value as a {@link Maybe}.
     *     </p>
     *     <p>
     *         If no value is set, an empty {@code Maybe} is returned.
     *     </p>
     * </div>
     *
     * @return a Maybe containing the value if present, or empty otherwise
     * @since 1.0.0
     */
    public Maybe<A> getValueSafe() {
        return maybeOfNullable(this.value);
    }

    /**
     * <div>
     *     <p>
     *         Compares this TypedValue to another object for equality.
     *         Two TypedValue instances are equal if both their type and value are equal.
     *     </p>
     * </div>
     *
     * @param o object to compare with
     * @return true if the given object is an equal TypedValue; false otherwise
     * @since 1.0.0
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof TypedValue<?> argument)) return false;

        return Objects.equals(value, argument.value) && type.equals(argument.type);
    }

    /**
     * <div>
     *     <p>
     *         Returns a hash code for this TypedValue based on its type and value.
     *     </p>
     * </div>
     *
     * @return computed hash code
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(value);
        result = 31 * result + type.hashCode();
        return result;
    }

    /**
     * <diV>
     *     <p>
     *         Returns a string representation in the form {@code TypedValue[type=..., value=...]}.
     *     </p>
     * </diV>
     *
     * @return human-readable description of this TypedValue
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", TypedValue.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("value=" + value)
                .toString();
    }

}
