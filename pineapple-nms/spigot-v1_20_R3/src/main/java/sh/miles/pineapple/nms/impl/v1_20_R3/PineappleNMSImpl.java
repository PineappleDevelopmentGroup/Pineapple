package sh.miles.pineapple.nms.impl.v1_20_R3;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.nms.api.PineappleNMS;
import sh.miles.pineapple.nms.api.PineappleUnsafe;
import sh.miles.pineapple.nms.api.menu.scene.MenuScene;
import sh.miles.pineapple.nms.api.menu.scene.custom.CustomMenuListener;
import sh.miles.pineapple.nms.impl.v1_20_R3.internal.ComponentUtils;
import sh.miles.pineapple.nms.impl.v1_20_R3.inventory.scene.PineappleMenuScene;
import sh.miles.pineapple.nms.impl.v1_20_R3.inventory.scene.custom.PineappleMenu;
import sh.miles.pineapple.nms.impl.v1_20_R3.packet.PineapplePacketsImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PineappleNMSImpl implements PineappleNMS {

    private static final MethodHandle itemStackHandle;
    private static final MethodHandle livingEntityLastDamageSourceHandle;
    private static final MenuType<?>[] CHEST_TYPES;

    static {
        itemStackHandle = ReflectionUtils.getFieldAsGetter(CraftItemStack.class, "handle");
        livingEntityLastDamageSourceHandle = ReflectionUtils.getFieldAsGetter(net.minecraft.world.entity.LivingEntity.class, "cd");
        CHEST_TYPES = new MenuType[]{MenuType.GENERIC_9x1, MenuType.GENERIC_9x2, MenuType.GENERIC_9x3, MenuType.GENERIC_9x4, MenuType.GENERIC_9x5, MenuType.GENERIC_9x6};
    }

    private final PineappleUnsafeImpl unsafe;
    private final PineapplePacketsImpl packets;

    public PineappleNMSImpl() {
        this.unsafe = new PineappleUnsafeImpl();
        this.packets = new PineapplePacketsImpl();
    }

    @NotNull
    @Override
    public MenuScene createMenuCustom(@NotNull final Player player, @NotNull final CustomMenuListener menuListener, final int rows, @NotNull final BaseComponent title) {
        Preconditions.checkArgument(player != null, "The given player must not be null");
        Preconditions.checkArgument(menuListener != null, "The given menuListener must not be null");
        Preconditions.checkArgument(title != null, "The given title must not be null");
        Preconditions.checkArgument(rows > 0 && rows < 7, "The given rows must be between 1 and 6 inclusive");

        final ServerPlayer splayer = ((CraftPlayer) player).getHandle();
        final MenuType<?> menuType = CHEST_TYPES[rows - 1];
        final PineappleMenu menu = new PineappleMenu(menuListener, menuType, splayer.nextContainerCounter(), splayer.getInventory(), rows);
        menu.setTitle(ComponentUtils.toMinecraftChat(title));
        return new PineappleMenuScene<>((CraftInventoryView) menu.getBukkitView());
    }

    @Nullable
    @Override
    public InventoryView openInventory(@NotNull final Player player, @NotNull final Inventory inventory, @NotNull final BaseComponent title) {
        ServerPlayer nms = ((CraftPlayer) player).getHandle();
        // legacy method to be replaced
        net.minecraft.world.inventory.MenuType<?> mojType = CraftContainer.getNotchInventoryType(inventory);

        if (mojType == null) {
            throw new IllegalArgumentException("could not find menu type for inventory type of " + inventory.getType());
        }
        AbstractContainerMenu menu = new CraftContainer(inventory, nms, nms.nextContainerCounter());
        menu = CraftEventFactory.callInventoryOpenEvent(nms, menu);
        if (menu == null) {
            throw new IllegalStateException("Unable to open menu for unknown reason");
        }

        nms.connection.send(new ClientboundOpenScreenPacket(menu.containerId, mojType, ComponentUtils.toMinecraftChat(title)));
        nms.containerMenu = menu;
        nms.initMenu(menu);
        return nms.containerMenu.getBukkitView();
    }

    @NotNull
    @Override
    public ItemStack setItemDisplayName(@NotNull final ItemStack item, final BaseComponent displayName) {
        final CraftItemStack craftItem = ensureCraftItemStack(item);
        final net.minecraft.world.item.ItemStack nmsItem = getItemStackHandle(craftItem);
        nmsItem.setHoverName(ComponentUtils.toMinecraftChat(displayName));
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @NotNull
    @Override
    public ItemStack setItemLore(@NotNull ItemStack item, @NotNull List<BaseComponent> lore) {
        final CraftItemStack craftItem = ensureCraftItemStack(item);
        final net.minecraft.world.item.ItemStack nmsItem = getItemStackHandle(craftItem);

        final CompoundTag tag = nmsItem.getTag() == null ? new CompoundTag() : nmsItem.getTag();
        if (!tag.contains(net.minecraft.world.item.ItemStack.TAG_DISPLAY)) {
            tag.put(net.minecraft.world.item.ItemStack.TAG_DISPLAY, new CompoundTag());
        }

        final CompoundTag displayTag = tag.getCompound(net.minecraft.world.item.ItemStack.TAG_DISPLAY);
        ListTag loreTag = new ListTag();
        for (int i = 0; i < lore.size(); i++) {
            loreTag.add(i, StringTag.valueOf(ComponentUtils.toJsonString(lore.get(i))));
        }

        displayTag.put(net.minecraft.world.item.ItemStack.TAG_LORE, loreTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public @NotNull List<BaseComponent> getItemLore(@NotNull final ItemStack item) {
        final CraftItemStack craftItem = ensureCraftItemStack(item);
        final net.minecraft.world.item.ItemStack nmsItem = getItemStackHandle(craftItem);

        final CompoundTag tag = nmsItem.getTag();
        if (tag == null) {
            return List.of();
        }

        final CompoundTag displayTag = tag.getCompound(net.minecraft.world.item.ItemStack.TAG_DISPLAY);
        if (displayTag == null) {
            return List.of();
        }

        final ListTag loreTag = displayTag.getList(net.minecraft.world.item.ItemStack.TAG_LORE, CraftMagicNumbers.NBT.TAG_LIST);
        if (loreTag == null) {
            return List.of();
        }

        final List<BaseComponent> lore = new ArrayList<>();
        for (int i = 0; i < loreTag.size(); i++) {
            lore.add(ComponentUtils.toBungeeChat(loreTag.getString(i)));
        }

        return lore;
    }

    @Override
    public byte[] itemsToBytes(@NotNull final Collection<ItemStack> itemStack) {
        NonNullList<net.minecraft.world.item.ItemStack> list = NonNullList.withSize(itemStack.size(), net.minecraft.world.item.ItemStack.EMPTY);
        list.addAll(itemStack.stream().map(CraftItemStack::asNMSCopy).toList());
        var tag = ContainerHelper.saveAllItems(new CompoundTag(), list);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(baos)) {
                NbtIo.write(tag, dos);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<ItemStack> itemsFromBytes(@NotNull final byte[] bytes, final int size) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            try (DataInputStream dis = new DataInputStream(bais)) {
                var items = NbtIo.read(dis);

                NonNullList<net.minecraft.world.item.ItemStack> list = NonNullList.withSize(size, net.minecraft.world.item.ItemStack.EMPTY);
                ContainerHelper.loadAllItems(items, list);
                return list.stream().map(CraftItemStack::asCraftMirror).collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public byte[] itemToBytes(@NotNull final ItemStack itemStack) {
        CraftItemStack craft = ensureCraftItemStack(itemStack);
        CompoundTag tag = getItemStackHandle(craft).getOrCreateTag();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(baos)) {
                NbtIo.write(tag, dos);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public ItemStack itemFromBytes(@NotNull final byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            try (DataInputStream dis = new DataInputStream(bais)) {
                return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of(NbtIo.read(dis)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PineapplePacketsImpl getPackets() {
        return this.packets;
    }

    @NotNull
    @Override
    public PineappleUnsafe getUnsafe() {
        return this.unsafe;
    }

    private CraftItemStack ensureCraftItemStack(ItemStack item) {
        return item instanceof CraftItemStack craftItem ? craftItem : CraftItemStack.asCraftCopy(item);
    }

    private static net.minecraft.world.item.ItemStack getItemStackHandle(CraftItemStack itemStack) {
        try {
            return (net.minecraft.world.item.ItemStack) itemStackHandle.invokeExact(itemStack);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }
}
