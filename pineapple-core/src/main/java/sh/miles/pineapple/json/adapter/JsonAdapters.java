package sh.miles.pineapple.json.adapter;

import com.google.gson.GsonBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Medium class used to register all TypeAdapter's provided by pineapple to a GsonBuilder
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class JsonAdapters {

    private JsonAdapters() {
    }

    /**
     * Registers all TypeAdapter's to a GsonBuilder
     * <p>
     * Below is a list of all adapter's registered by default
     * <ul>
     * <li>{@link ItemStack}</li>
     * <li>{@link UUID}</li>
     * </ul>
     *
     * @param builder the GsonBuilder to register them too
     * @since 1.0.0-SNAPSHOT
     */
    public static void registerAll(GsonBuilder builder) {
        builder.registerTypeAdapter(UUID.class, new UUIDAdapter());
        builder.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeHierarchyAdapter(NamespacedKey.class, new NamespacedKeyAdapter());
        builder.registerTypeHierarchyAdapter(PotionEffectType.class, new PotionEffectTypeAdapter());
        builder.registerTypeHierarchyAdapter(DamageType.class, new DamageTypeAdapter());
        builder.registerTypeHierarchyAdapter(Particle.class, new ParticleAdapter());
        builder.registerTypeHierarchyAdapter(PotionEffectType.class, new PotionEffectTypeAdapter());
    }

}
