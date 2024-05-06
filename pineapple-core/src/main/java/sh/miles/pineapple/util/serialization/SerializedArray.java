package sh.miles.pineapple.util.serialization;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Represents a SerializedArray of elements.
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class SerializedArray extends SerializedElement implements Iterable<SerializedElement> {
    private final ArrayList<SerializedElement> elements;

    SerializedArray() {
        this.elements = new ArrayList<>();
    }

    SerializedArray(final int capacity) {
        this.elements = new ArrayList<>(capacity);
    }

    /**
     * Adds a new primitive to the list
     *
     * @param primitive the primitive to add
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final SerializedElement primitive) {
        Preconditions.checkArgument(primitive != null, "the given primitive must not be null");
        this.elements.add(primitive);
    }

    /**
     * Adds a string to the array
     *
     * @param string the string to add
     * @since 1.0.0-SNAPSHOT
     */
    public void add(@NotNull final String string) {
        this.elements.add(new SerializedPrimitive(string));
    }

    /**
     * Adds a new number to the list
     *
     * @param integer the number to add
     * @since 1.0.0-SNAPSHOT
     */
    public void add(final int integer) {
        this.elements.add(new SerializedPrimitive(integer));
    }

    /**
     * Adds a new decimal to the list
     *
     * @param decimal the decimal to add
     * @since 1.0.0-SNAPSHOT
     */
    public void add(final double decimal) {
        this.elements.add(new SerializedPrimitive(decimal));
    }

    /**
     * Adds a long to the list
     *
     * @param int64 the long
     * @since 1.0.0-SNAPSHOT
     */
    public void add(final long int64) {
        this.elements.add(new SerializedPrimitive(int64));
    }

    /**
     * Adds a boolean to the list
     *
     * @param bool the  boolean
     * @since 1.0.0-SNAPSHOT
     */
    public void add(final boolean bool) {
        this.elements.add(new SerializedPrimitive(bool));
    }

    /**
     * Checks if the list contains the given element
     *
     * @param primitive the primitive to add
     * @return true if it is contained, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    public boolean contains(@NotNull final SerializedElement primitive) {
        Preconditions.checkArgument(primitive != null, "The given primitive must not be null");
        return this.elements.contains(primitive);
    }

    @NotNull
    @Override
    public Iterator<SerializedElement> iterator() {
        return elements.iterator();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final SerializedArray array)) {
            return false;
        }
        return Objects.equals(elements, array.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Array(").append(this.elements.size()).append(") {").append("\n");
        for (final SerializedElement element : elements) {
            builder.append(element).append(",\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
