package sh.miles.pineapple.util.serialization.adapter.bukkit;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterPack;

import java.util.function.Consumer;

/**
 * Represents a pack of all adapters for Bukkit
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class BukkitAdapterPack implements SerializedAdapterPack {

    public static final BukkitAdapterPack INSTANCE = new BukkitAdapterPack();

    private BukkitAdapterPack() {
    }

    @Override
    public void bootstrap(@NotNull final Consumer<SerializedAdapter<?>> registrar) {
        registrar.accept(new ArmorTrimAdapter());
        registrar.accept(new AttributeModifierAdapter());
        registrar.accept(new BoundingBoxAdapter());
        registrar.accept(new BukkitColorAdapter());
        BukkitRegistryAdapter.getRegistryAdapters().forEach(registrar);
        registrar.accept(new ChatColorAdapter());
        registrar.accept(new FireworkEffectAdapter());
        registrar.accept(new LocationAdapter());
        registrar.accept(new MaterialAdapter());
        registrar.accept(new NamespacedKeyAdapter());
        registrar.accept(new PineappleComponentAdapter());
        registrar.accept(new PotionEffectsAdapter());

    }
}
