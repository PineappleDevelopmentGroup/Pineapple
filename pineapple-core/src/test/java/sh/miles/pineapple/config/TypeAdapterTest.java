package sh.miles.pineapple.config;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.BukkitTest;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.collection.WeightedRandom;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TypeAdapterTest extends BukkitTest {

    private ConfigReloadable<ConfigMock> config;

    @BeforeEach
    @Override
    public void setup() {
        super.setup();
        PineappleLib.initialize(super.plugin, false);
        this.config = PineappleLib.getConfigurationManager()
                .createStaticReloadable(new File(plugin.getDataFolder(), "config.yml"), ConfigMock.class);
        this.config.saveDefaults().load();
    }

    @AfterEach
    @Override
    public void teardown() {
        super.teardown();
        PineappleLib.cleanup();
    }

    @Test
    public void test_Collection_List_TypeAdapter() {
        List<String> collection = ConfigMock.COLLECTION_LIST;
        assertEquals(3, collection.size());
        assertEquals("a", collection.get(0));
        assertEquals("b", collection.get(1));
        assertEquals("c", collection.get(2));
        assertTrue(collection instanceof ArrayList<String>);
    }

    @Test
    public void test_Collection_Set_TypeAdapter() {
        Set<String> collection = ConfigMock.COLLECTION_SET;
        assertEquals(3, collection.size());
        assertTrue(collection.contains("a"));
        assertTrue(collection.contains("b"));
        assertTrue(collection.contains("c"));
        assertTrue(collection instanceof LinkedHashSet<String>);
    }

    @Test
    public void test_Collection_Queue_TypeAdapter() {
        Queue<String> collection = ConfigMock.COLLECTION_QUEUE;
        assertEquals(3, collection.size());
        assertEquals("a", collection.poll());
        assertEquals("b", collection.poll());
        assertEquals("c", collection.poll());
        assertTrue(collection instanceof ArrayDeque<String>);
    }

    @Test
    public void test_Collection_Map_TypeAdapter() {
        Map<String, String> collection = ConfigMock.COLLECTION_MAP;
        assertEquals(3, collection.size());
        assertEquals("1", collection.get("a"));
        assertEquals("2", collection.get("b"));
        assertEquals("3", collection.get("c"));
        assertTrue(collection instanceof HashMap<String, String>);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_Color_TypeAdapter() {
        assertEquals(ChatColor.of("#ff5555"), ConfigMock.COLOR_RED);
        assertEquals(ChatColor.of("#55ff55"), ConfigMock.COLOR_GREEN);
        assertEquals(ChatColor.of("#5555ff"), ConfigMock.COLOR_BLUE);
    }

    @Test
    public void test_Enum_TypeAdapter() {
        assertEquals(EnumMock.VALUE1, ConfigMock.ENUM_1);
        assertEquals(EnumMock.VALUE2, ConfigMock.ENUM_2);
        assertEquals(EnumMock.VALUE3, ConfigMock.ENUM_3);
    }

    @Test
    public void test_Material_TypeAdapter() {
        assertEquals(Material.BARRIER, ConfigMock.MATERIAL);
    }

    @Test
    public void test_PineappleComponent_TypeAdapter() {
        PineappleComponent real = PineappleChat.component("<green></bold>Test");
        PineappleComponent config = ConfigMock.CHAT;
        assertEquals(real, config);
        assertEquals(real.getSource(), config.getSource());
    }

    @Test
    public void test_Primitive_Boolean_TypeAdapter() {
        assertTrue(ConfigMock.PRIMITIVE_BOOLEAN);
    }

    @Test
    public void test_Primitive_Int_TypeAdapter() {
        assertEquals(1, ConfigMock.PRIMITIVE_INT);
    }

    @Test
    public void test_Primitive_Long_TypeAdapter() {
        assertEquals(1L, ConfigMock.PRIMITIVE_LONG);
    }

    @Test
    public void test_WeightedRandom_TypeAdapter() {
        WeightedRandom<String> real = ConfigMock.getWeightedRandom();
        WeightedRandom<String> config = ConfigMock.WEIGHTED_RANDOM;
        assertEquals(3, config.size());
    }
}
