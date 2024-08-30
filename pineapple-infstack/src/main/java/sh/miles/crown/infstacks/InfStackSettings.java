package sh.miles.crown.infstacks;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.chat.PineappleComponent;

import java.util.List;

/**
 * Settings for {@link InfStack}
 *
 * @param lore         the lore style
 * @param maxStackSize the max stack size
 * @param loreApplier  should apply lore using the pineapple component and returns a modified ItemStack
 */
public record InfStackSettings(@NotNull List<PineappleComponent> lore, long maxStackSize,
                               @NotNull LoreApplier loreApplier) {

    public interface LoreApplier {
        @NotNull
        ItemStack apply(@NotNull final List<PineappleComponent> component, final long currentAmount, @NotNull final ItemStack display, @NotNull final ItemStack comparator, boolean removeOldLore);
    }

}
