package sh.miles.pineapple.nms.impl.v1_20_R4;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.SharedConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.nms.api.PineappleNMS;
import sh.miles.pineapple.nms.api.PineappleUnsafe;
import sh.miles.pineapple.nms.impl.v1_20_R4.internal.ComponentUtils;
import sh.miles.pineapple.nms.impl.v1_20_R4.packet.PineapplePacketsImpl;

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
        nmsItem.set(DataComponents.CUSTOM_NAME, ComponentUtils.toMinecraftChat(displayName));
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @NotNull
    @Override
    public ItemStack setItemLore(@NotNull ItemStack item, @NotNull List<BaseComponent> lore) {
        final CraftItemStack craftItem = ensureCraftItemStack(item);
        final net.minecraft.world.item.ItemStack nmsItem = getItemStackHandle(craftItem);
        nmsItem.set(DataComponents.LORE, new ItemLore(lore.stream().map(ComponentUtils::toMinecraftChat).collect(Collectors.toList())));
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public @NotNull List<BaseComponent> getItemLore(@NotNull final ItemStack item) {
        final CraftItemStack craftItem = ensureCraftItemStack(item);
        final net.minecraft.world.item.ItemStack nmsItem = getItemStackHandle(craftItem);
        Preconditions.checkState(nmsItem != null, "Failed to acquire the handle for the Nms Item");
        ItemLore lore = nmsItem.get(DataComponents.LORE);
        if (lore != null && lore != ItemLore.EMPTY) {
            return new ArrayList<>();
        }

        return lore.lines().stream().map(ComponentUtils::toBungeeChat).collect(Collectors.toList());
    }

    private static final String EMPTY_TAG = "pineapple_empty_item_tag";

    @Override
    public byte[] itemsToBytes(@NotNull final Collection<ItemStack> itemStack) {
        final var tag = new CompoundTag();
        NbtUtils.addCurrentDataVersion(tag);

        int index = 0;
        for (final ItemStack stack : itemStack) {
            final var nmsCopy = CraftItemStack.asNMSCopy(stack);
            if (nmsCopy.isEmpty()) {
                final CompoundTag empty = new CompoundTag();
                empty.putBoolean(EMPTY_TAG, true);
                tag.put(index + "", empty);
                ++index;
                continue;
            }
            tag.put(index + "", nmsCopy.save(MinecraftServer.getServer().registryAccess()));
            ++index;
        }

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final DataOutputStream dataStream = new DataOutputStream(baos);
            NbtIo.writeCompressed(tag, dataStream);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<ItemStack> itemsFromBytes(@NotNull final byte[] bytes, final int size) {
        final List<ItemStack> items = new ArrayList<>(size);
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            final DataInputStream dataStream = new DataInputStream(bais);
            final var tempTag = NbtIo.readCompressed(dataStream, NbtAccounter.unlimitedHeap());
            final int dataVersion = NbtUtils.getDataVersion(tempTag, -1);

            final DataFixer fixer = MinecraftServer.getServer().getFixerUpper();
            final int currentVersion = SharedConstants.getCurrentVersion().getDataVersion().getVersion();
            for (final String allKey : tempTag.getAllKeys()) {
                Integer parsed = Ints.tryParse(allKey);
                if (parsed == null) {
                    continue;
                }
                final CompoundTag itemTag = tempTag.getCompound(allKey);
                if (itemTag.contains(EMPTY_TAG)) {
                    items.add(CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.EMPTY));
                    continue;
                }
                final Dynamic<Tag> result = fixer.update(References.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, itemTag), dataVersion, currentVersion);
                items.add(CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.parseOptional(MinecraftServer.getServer().registryAccess(), (CompoundTag) result.getValue())));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @NotNull
    @Override
    public byte[] itemToBytes(@NotNull final ItemStack itemStack) {
        throw new UnsupportedOperationException("The method PineappleNMS#itemToBytes is no longer supported in 1.20.5");
    }

    @NotNull
    @Override
    public ItemStack itemFromBytes(@NotNull final byte[] bytes) {
        throw new UnsupportedOperationException("The method PineappleNMS#itemFromBytes is no longer supported in 1.20.5");
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
