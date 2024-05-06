package sh.miles.pineapple.util.serialization.adapter;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.WriteableRegistry;
import sh.miles.pineapple.util.serialization.bridges.SerializedBridge;

import java.util.HashSet;
import java.util.Set;

/**
 * A Registry for Serialized Adapters that can be platform agnostic
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class SerializedAdapterRegistry extends WriteableRegistry<SerializedAdapter<?>, Class<?>> {

    public static final SerializedAdapterRegistry INSTANCE = new SerializedAdapterRegistry();

    private final Set<SerializedBridge> bridges;

    private SerializedAdapterRegistry() {
        this.bridges = new HashSet<>();
        bridges.addAll(SerializedBridge.getDefaultBridges());

        // Adapters
        BukkitRegistryAdapter.getRegistryAdapters().forEach(this::register);
        register(new ChatColorAdapter());
        register(new MaterialAdapter());
        register(new NamespacedKeyAdapter());
        register(new PineappleComponentAdapter());
        register(new UUIDAdapter());
    }

    @Override
    public boolean register(@NotNull final SerializedAdapter<?> object) {
        boolean status = super.register(object);
        if (!status) {
            return false;
        }

        for (final SerializedBridge bridge : bridges) {
            bridge.register(object);
        }
        return true;
    }

    /**
     * Registers a bridge
     *
     * @param bridge the bridge to register
     * @since 1.0.0-SNAPSHOT
     */
    public void registerBridge(@NotNull final SerializedBridge bridge) {
        this.bridges.add(bridge);
        syncBridge(bridge);
    }

    private void syncBridge(@NotNull final SerializedBridge bridge) {
        for (final SerializedAdapter<?> value : registry.values()) {
            bridge.register(value);
        }
    }
}
