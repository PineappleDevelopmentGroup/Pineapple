package sh.miles.pineapple.nms.impl.v1_20_R4.inventory.scene;

import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventoryView;
import sh.miles.pineapple.nms.api.menu.MenuType;
import sh.miles.pineapple.nms.api.menu.scene.MenuScene;
import sh.miles.pineapple.nms.impl.v1_20_R4.inventory.PineappleMenuType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Deprecated(forRemoval = true)
public final class PineappleSceneFactory {

    private static final Map<MenuType<?>, Function<CraftInventoryView, MenuScene>> factory = new HashMap<>();

    static {
        factory.put(MenuType.ANVIL, PineappleAnvilScene::new);
    }

    @SuppressWarnings("all")
    public static <T extends MenuScene> T make(MenuType<T> type, CraftInventoryView view) {
        return (T) factory.getOrDefault(((PineappleMenuType<T>) type), PineappleMenuScene::new).apply(view);
    }
}
