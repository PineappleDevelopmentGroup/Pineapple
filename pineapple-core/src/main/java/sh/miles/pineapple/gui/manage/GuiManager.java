package sh.miles.pineapple.gui.manage;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.function.Option;
import sh.miles.pineapple.gui.PlayerGui;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages Menus and their functionality
 *
 * @since 1.0.0-SNAPSHOT
 */
public class GuiManager {

    private final Map<Inventory, PlayerGui<?>> guis;

    /**
     * Creates a GuiManager
     *
     * @param plugin the plugin to use
     * @since 1.0.0-SNAPSHOT
     */
    public GuiManager(@NotNull final Plugin plugin) {
        this.guis = new HashMap<>();
    }

    /**
     * @param gui the gui to register
     * @throws IllegalArgumentException the illegal argument exception
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public void register(@NotNull final PlayerGui<?> gui) throws IllegalArgumentException {
        this.guis.put(gui.topInventory(), gui);
    }

    /**
     * gets a player gui
     *
     * @param inventory the given inventory that the GUI belongs to
     * @return the player gui
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Option<PlayerGui<?>> getGui(@NotNull final Inventory inventory) {
        final var gui = this.guis.get(inventory);
        if (gui == null) {
            return Option.none();
        }

        return Option.some(gui);
    }

    /**
     * Unregisters the given inventory
     *
     * @param inventory the inventory
     * @since 1.0.0-SNAPSHOT
     */
    public void unregister(@NotNull final Inventory inventory) {
        this.guis.remove(inventory);
    }

}
