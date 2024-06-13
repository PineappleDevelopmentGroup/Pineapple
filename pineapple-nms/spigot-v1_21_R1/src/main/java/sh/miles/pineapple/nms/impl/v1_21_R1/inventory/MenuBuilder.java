package sh.miles.pineapple.nms.impl.v1_21_R1.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftHumanEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MenuBuilder {

    public static final MenuBuilder INSTANCE = new MenuBuilder();

    private final Map<MenuType<?>, ContainerProvider> builder;

    private MenuBuilder() {
        this.builder = new HashMap<>();
        this.builder.put(MenuType.FURNACE, ContainerProvider.tile(FurnaceBlockEntity::new, Blocks.FURNACE));
        this.builder.put(MenuType.SMOKER, ContainerProvider.tile(SmokerBlockEntity::new, Blocks.SMOKER));
        this.builder.put(MenuType.BLAST_FURNACE, ContainerProvider.tile(BlastFurnaceBlockEntity::new, Blocks.BLAST_FURNACE));
        this.builder.put(MenuType.ENCHANTMENT, (player, inventory) -> new SimpleMenuProvider(
                        (syncId, playerinventory, human) -> new EnchantmentMenu(syncId, playerinventory, ContainerLevelAccess.create(human.level(), human.blockPosition())),
                        Component.empty()
                ).createMenu(player.nextContainerCounter(), inventory, player)
        );
        this.builder.put(MenuType.CARTOGRAPHY_TABLE, ContainerProvider.worldAccess(CartographyTableMenu::new));
        this.builder.put(MenuType.ANVIL, ContainerProvider.worldAccess(AnvilMenu::new));
        this.builder.put(MenuType.GRINDSTONE, ContainerProvider.worldAccess(GrindstoneMenu::new));
    }

    public AbstractContainerMenu build(CraftHumanEntity human, MenuType<?> type) {
        final ContainerProvider provider = builder.getOrDefault(type, (player, inventory) -> type.create(player.nextContainerCounter(), inventory));
        return provider.supply((ServerPlayer) human.getHandle(), human.getHandle().getInventory());
    }

    interface ContainerProvider {
        AbstractContainerMenu supply(final ServerPlayer player, Inventory playerInventory);

        static ContainerProvider tile(BiFunction<BlockPos, BlockState, MenuConstructor> entity, Block block) {
            return (player, inventory) -> entity.apply(BlockPos.ZERO, block.defaultBlockState()).createMenu(player.nextContainerCounter(), inventory, player);
        }

        static ContainerProvider worldAccess(ContainerAccessMenu menu) {
            return (player, inventory) -> menu.build(player.nextContainerCounter(), inventory, ContainerLevelAccess.create(player.level(), player.blockPosition()));
        }
    }

    interface ContainerAccessMenu {
        AbstractContainerMenu build(int syncId, Inventory inventory, ContainerLevelAccess access);
    }
}
