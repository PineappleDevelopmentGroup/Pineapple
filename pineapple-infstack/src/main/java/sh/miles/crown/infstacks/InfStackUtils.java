package sh.miles.crown.infstacks;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.Pair;

import java.util.ArrayList;

/**
 * A Utility classes for InfStacks
 */
public final class InfStackUtils {

    /**
     * Gathers the size of the stack from the InfStack
     */
    public static final NamespacedKey STACK_SIZE_KEY = NamespacedKey.fromString("crown_infstacks:stack_size");

    private InfStackUtils() {
        throw new UnsupportedOperationException("Can not instantiate utility class");
    }

    /**
     * Checks if ItemStack is an InfStack
     *
     * @param itemStack the ItemStack
     * @return true if it is an InfStack otherwise false
     */
    public static boolean isInfStack(@NotNull final ItemStack itemStack) {
        if (itemStack.getType().isAir()) {
            return false;
        }
        final ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        return meta.getPersistentDataContainer().has(STACK_SIZE_KEY);
    }

    /**
     * Sets up the display item
     *
     * @param item       the item to setup as a display
     * @param comparator the comparator stack for this display
     * @param settings   the InfStackSettings
     * @return a pair of an ItemStack and its accompanying size
     */
    public static Pair<ItemStack, Long> setupDisplay(@NotNull final ItemStack item, @NotNull final ItemStack comparator, @NotNull final InfStackSettings settings) {
        if (item.getType().isAir()) {
            throw new IllegalStateException("can not create air as comparator");
        }
        final ItemStack display = item.clone();
        long amount = item.getAmount();
        display.setAmount(1);

        final ItemMeta meta = display.getItemMeta();
        if (meta == null) {
            throw new IllegalStateException("Meta must not be null");
        }
        final var container = meta.getPersistentDataContainer();
        boolean removeOldLore = false;
        if (!container.has(STACK_SIZE_KEY)) {
            container.set(STACK_SIZE_KEY, PersistentDataType.LONG, amount);
            display.setItemMeta(meta);
            removeOldLore = true;
        } else {
            amount = container.get(STACK_SIZE_KEY, PersistentDataType.LONG);
        }
        return Pair.of(settings.loreApplier().apply(settings.lore(), amount, display, comparator, removeOldLore), amount);
    }

    /**
     * Sets up the comparator stack for InfStacks
     *
     * @param item     the item to setup as the comparator
     * @param settings the InfStackSettings
     * @return the item as a Comparator
     */
    public static ItemStack setupComparator(@NotNull ItemStack item, @NotNull final InfStackSettings settings) {
        if (item.getType().isAir()) {
            throw new IllegalStateException("can not create air as comparator");
        }
        final ItemStack comparator = item.clone();
        comparator.setAmount(1);

        final ItemMeta meta = comparator.getItemMeta();
        if (meta == null) {
            throw new IllegalStateException("Meta must not be null");
        }
        final var container = meta.getPersistentDataContainer();
        if (container.has(STACK_SIZE_KEY)) {
            if (meta.hasLore()) {
                final var list = new ArrayList<>(meta.getLore());
                for (int i = 0; i < settings.lore().size(); i++) {
                    list.remove(list.size() - 1);
                }
                meta.setLore(list);
            }
            container.remove(STACK_SIZE_KEY);
            comparator.setItemMeta(meta);
        }
        return comparator;
    }
}
