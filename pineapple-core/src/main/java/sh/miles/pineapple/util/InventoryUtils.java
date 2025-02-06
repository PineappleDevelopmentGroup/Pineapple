package sh.miles.pineapple.util;

import org.bukkit.event.inventory.InventoryAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class InventoryUtils {

    private static final List<InventoryAction> PICKUP_ACTIONS = List.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF);

    public static boolean isPickupAction(@NotNull final InventoryAction action) {
        return PICKUP_ACTIONS.contains(action);
    }
}
