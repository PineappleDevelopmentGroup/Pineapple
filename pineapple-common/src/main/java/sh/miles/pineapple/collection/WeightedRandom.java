package sh.miles.pineapple.collection;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * Weighted random implementation
 *
 * @param <T> the type
 * @since 1.0.0-SNAPSHOT
 */
public class WeightedRandom<T> {
    private final NavigableMap<Double, T> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    /**
     * Creates a new weighted random
     *
     * @since 1.0.0-SNAPSHOT
     */
    public WeightedRandom() {
        this(new Random());
    }

    /**
     * Creates a new weighted random from the provided Random instance
     *
     * @param random the random instance
     * @since 1.0.0-SNAPSHOT
     */
    public WeightedRandom(Random random) {
        this.random = random;
    }

    /**
     * Adds a new result with the specified weight
     *
     * @param weight the weight
     * @param result the result
     * @return this WeightedRandom instance
     * @since 1.0.0-SNAPSHOT
     */
    public WeightedRandom<T> add(double weight, T result) {
        if (weight <= 0) {
            return this;
        }

        total += weight;
        map.put(total, result);
        return this;
    }

    /**
     * Polls a random value from the map
     *
     * @return the random value
     * @since 1.0.0-SNAPSHOT
     */
    public T poll() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    /**
     * Checks if the WeightedRandom is empty
     *
     * @return true if empty
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    /**
     * Gets the size of the WeightedRandom
     *
     * @return the size
     * @since 1.0.0-SNAPSHOT
     */
    public int size() {
        return this.map.size();
    }

    /**
     * Gets all entries in WeightedRandom
     *
     * @return the entry set
     * @since 1.0.0-SNAPSHOT
     */
    public Set<Entry<Double, T>> getEntries() {
        double prev = 0;
        Set<Entry<Double, T>> entries = new LinkedHashSet<>();

        for (Entry<Double, T> entry : this.map.entrySet()) {
            entries.add(Map.entry(entry.getKey() - prev, entry.getValue()));
            prev = entry.getKey();
        }

        return entries;
    }
}
