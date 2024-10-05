package sh.miles.pineapple.util.spec;

import com.google.common.collect.ImmutableList;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a firework specification
 *
 * @param effects the effects the firework has
 * @since 1.0.0-SNAPSHOT
 */
public record FireworkSpec(@NotNull List<FireworkEffect> effects, int power) {

    public FireworkSpec {
        effects = ImmutableList.copyOf(effects);
    }

    /**
     * Applies the firework specification to the given item
     *
     * @param item the item apply to
     * @since 1.0.0-SNAPSHOT
     */
    public void apply(@NotNull final ItemStack item) {
        item.setItemMeta(buildSpecMeta((FireworkMeta) item.getItemMeta()));
    }

    /**
     * Spawns a firework with this specification at the given location
     *
     * @param location the location to spawn the firework at
     * @return the firework spawned
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Firework spawn(@NotNull final Location location) {
        return spawn(location, (__) -> {
        });
    }

    /**
     * Spawns firework with this specification at the given location
     *
     * @param location      the location to spawn the firework at
     * @param configuration any further firework configuration
     * @return the firework spawned
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Firework spawn(@NotNull final Location location, Consumer<Firework> configuration) {
        final World world = location.getWorld();
        return world.spawn(location, Firework.class, (firework) -> {
            firework.setFireworkMeta(buildSpecMeta(firework.getFireworkMeta()));
            configuration.accept(firework);
        });
    }

    /**
     * Builds the spec from the given FireworkMeta
     *
     * @param meta the meta
     * @return the given meta
     * @since 1.0.0-SNAPSHOT
     */
    private FireworkMeta buildSpecMeta(FireworkMeta meta) {
        for (final FireworkEffect effect : this.effects) {
            meta.addEffect(effect);
        }
        meta.setPower(this.power);
        return meta;
    }
}
