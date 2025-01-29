package sh.miles.pineapple.collection;

/**
 * A Pair data structure which can have one left and right immutable value
 *
 * @param left  the left value
 * @param right the right value
 * @param <L>   the left type
 * @param <R>   the right type
 * @since 1.0.0-SNAPSHOT
 */
public record Pair<L, R>(L left, R right) {

    /**
     * Creates a new Pair
     *
     * @param left  left value
     * @param right right value
     * @param <L>   left type
     * @param <R>   right type
     * @return the pair with the given values
     * @since 1.0.0-SNAPSHOT
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    @Override
    public String toString() {
        return "Pair{" + "left=" + left + ", right=" + right + '}';
    }

}
