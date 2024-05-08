package sh.miles.pineapple.util.serialization;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.exception.InvalidSerializedTypeException;

/**
 * An Element in a Serialization Tree
 *
 * @since 1.0.0-SNAPSHOT
 */
public sealed class SerializedElement permits SerializedArray, SerializedObject, SerializedPrimitive {

    /**
     * Gets a SerializedPrimitive from this SerializedElement
     *
     * @return this as a SerializedPrimitive
     * @throws InvalidSerializedTypeException thrown if this is not actually a SerializedPrimitive
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public SerializedPrimitive getAsPrimitive() throws InvalidSerializedTypeException {
        if (this instanceof SerializedPrimitive) {
            return (SerializedPrimitive) this;
        }

        throw new InvalidSerializedTypeException(SerializedPrimitive.class, this.getClass());
    }

    /**
     * Gets a SerializedArray from this SerializedElement
     *
     * @return this as a SerializedArray
     * @throws InvalidSerializedTypeException thrown if this is not actually a SerializedArray
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public SerializedArray getAsArray() throws InvalidSerializedTypeException {
        if (this instanceof SerializedArray) {
            return (SerializedArray) this;
        }

        throw new InvalidSerializedTypeException(SerializedPrimitive.class, this.getClass());
    }

    /**
     * Gets a SerializedObject from this SerializedElement
     *
     * @return this as a SerializedObject
     * @throws InvalidSerializedTypeException thrown if this is not actually a SerializedObject
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public SerializedObject getAsObject() throws InvalidSerializedTypeException {
        if (this instanceof SerializedObject) {
            return (SerializedObject) this;
        }

        throw new InvalidSerializedTypeException(SerializedObject.class, this.getClass());
    }

    /**
     * Checks if this is a primitive
     *
     * @return true if this is a primitive
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isPrimitive() {
        return this instanceof SerializedPrimitive;
    }

    /**
     * Checks if this is a array
     *
     * @return true if this is a array
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isArray() {
        return this instanceof SerializedArray;
    }

    /**
     * Checks if this is a object
     *
     * @return true if this is a object
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isObject() {
        return this instanceof SerializedObject;
    }

    /**
     * Creates a new SerializedObject
     *
     * @return the new object
     * @since 1.0.0_SNAPSHOT
     */
    @NotNull
    public static SerializedObject object() {
        return new SerializedObject();
    }

    /**
     * Creates a new SerializedArray
     *
     * @return the new array
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static SerializedArray array() {
        return new SerializedArray();
    }

    /**
     * Creates a new SerializedArray
     *
     * @param capacity the initial capacity
     * @return the new array
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static SerializedArray array(int capacity) {
        return new SerializedArray();
    }

    /**
     * Creates a new SerializedPrimitive
     *
     * @param integer the integer
     * @return the new primitive
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static SerializedPrimitive primitive(int integer) {
        return new SerializedPrimitive(integer);
    }

    /**
     * Creates a new SerializedPrimitive
     *
     * @param decimal the double
     * @return the new primitive
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static SerializedPrimitive primitive(double decimal) {
        return new SerializedPrimitive(decimal);
    }

    /**
     * Creates a new SerializedPrimitive
     *
     * @param int64 the long
     * @return the new primitive
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static SerializedPrimitive primitive(long int64) {
        return new SerializedPrimitive(int64);
    }

    /**
     * Creates a new SerializedPrimitive
     *
     * @param bool the boolean
     * @return the new primitive
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static SerializedPrimitive primitive(boolean bool) {
        return new SerializedPrimitive(bool);
    }

    /**
     * Creates a new SerializedPrimitive
     *
     * @param string the string
     * @return the new primitive
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static SerializedPrimitive primitive(@NotNull final String string) {
        return new SerializedPrimitive(string);
    }

    /**
     * Creates a new SerializedPrimitive.
     * <p>
     * The use of this method is inherently unsafe. The other primitive methods allow for type checking to ensure no
     * runtime errors can occur. This throws all of that into the air and can not guarantee the object works until
     * runtime.
     * <p>
     * Ignore this warning if you find yourself using this method to develop new bridges. This method is inherently
     * useful in some cases when developing bridges and exists for such cases
     *
     * @param object the object to use
     * @return the new primitive
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    @NotNull
    public static SerializedPrimitive primitive(@NotNull final Object object) {
        return new SerializedPrimitive(object);
    }
}
