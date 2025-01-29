package sh.miles.pineapple.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An Option that can contain or not contain a value
 *
 * @param <E> the entry type
 */
public sealed class Option<E> permits Option.None, Option.Some {

    /**
     * Commits an operation given that an option is present
     *
     * @param consumer the operation on present
     */
    public void ifPresent(@NotNull final Consumer<E> consumer) {
        if (this instanceof Option.Some<E> some) {
            consumer.accept(some.some);
        }
    }

    /**
     * Maps the given option to another value if available. Otherwise None is returned
     *
     * @param mapper the mapper
     * @param <R>    the returned type
     * @return the new option
     */
    public <R> Option<R> map(@NotNull final Function<E, R> mapper) {
        if (!(this instanceof Some<E> some)) {
            return new None<>();
        }

        return new Some<>(mapper.apply(some.some));
    }

    /**
     * Gets some value or throws
     *
     * @return the value
     * @throws IllegalStateException if no value was found
     */
    public E orThrow() throws IllegalStateException {
        return orThrow("Unable to find some value");
    }

    /**
     * Gets some value or throws with the given message
     *
     * @param message the message to throw with
     * @return the value
     * @throws IllegalStateException if no value was found
     */
    public E orThrow(String message) throws IllegalStateException {
        if (!(this instanceof Some<E> some)) {
            throw new IllegalStateException(message);
        }

        return some.some;
    }

    /**
     * Gets some value or the other value
     *
     * @param value the non null value to use instead
     * @return the value
     */
    @NotNull
    public E orElse(@NotNull final E value) {
        if (!(this instanceof Some<E> some)) {
            return Objects.requireNonNull(value);
        }

        return some.some;
    }

    /**
     * Gets some value or runs the supplying function
     *
     * @param value the value function to run
     * @return the value
     */
    @NotNull
    public E orElse(@NotNull final Supplier<E> value) {
        if (!(this instanceof Some<E> some)) {
            return Objects.requireNonNull(value.get());
        }

        return some.some;
    }

    /**
     * Creates Some Option
     *
     * @param value the value
     * @param <E>   the entry type
     * @return the Option
     */
    public static <E> Option<E> some(@NotNull final E value) {
        return new Some<>(value);
    }

    /**
     * Creates None Option
     *
     * @param <E> the entry type
     * @return the Option
     */
    public static <E> Option<E> none() {
        return new None<>();
    }

    /**
     * Represents some value
     *
     * @param <E> the entry type
     */
    public static final class Some<E> extends Option<E> {

        private final E some;

        public Some(E some) {
            this.some = Objects.requireNonNull(some);
        }

        public E some() {
            return this.some;
        }

    }

    /**
     * Represents no value
     *
     * @param <E> arbitrary type
     */
    public static final class None<E> extends Option<E> {
        public None() {
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Option.None<?>;
        }
    }

}
