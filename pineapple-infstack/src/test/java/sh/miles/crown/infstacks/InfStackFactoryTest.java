package sh.miles.crown.infstacks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.chat.PineappleChat;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InfStackFactoryTest extends AbstractPluginTest {

    private InfStackFactory factory;

    @BeforeEach
    public void setup() {
        super.setup();
        this.factory = new InfStackFactory(new InfStackSettings(PineappleChat.component("Amount %s"), Long.MAX_VALUE,
                (lore, amount, item) -> {
                    final ItemMeta meta = item.getItemMeta();
                    final var list = new ArrayList<String>();
                    list.add(lore.getSource().formatted(amount));
                    meta.setLore(list);
                    item.setItemMeta(meta);
                    return item;
                }));
    }

    @Test
    void test_CanCreateEmpty() {
        final InfStack stack = factory.air();
        assertTrue(stack.isEmpty());
        assertTrue(stack.isAir());
        assertFalse(stack.grow(123));
        assertFalse(stack.shrink(1));
        assertFalse(stack.grow(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
    }

    @Test
    void test_CanGrow_Appropriately() {
        final InfStack stack = factory.create(new ItemStack(Material.FEATHER, 10));
        assertEquals(10, stack.getStackSize());
        assertFalse(stack.isEmpty() || stack.isAir());
        assertTrue(stack.grow(100));
        assertEquals(110, stack.getStackSize());

        final ItemStack extracted = stack.extract(64);
        assertEquals(46, stack.getStackSize());
        assertEquals(new ItemStack(Material.FEATHER, 64), extracted);
    }

    @Test
    void test_CanShrink_Appropriately() {
        final InfStack stack = factory.create(new ItemStack(Material.FEATHER, 10));
        assertTrue(stack.shrink(9));
        assertEquals(1, stack.getStackSize());

        final ItemStack extracted = stack.extract(1);
        assertEquals(new ItemStack(Material.FEATHER), extracted);
        assertTrue(stack.isEmpty());
    }

    @Test
    void test_CanOverExtract() {
        final InfStack stack = factory.create(new ItemStack(Material.FEATHER, 10));
        assertEquals(10, stack.getStackSize());
        final ItemStack extracted = stack.extract(10);
        assertEquals(new ItemStack(Material.FEATHER, 10), extracted);
        assertEquals(0, stack.getStackSize());
        assertTrue(stack.isEmpty());
    }

    @Test
    void test_DetectsDirtiedStack() {
        final InfStack stack = factory.create(new ItemStack(Material.FEATHER, 10));
        final ItemStack source = stack.getDisplay();
        assertDoesNotThrow(() -> factory.create(source));
    }

    @Test
    void test_DetectsPureStack() {
        final InfStack stack = factory.create(new ItemStack(Material.FEATHER, 10));
        final ItemStack comparator = stack.getComparator();
        assertDoesNotThrow(() -> factory.create(comparator));
    }

    @AfterEach
    public void teardown() {
        super.teardown();
        this.factory = null;
    }
}
