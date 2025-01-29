package sh.miles.pineapple.util.serialization.adapter;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a pack of serialized adapters that can be registered
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface SerializedAdapterPack {

    /**
     * Bootstraps the registrar by registering all valid serialized adapters
     *
     * @param registrar the registrar to handle the bootstrap
     * @since 1.0.0-SNAPSHOT
     */
    void bootstrap(@NotNull final Consumer<SerializedAdapter<?>> registrar);
}
