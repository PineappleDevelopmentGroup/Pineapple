package sh.miles.crown.infstacks;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

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
        final ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        return meta.getPersistentDataContainer().has(STACK_SIZE_KEY);
    }

    public static ItemStack setupDisplay(@NotNull final ItemStack item, @NotNull final InfStackSettings settings) {
        final ItemStack display = item.clone();
        final long amount = item.getAmount();
        display.setAmount(1);

        final ItemMeta meta = display.getItemMeta();
        assert meta != null;
        final var container = meta.getPersistentDataContainer();
        if (!container.has(STACK_SIZE_KEY)) {
            meta.getPersistentDataContainer().set(STACK_SIZE_KEY, PersistentDataType.LONG, amount);
            display.setItemMeta(meta);
        }

        return settings.loreApplier().apply(settings.lore(), amount, display);
    }

    public static ItemStack setupComparator(@NotNull final ItemStack item) {
        final ItemStack comparator = item.clone();
        comparator.setAmount(1);

        final ItemMeta meta = comparator.getItemMeta();
        assert meta != null;
        final var container = meta.getPersistentDataContainer();
        if (container.has(STACK_SIZE_KEY)) {
            if (meta.hasLore()) {
                final var list = new ArrayList<>(meta.getLore());
                list.remove(list.size() - 1);
                meta.setLore(list);
            }
            container.remove(STACK_SIZE_KEY);
            comparator.setItemMeta(meta);
        }
        return comparator;
    }
}
