package sh.miles.pineapple.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A Class that can contain either the left or right value
 *
 * @param <L> the left value
 * @param <R> the right value
 */
public sealed class Either<L, R> permits Either.Left, Either.Right {

    /**
     * Computes a function if the type is left
     *
     * @param computer the computer
     * @return this either
     */
    public Either<L, R> computeIfLeft(Consumer<L> computer) {
        if (this instanceof Either.Left<L, R> left) {
            computer.accept(left.value);
        }
        return this;
    }

    /**
     * Computes a function if the type is right
     *
     * @param computer the computer
     * @return this either
     */
    public Either<L, R> computeIfRight(Consumer<R> computer) {
        if (this instanceof Either.Right<L, R> right) {
            computer.accept(right.value);
        }
        return this;
    }

    /**
     * Computes a left and right function depending on the contained value
     *
     * @param leftComputer  the left computer
     * @param rightComputer the right computer
     */
    public void compute(Consumer<L> leftComputer, Consumer<R> rightComputer) {
        if (this instanceof Either.Left<L, R> left) {
            leftComputer.accept(left.value);
        } else {
            rightComputer.accept(((Right<L, R>) this).value);
        }
    }

    /**
     * Gets the left value or throws
     *
     * @return the left value
     * @throws IllegalStateException thrown if no left value was found
     */
    public L leftOrThrow() throws IllegalStateException {
        if (this instanceof Either.Left<L, R> left) {
            return left.value;
        }

        throw new IllegalStateException("Could not find left value");
    }

    /**
     * Gets the right value or throws
     *
     * @return the right value
     * @throws IllegalStateException thrown if no right value was found
     */
    public R rightOrThrow() throws IllegalStateException {
        if (this instanceof Either.Right<L, R> right) {
            return right.value;
        }

        throw new IllegalStateException("Could not find right value");
    }

    /**
     * Gets whether the value is on the left
     *
     * @return true if on left
     */
    public boolean isLeft() {
        return this instanceof Either.Left<L, R>;
    }

    /**
     * Gets whether the value is on the right
     *
     * @return true if on right
     */
    public boolean isRight() {
        return this instanceof Either.Right<L, R>;
    }

    /**
     * Gets a possible left value
     *
     * @return the option
     */
    public Option<L> left() {
        if (this instanceof Either.Left<L, R> left) {
            return Option.some(left.value);
        }
        return Option.none();
    }

    /**
     * Gets a possible right value
     *
     * @return the option
     */
    public Option<R> right() {
        if (this instanceof Either.Right<L, R> right) {
            return Option.some(right.value);
        }
        return Option.none();
    }

    /**
     * Creates a Left Either
     *
     * @param left the left
     * @param <L>  left type
     * @param <R>  right type
     * @return the Either
     */
    public static <L, R> Either<L, R> left(@NotNull final L left) {
        return new Left<>(left);
    }

    /**
     * Creates a Right Either
     *
     * @param right the right
     * @param <L>   left type
     * @param <R>   right type
     * @return the Either
     */
    public static <L, R> Either<L, R> right(@NotNull final R right) {
        return new Right<>(right);
    }

    public static final class Left<L, R> extends Either<L, R> {
        private final L value;

        Left(@NotNull final L left) {
            this.value = Objects.requireNonNull(left);
        }

        public L value() {
            return this.value;
        }
    }

    public static final class Right<L, R> extends Either<L, R> {
        private final R value;

        Right(@NotNull final R right) {
            this.value = Objects.requireNonNull(right);
        }

        public R value() {
            return this.value;
        }
    }
}
