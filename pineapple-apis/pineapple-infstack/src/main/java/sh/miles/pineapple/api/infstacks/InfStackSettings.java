package sh.miles.pineapple.api.infstacks;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.chat.PineappleComponent;

import java.util.List;

/**
 * Settings for {@link InfStack}
 *
 * @param lore          the lore style
 * @param maxStackSize  the max stack size
 * @param defaultManual true if by default all InfStacks are updated manually instead of automatically via their respective methods
 * @param loreApplier   should apply lore using the pineapple component and returns a modified ItemStack
 */
@NullMarked
public record InfStackSettings(List<PineappleComponent> lore, long maxStackSize,
                               boolean defaultManual, LoreApplier loreApplier) {

    public interface LoreApplier {
        @NotNull
        ItemStack apply(final List<PineappleComponent> component, final long currentAmount, final ItemStack display, final ItemStack comparator, boolean removeOldLore);
    }

}
