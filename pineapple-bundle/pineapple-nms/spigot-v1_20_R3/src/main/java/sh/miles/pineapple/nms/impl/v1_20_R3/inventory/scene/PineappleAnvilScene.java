package sh.miles.pineapple.nms.impl.v1_20_R3.inventory.scene;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryView;
import sh.miles.pineapple.nms.api.menu.scene.AnvilScene;

@Deprecated(forRemoval = true)
public class PineappleAnvilScene extends PineappleMenuScene<AnvilMenu> implements AnvilScene {

    public PineappleAnvilScene(final CraftInventoryView view) {
        super(view);
    }

    @Override
    public String getText() {
        return container.itemName;
    }

    @Override
    public void setText(final String text) {
        container.itemName = text;
        final Slot slot = container.getSlot(0);
        if (slot.hasItem()) {
            slot.getItem().setHoverName(Component.literal(text));
        }
    }

    @Override
    public int getRepairCost() {
        return container.cost.get();
    }

    @Override
    public void setRepairCost(final int cost) {
        container.cost.set(cost);
    }

    @Override
    public int getRepairItemCost() {
        return container.repairItemCountCost;
    }

    @Override
    public void setRepairItemCost(final int amountCost) {
        container.repairItemCountCost = amountCost;
    }

}
