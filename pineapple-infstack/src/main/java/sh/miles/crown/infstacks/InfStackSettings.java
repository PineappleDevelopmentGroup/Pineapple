package sh.miles.crown.infstacks;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.chat.PineappleComponent;

/**
 * Settings for {@link InfStack}
 *
 * @param lore         the lore style
 * @param maxStackSize the max stack size
 * @param loreApplier  should apply lore using the pineapple component and returns a modified ItemStack
 */
public record InfStackSettings(@NotNull PineappleComponent lore, long maxStackSize, @NotNull LoreApplier loreApplier) {

    public interface LoreApplier {
        @NotNull
        ItemStack apply(@NotNull final PineappleComponent component, final long currentAmount, @NotNull final ItemStack itemStack);
    }

}
