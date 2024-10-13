package sh.miles.pineapple.nms.api.component;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * All pineapple compatibility for item components
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface ItemComponents {

    /**
     * Gets the display name of the item
     *
     * @param item the item to get the display name of
     * @return the component name
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    Component getDisplayName(@NotNull final ItemStack item);

    /**
     * Sets the display name of the given item
     *
     * @param item      the item to set the name of
     * @param component the name of the item to set
     * @return the modified item, which is NOT the same as the given item
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ItemStack setDisplayName(@NotNull final ItemStack item, @NotNull final Component component);

    /**
     * Gets the lore of the given item
     *
     * @param item the item to get the lore of
     * @return the lore
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    List<Component> getLore(@NotNull final ItemStack item);

    /**
     * Sets the lore of the given item
     *
     * @param item       the item to set the lore of
     * @param components the components to set as lore
     * @return the modified item, which is NOT the same as the given item
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    ItemStack setLore(@NotNull final ItemStack item, @NotNull final List<Component> components);
}
