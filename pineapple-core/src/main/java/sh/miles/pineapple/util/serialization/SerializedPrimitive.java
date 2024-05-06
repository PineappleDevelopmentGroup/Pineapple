package sh.miles.pineapple.util.serialization;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.exception.InvalidSerializedTypeException;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * A SerializedPrimitive that can be one of a few "primitive" serialized type.
 * <p>
 * Types considered primitives are, int, double, long, string, boolean
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class SerializedPrimitive extends SerializedElement {

    private final Object object;

    SerializedPrimitive(@NotNull final Object object) {
        Preconditions.checkArgument(object != null, "The given object mustn't be null");
        this.object = object;
        Preconditions.checkArgument(isAny(), "The given object does not match any required primitive type. Found type %s".formatted(object.getClass()));
    }

    /**
     * Gets this primitive as a int
     *
     * @return the int
     * @throws InvalidSerializedTypeException thrown if the type is not actually an int
     * @since 1.0.0-SNAPSHOT
     */
    public int getAsInt() throws InvalidSerializedTypeException {
        if (this.object instanceof Integer || this.object.getClass().isAssignableFrom(int.class)) {
            return (int) this.object;
        }
        throw new InvalidSerializedTypeException(int.class, this.object.getClass());
    }

    /**
     * Gets this primitive as a double
     *
     * @return the string
     * @throws InvalidSerializedTypeException thrown if the type is not actually a double
     * @since 1.0.0-SNAPSHOT
     */
    public double getAsDouble() throws InvalidSerializedTypeException {
        if (this.object instanceof Double || this.object.getClass().isAssignableFrom(double.class)) {
            return (double) this.object;
        }
        throw new InvalidSerializedTypeException(double.class, this.object.getClass());
    }

    /**
     * Gets this primitive as a long
     *
     * @return the string
     * @throws InvalidSerializedTypeException thrown if the type is not actually a long
     * @since 1.0.0-SNAPSHOT
     */
    public long getAsLong() throws InvalidSerializedTypeException {
        if (this.object instanceof Long || this.object.getClass().isAssignableFrom(long.class)) {
            return (long) this.object;
        }

        throw new InvalidSerializedTypeException(long.class, this.object.getClass());
    }

    /**
     * Gets this primitive as a boolean
     *
     * @return the string
     * @throws InvalidSerializedTypeException thrown if the type is not actually a boolean
     * @since 1.0.0-SNAPSHOT
     */
    public boolean getAsBoolean() throws InvalidSerializedTypeException {
        if (this.object instanceof Boolean || this.object.getClass().isAssignableFrom(boolean.class)) {
            return (boolean) this.object;
        }

        throw new InvalidSerializedTypeException(boolean.class, this.object.getClass());
    }

    /**
     * Gets this primitive as a string
     *
     * @return the string
     * @throws InvalidSerializedTypeException thrown if the type is not actually a string
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public String getAsString() throws InvalidSerializedTypeException {
        if (this.object instanceof String) {
            return (String) this.object;
        }
        throw new InvalidSerializedTypeException(String.class, this.object.getClass());
    }

    /**
     * Determines whether or not this SerializedPrimitive is an int
     *
     * @return true if this serialized primitive is an int
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isInt() {
        return this.object instanceof Integer || this.object.getClass().isAssignableFrom(int.class);
    }

    /**
     * Determines whether or not this SerializedPrimitive is a double
     *
     * @return true if this serialized primitive is a double
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isDouble() {
        return this.object instanceof Double || this.object.getClass().isAssignableFrom(double.class);
    }

    /**
     * Determines whether or not this SerializedPrimitive is a long
     *
     * @return true if this serialized primitive is a long
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isLong() {
        return this.object instanceof Long || this.object.getClass().isAssignableFrom(long.class);
    }

    /**
     * Determines whether or not this SerializedPrimitive is a boolean
     *
     * @return true if this serialized primitive is a boolean
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isBoolean() {
        return this.object instanceof Boolean || this.object.getClass().isAssignableFrom(boolean.class);
    }

    /**
     * Determines whether or not this SerializedPrimitive is a string
     *
     * @return true if this serialized primitive is a string
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isString() {
        return this.object instanceof String;
    }

    private boolean isAny() {
        return isString() || isBoolean() || isLong() || isDouble() || isInt();
    }

    /**
     * Gets the type of the primitive
     *
     * @return the type
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Type getTypeOfPrimitive() {
        return this.object.getClass();
    }

    @Override
    public boolean equals(final Object object1) {
        if (this == object1) return true;
        if (!(object1 instanceof final SerializedPrimitive that)) return false;
        return Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }

    @Override
    public String toString() {
        return "Primitive(%s)".formatted(object);
    }
}
