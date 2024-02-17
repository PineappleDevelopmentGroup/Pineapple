package sh.miles.pineapple.nms.api.menu;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.RegistryKey;
import sh.miles.pineapple.nms.api.menu.scene.AnvilScene;
import sh.miles.pineapple.nms.api.menu.scene.MenuScene;
import sh.miles.pineapple.nms.api.registry.PineappleRegistry;

/**
 * Represents a Menu that can be viewed by a player
 *
 * @param <T> the type of scene and data associated with that view
 * @since 1.0.0-SNAPSHOT
 */
public interface MenuType<T extends MenuScene> extends RegistryKey<NamespacedKey> {

    MenuType<MenuScene> GENERIC_9x1 = get("generic_9x1");
    MenuType<MenuScene> GENERIC_9x2 = get("generic_9x2");
    MenuType<MenuScene> GENERIC_9x3 = get("generic_9x3");
    MenuType<MenuScene> GENERIC_9x4 = get("generic_9x4");
    MenuType<MenuScene> GENERIC_9x5 = get("generic_9x5");
    MenuType<MenuScene> GENERIC_9x6 = get("generic_9x6");
    MenuType<MenuScene> GENERIC_3x3 = get("generic_3x3");
    MenuType<AnvilScene> ANVIL = get("anvil");
    MenuType<MenuScene> BEACON = get("beacon");
    MenuType<MenuScene> BLAST_FURNACE = get("blast_furnace");
    MenuType<MenuScene> BREWING_STAND = get("brewing_stand");
    MenuType<MenuScene> CRAFTING = get("crafting");
    MenuType<MenuScene> ENCHANTMENT = get("enchantment");
    MenuType<MenuScene> FURNACE = get("furnace");
    MenuType<MenuScene> GRINDSTONE = get("grindstone");
    MenuType<MenuScene> HOPPER = get("hopper");
    MenuType<MenuScene> LECTERN = get("lectern");
    MenuType<MenuScene> LOOM = get("loom");
    MenuType<MenuScene> MERCHANT = get("merchant");
    MenuType<MenuScene> SHULKER_BOX = get("shulker_box");
    MenuType<MenuScene> SMITHING = get("smithing");
    MenuType<MenuScene> SMOKER = get("smoker");
    MenuType<MenuScene> CARTOGRAPHY_TABLE = get("cartography_table");
    MenuType<MenuScene> STONECUTTER = get("stonecutter");

    /**
     * Creates a MenuScene of this container for the player with the provided title
     *
     * @param player the player
     * @param title  the title
     * @return a container scene
     * @since 1.0.0-SNAPSHOT
     */
    T create(@NotNull final HumanEntity player, @NotNull final BaseComponent title);

    @SuppressWarnings("unchecked")
    private static <T extends MenuScene> MenuType<T> get(String id) {
        return (MenuType<T>) PineappleRegistry.MENU.getOrNull(NamespacedKey.minecraft(id));
    }
}
