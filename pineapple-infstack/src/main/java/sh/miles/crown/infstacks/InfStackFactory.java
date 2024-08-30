package sh.miles.crown.infstacks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.Pair;

public class InfStackFactory {

    private static final ItemStack AIR = new ItemStack(Material.AIR);

    private final InfStackSettings settings;

    /**
     * Creates a InfStackFactory
     *
     * @param settings the settings
     */
    public InfStackFactory(@NotNull final InfStackSettings settings) {
        this.settings = settings;
    }

    /**
     * Creates a new InfStack
     *
     * @param item the item
     * @return the InfStack
     */
    public InfStack create(@NotNull final ItemStack item) {
        if (item.getType().isAir()) {
            return air();
        }

        final ItemStack comparator = InfStackUtils.setupComparator(item, settings);
        final Pair<ItemStack, Long> display = InfStackUtils.setupDisplay(item, comparator, settings);

        return new InfStack(display.left(), comparator, display.right(), settings);
    }

    /**
     * Creates an air {@link InfStack}
     *
     * @return an air {@link InfStack}
     */
    public InfStack air() {
        return new InfStack(AIR, AIR, 0, settings);
    }
}
