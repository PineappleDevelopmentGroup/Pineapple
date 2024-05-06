package sh.miles.pineapple.util.serialization.bridges;

import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;

import java.util.Set;

/**
 * Bridges serialized objects between other platforms
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface SerializedBridge {

    /**
     * Registers a SerializedAdapter into the native communication
     *
     * @param adapter the adapter to adapt
     * @since 1.0.0-SNAPSHOT
     */
    void register(SerializedAdapter<?> adapter);

    /**
     * Gets a set of default bridges
     *
     * @return the set of default bridges
     */
    static Set<SerializedBridge> getDefaultBridges() {
        return Set.of();
    }
}
