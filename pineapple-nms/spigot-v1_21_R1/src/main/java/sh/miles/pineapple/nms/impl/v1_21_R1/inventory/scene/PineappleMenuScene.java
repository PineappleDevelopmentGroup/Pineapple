package sh.miles.pineapple.nms.impl.v1_21_R1.inventory.scene;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.nms.api.menu.scene.MenuScene;
import sh.miles.pineapple.nms.impl.v1_21_R1.internal.ComponentUtils;

public class PineappleMenuScene<T extends AbstractContainerMenu> implements MenuScene {

    private final CraftInventoryView view;
    protected final T container;

    @SuppressWarnings("unchecked")
    public PineappleMenuScene(CraftInventoryView view) {
        this.view = view;
        this.container = (T) view.getHandle();
    }

    @Override
    public BaseComponent getTitle() {
        return ComponentUtils.toBungeeChat(container.getTitle());
    }

    @Override
    public void setTitle(final BaseComponent title) {
        container.setTitle(ComponentUtils.toMinecraftChat(title));
    }

    @NotNull
    @Override
    public InventoryView getBukkitView() {
        return this.view;
    }

    @Override
    public void open() {
        this.view.getPlayer().openInventory(this.view);
    }
}
