package sh.miles.pineapple.gui.manage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.menu.manage.MenuManager;

/**
 * Listener implementation medium for Bukkit
 * <p>
 * This class should not be registered or implemented by plugins. If you need to reflectively access this listener you
 * ARE doing something wrong.
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
class GuiListener implements Listener {

    private final GuiManager manager;

    public GuiListener(GuiManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(@NotNull final InventoryClickEvent event) {
        manager.getGui(event.getInventory()).ifPresent(menu -> menu.handleClick(event));
    }

    @EventHandler
    public void onOpen(@NotNull final InventoryOpenEvent event) {
        manager.getGui(event.getInventory()).ifPresent(menu -> menu.handleOpen(event));
    }

    @EventHandler
    public void onClose(@NotNull final InventoryCloseEvent event) {
        manager.getGui(event.getInventory()).ifPresent(menu -> menu.handleClose(event));
    }

}
