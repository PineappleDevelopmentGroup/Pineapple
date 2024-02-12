package sh.miles.pineapple.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An Option that can contain or not contain a value
 *
 * @param <E> the entry type
 */
public sealed class Option<E> permits Option.None, Option.Some {

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
        if (!(this instanceof Some<E> some)) {
            throw new IllegalStateException("Unable to find some value");
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

        Some(E some) {
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
    }

}
