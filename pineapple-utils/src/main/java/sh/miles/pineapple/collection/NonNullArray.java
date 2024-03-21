package sh.miles.pineapple.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * An Array wrapper that prevents null elements from occurring
 *
 * @param <E> the type of element
 */
@SuppressWarnings("unchecked")
public class NonNullArray<E> implements Collection<E> {

    private final Object[] objects;
    private final Supplier<E> nullValue;

    /**
     * Creates a new NonNullArray
     *
     * @param size      the size of the array
     * @param nullValue the null value
     */
    public NonNullArray(final int size, @NotNull final Supplier<E> nullValue) {
        this.objects = new Object[size];
        this.nullValue = nullValue;
        for (int i = 0; i < objects.length; i++) {
            objects[i] = this.nullValue.get();
        }
    }

    /**
     * Gets the value at the given index of the array
     *
     * @param index the index to get the item from
     * @return the element at that position
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public E get(final int index) throws IndexOutOfBoundsException {
        verifyIndex(index);
        return (E) objects[index];
    }

    /**
     * Sets an element at the specified index
     *
     * @param index the index to set the value at
     * @param value the value to set
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public void set(final int index, @Nullable final E value) throws IndexOutOfBoundsException {
        if (value == null) {
            objects[index] = (Supplier<E>) this.nullValue.get();
            return;
        }

        objects[index] = value;
    }

    /**
     * Adds a value to an empty position in the list
     *
     * @param value the value to add
     * @return true if the value could be added, otherwise false
     */
    public boolean add(@NotNull final E value) {
        Objects.requireNonNull(value);
        E defaultValue = this.nullValue.get();
        for (int i = 0; i < this.objects.length; i++) {
            Object object = this.objects[i];
            if (defaultValue.equals(object)) {
                this.objects[i] = value;
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the given object
     *
     * @param value the value to remove
     * @return true if the value was removed, otherwise false
     */
    public boolean remove(@NotNull final Object value) {
        Objects.requireNonNull(value);
        for (int i = 0; i < this.objects.length; i++) {
            Object object = this.objects[i];
            if (object.equals(value)) {
                this.objects[i] = this.nullValue.get();
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the element from the given index
     *
     * @param index the index to remove the element from
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public void remove(final int index) throws IndexOutOfBoundsException {
        verifyIndex(index);
        this.objects[index] = this.nullValue.get();
    }

    /**
     * Clears the Array of all values
     */
    public void clear() {
        for (int i = 0; i < objects.length; i++) {
            this.objects[i] = this.nullValue.get();
        }
    }

    /**
     * Uses the contents of the current array to grow it into another array
     * <p>
     * Note this method does not mutate the current NonNullArray instead it grows the array and returns the object with
     * copied values in a grown array.
     *
     * @param growBy how much to grow by
     * @return the new array
     */
    public NonNullArray<E> grow(int growBy) {
        var array = new NonNullArray<>(this.objects.length + growBy, this.nullValue);
        System.arraycopy(objects, 0, array.objects, 0, objects.length);
        return array;
    }

    /**
     * Uses the contents of the current array to shrink it into another array
     * <p>
     * Note this method does not mutate the current NonNullArray instead it shrinks the array and returns the object
     * with copied values in a shrunk array.
     *
     * @param shrinkBy how much to shrink by
     * @return the new array
     */
    public NonNullArray<E> shrink(int shrinkBy) {
        var array = new NonNullArray<>(this.objects.length - shrinkBy, this.nullValue);
        System.arraycopy(objects, 0, array.objects, 0, array.objects.length);
        return array;
    }

    /**
     * Copies the values from this NonNullArray to another
     *
     * @return the new NonNullArray
     */
    public NonNullArray<E> copy() {
        var array = new NonNullArray<>(this.objects.length, this.nullValue);
        System.arraycopy(objects, 0, array.objects, 0, array.objects.length);
        return array;
    }

    /**
     * Copies the values from this NonNullArray to another
     *
     * @param copier the copier to apply in order to apply a deep copy
     * @return the new NonNullArray
     */
    public NonNullArray<E> copy(UnaryOperator<E> copier) {
        var array = new NonNullArray<>(this.objects.length, this.nullValue);
        for (int i = 0; i < array.size(); i++) {
            array.objects[i] = copier.apply((E) this.objects[i]);
        }
        return array;
    }

    /**
     * Gets the NullValue supplier
     *
     * @return the nullValue Supplier
     */
    public Supplier<E> getNullValue() {
        return nullValue;
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c) {
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends E> c) {
        final List<Integer> openIndexes = new ArrayList<>();
        System.out.println(openIndexes);
        var baseValue = this.nullValue.get();
        for (int i = 0; i < this.objects.length; i++) {
            if (objects[i].equals(baseValue)) {
                openIndexes.add(i);
            }
        }

        if (openIndexes.size() < c.size()) {
            return false;
        }
        int index = 0;
        for (final E e : c) {
            var openIndex = openIndexes.get(index);
            set(openIndex, e);
            index++;
        }

        return true;
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c) {
        for (final Object o : c) {
            remove(c);
        }
        return true;
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c) {
        throw new UnsupportedOperationException("retainAll is not currently supported by NonNullArray");
    }

    @Override
    public int size() {
        return this.objects.length;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(final Object o) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return Arrays.stream(this.objects).map(o -> (E) o).iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return Arrays.stream(this.objects).toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull final T[] a) {
        if (a.length < objects.length) {
            return (T[]) Arrays.copyOf(this.objects, this.objects.length, a.getClass());
        }
        System.arraycopy(this.objects, 0, a, 0, this.objects.length);
        if (a.length > objects.length) {
            a[objects.length] = null;
        }
        return a;
    }

    /**
     * Verifies the index given
     *
     * @param index the index to verify
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    private void verifyIndex(final int index) throws IndexOutOfBoundsException {
        if (index >= objects.length || index < 0) {
            throw new IndexOutOfBoundsException("index %d is out of range for array of size %d".formatted(index, objects.length));
        }
    }
}
