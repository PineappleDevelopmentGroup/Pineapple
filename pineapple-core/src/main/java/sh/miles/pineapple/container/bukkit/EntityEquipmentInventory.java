package sh.miles.pineapple.container.bukkit;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.collection.NonNullArray;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * A mock implementation of Inventory that supports EntityEquipment
 */
public class EntityEquipmentInventory implements Inventory {

    public static final int SIZE = 6;
    public static final int MAIN_HAND = 0;
    public static final int OFF_HAND = 1;
    public static final int HEAD = 2;
    public static final int CHEST = 3;
    public static final int LEGS = 4;
    public static final int FEET = 5;

    private final NonNullArray<ItemStack> contents;
    private final EntityEquipment equipment;

    /**
     * Creates a new EntityEquipmentInventory
     *
     * @param equipment the equipment
     */
    public EntityEquipmentInventory(@NotNull final EntityEquipment equipment) {
        Preconditions.checkArgument(equipment != null, "The provided equipment must not be null");
        this.contents = new NonNullArray<>(6, () -> new ItemStack(Material.AIR));
        this.equipment = equipment;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setMaxStackSize(final int amount) {
        throw new UnsupportedOperationException("you can not set a max stack size in this inventory type");
    }

    @Nullable
    @Override
    public ItemStack getItem(final int i) {
        receiveUpdate(i);
        return contents.get(i);
    }

    @Override
    public void setItem(final int i, @Nullable final ItemStack itemStack) {
        contents.set(i, itemStack);
        pushUpdate(i);
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> addItem(@NotNull final ItemStack... itemStacks) throws IllegalArgumentException {
        throw new UnsupportedOperationException("this is not supported for this inventory type");
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> removeItem(@NotNull final ItemStack... itemStacks) throws IllegalArgumentException {
        throw new UnsupportedOperationException("this is not supported for this inventory type");
    }

    @NotNull
    @Override
    public ItemStack[] getContents() {
        for (int i = 0; i < 6; i++) {
            receiveUpdate(i);
        }
        return this.contents.toArray(ItemStack[]::new);
    }

    @Override
    public void setContents(@NotNull final ItemStack[] itemStacks) throws IllegalArgumentException {
        if (itemStacks.length > 6) {
            throw new IllegalArgumentException("The given array is larger than this inventory array size of %d versus inventory size of %d".formatted(itemStacks.length, SIZE));
        }

        for (int i = 0; i < itemStacks.length; i++) {
            contents.set(i, itemStacks[i]);
            pushUpdate(i);
        }
    }

    @NotNull
    @Override
    public ItemStack[] getStorageContents() {
        return getContents();
    }

    @Override
    public void setStorageContents(@NotNull final ItemStack[] itemStacks) throws IllegalArgumentException {
        setContents(itemStacks);
    }

    @Override
    public boolean contains(@NotNull final Material material) throws IllegalArgumentException {
        for (final ItemStack item : contents) {
            if (item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(@Nullable final ItemStack itemStack) {
        for (final ItemStack item : contents) {
            if (item.equals(itemStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(@NotNull final Material material, final int i) throws IllegalArgumentException {
        int total = 0;
        for (final ItemStack item : contents) {
            if (item.getType() == material) {
                total += item.getAmount();
            }
        }
        return total == i;
    }

    @Override
    public boolean contains(@Nullable final ItemStack itemStack, final int i) {
        int total = 0;
        for (final ItemStack item : contents) {
            if (item.isSimilar(itemStack)) {
                total += item.getAmount();
            }
        }
        return total == i;
    }

    @Override
    public boolean containsAtLeast(@Nullable final ItemStack itemStack, final int i) {
        int total = 0;
        for (final ItemStack item : contents) {
            if (item.isSimilar(itemStack)) {
                total += item.getAmount();
            }
        }
        return total >= i;
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@NotNull final Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException("this function can NOT be supported by this implementation");
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@Nullable final ItemStack itemStack) {
        throw new UnsupportedOperationException("this function can NOT be supported by this implementation");
    }

    @Override
    public int first(@NotNull final Material material) throws IllegalArgumentException {
        for (int i = 0; i < contents.size(); i++) {
            var item = contents.get(i);
            if (item.getType() == material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(@NotNull final ItemStack itemStack) {
        for (int i = 0; i < contents.size(); i++) {
            var item = contents.get(i);
            if (item.isSimilar(itemStack)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        for (int i = 0; i < this.contents.size(); i++) {
            var item = contents.get(i);
            if (item.getType().isAir()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        for (final ItemStack next : this.contents) {
            if (!next.getType().isAir()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void remove(@NotNull final Material material) throws IllegalArgumentException {
        var iterator = this.contents.iterator();
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (next.getType() == material) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void remove(@NotNull final ItemStack itemStack) {
        this.contents.remove(itemStack);
    }

    @Override
    public void clear(final int i) {
        this.contents.set(i, null);
    }

    @Override
    public void clear() {
        this.contents.clear();
    }

    @NotNull
    @Override
    public List<HumanEntity> getViewers() {
        throw new UnsupportedOperationException("this action is not supported in this inventory");
    }

    @NotNull
    @Override
    public InventoryType getType() {
        throw new UnsupportedOperationException("this action is not supported in this inventory");
    }

    @Nullable
    @Override
    public InventoryHolder getHolder() {
        if (equipment.getHolder() instanceof InventoryHolder holder) {
            return holder;
        }
        return null;
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator() {
        return this.contents.stream().toList().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator(final int i) {
        return this.contents.stream().toList().listIterator(i);
    }

    @Nullable
    @Override
    public Location getLocation() {
        return null;
    }

    private void pushUpdate(int slot) {
        switch (slot) {
            case MAIN_HAND -> equipment.setItem(EquipmentSlot.HAND, contents.get(MAIN_HAND));
            case OFF_HAND -> equipment.setItem(EquipmentSlot.HAND, contents.get(OFF_HAND));
            case HEAD -> equipment.setItem(EquipmentSlot.HEAD, contents.get(HEAD));
            case CHEST -> equipment.setItem(EquipmentSlot.CHEST, contents.get(CHEST));
            case LEGS -> equipment.setItem(EquipmentSlot.LEGS, contents.get(LEGS));
            case FEET -> equipment.setItem(EquipmentSlot.FEET, contents.get(FEET));
            default ->
                    throw new IllegalArgumentException("The given slot %d is not in within the range %d to %d".formatted(slot, 0, SIZE));
        }
    }

    private void receiveUpdate(int slot) {
        switch (slot) {
            case MAIN_HAND -> contents.set(MAIN_HAND, equipment.getItem(EquipmentSlot.HAND));
            case OFF_HAND -> contents.set(OFF_HAND, equipment.getItem(EquipmentSlot.OFF_HAND));
            case HEAD -> contents.set(HEAD, equipment.getItem(EquipmentSlot.HEAD));
            case CHEST -> contents.set(CHEST, equipment.getItem(EquipmentSlot.CHEST));
            case LEGS -> contents.set(LEGS, equipment.getItem(EquipmentSlot.LEGS));
            case FEET -> contents.set(FEET, equipment.getItem(EquipmentSlot.FEET));
            default ->
                    throw new IllegalArgumentException("The given slot %d is not in within the range %d to %d".formatted(slot, 0, SIZE));
        }
    }
}
