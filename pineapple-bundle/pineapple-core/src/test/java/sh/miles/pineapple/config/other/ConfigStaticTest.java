package sh.miles.pineapple.config.other;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.BukkitTest;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.collection.WeightedRandom;
import sh.miles.pineapple.config.EnumMock;

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

public class ConfigStaticTest extends BukkitTest {

    @BeforeEach
    @Override
    public void setup() {
        super.setup();
        PineappleLib.initialize(super.plugin, false);
        PineappleLib.getConfigurationManager().createConfiguration(new File(plugin.getDataFolder(), "config.yml"), ConfigStaticMock.class).save(false).load();
    }

    @AfterEach
    @Override
    public void teardown() {
        super.teardown();
        PineappleLib.cleanup();
    }

    @Test
    public void test_Collection_List_TypeAdapter() {
        List<String> collection = ConfigStaticMock.COLLECTION_LIST;
        assertEquals(3, collection.size());
        assertEquals("a", collection.get(0));
        assertEquals("b", collection.get(1));
        assertEquals("c", collection.get(2));
        assertTrue(collection instanceof ArrayList<String>);
    }

    @Test
    public void test_Collection_Set_TypeAdapter() {
        Set<String> collection = ConfigStaticMock.COLLECTION_SET;
        assertEquals(3, collection.size());
        assertTrue(collection.contains("a"));
        assertTrue(collection.contains("b"));
        assertTrue(collection.contains("c"));
        assertTrue(collection instanceof LinkedHashSet<String>);
    }

    @Test
    public void test_Collection_Queue_TypeAdapter() {
        Queue<String> collection = ConfigStaticMock.COLLECTION_QUEUE;
        assertEquals(3, collection.size());
        assertEquals("a", collection.poll());
        assertEquals("b", collection.poll());
        assertEquals("c", collection.poll());
        assertTrue(collection instanceof ArrayDeque<String>);
    }

    @Test
    public void test_Collection_Map_TypeAdapter() {
        Map<String, String> collection = ConfigStaticMock.COLLECTION_MAP;
        assertEquals(3, collection.size());
        assertEquals("1", collection.get("a"));
        assertEquals("2", collection.get("b"));
        assertEquals("3", collection.get("c"));
        assertTrue(collection instanceof HashMap<String, String>);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_Color_TypeAdapter() {
        assertEquals(ChatColor.of("#ff5555"), ConfigStaticMock.COLOR_RED);
        assertEquals(ChatColor.of("#55ff55"), ConfigStaticMock.COLOR_GREEN);
        assertEquals(ChatColor.of("#5555ff"), ConfigStaticMock.COLOR_BLUE);
    }

    @Test
    public void test_Enum_TypeAdapter() {
        Assertions.assertEquals(EnumMock.VALUE1, ConfigStaticMock.ENUM_1);
        Assertions.assertEquals(EnumMock.VALUE2, ConfigStaticMock.ENUM_2);
        Assertions.assertEquals(EnumMock.VALUE3, ConfigStaticMock.ENUM_3);
    }

    @Test
    public void test_Material_TypeAdapter() {
        assertEquals(Material.BARRIER, ConfigStaticMock.MATERIAL);
    }

    @Test
    public void test_PineappleComponent_TypeAdapter() {
        PineappleComponent real = PineappleChat.component("<green></bold>Test");
        PineappleComponent config = ConfigStaticMock.CHAT;
        assertEquals(real, config);
        assertEquals(real.getSource(), config.getSource());
    }

    @Test
    public void test_Boolean_TypeAdapter() {
        assertTrue(ConfigStaticMock.PRIMITIVE_BOOLEAN);
        assertTrue(ConfigStaticMock.OBJECT_BOOLEAN);
    }

    @Test
    public void test_Int_TypeAdapter() {
        assertEquals(1, ConfigStaticMock.PRIMITIVE_INT);
        assertEquals(1, ConfigStaticMock.OBJECT_INTEGER);
    }

    @Test
    public void test_Long_TypeAdapter() {
        assertEquals(4294967299L, ConfigStaticMock.PRIMITIVE_LONG);
        assertEquals(4294967299L, ConfigStaticMock.OBJECT_LONG);
    }

    @Test
    public void test_WeightedRandom_TypeAdapter() {
        WeightedRandom<String> real = ConfigStaticMock.getWeightedRandom();
        WeightedRandom<String> config = ConfigStaticMock.WEIGHTED_RANDOM;
        assertEquals(3, config.size());
    }

    @Test
    public void test_Private_Fields() {
        assertEquals("full private string", ConfigStaticMock.getFullPrivateString());
        assertEquals("package private string", ConfigStaticMock.PACKAGE_PRIVATE_STRING);
    }
}
