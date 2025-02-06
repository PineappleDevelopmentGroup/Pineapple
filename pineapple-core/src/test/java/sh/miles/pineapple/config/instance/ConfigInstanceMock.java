package sh.miles.pineapple.config.instance;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.collection.WeightedRandom;
import sh.miles.pineapple.config.EnumMock;
import sh.miles.pineapple.config.annotation.ConfigPath;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@SuppressWarnings("deprecation")
public class ConfigInstanceMock {

    @ConfigPath("test.collection.list")
    public List<String> COLLECTION_LIST = new ArrayList<>(List.of("a", "b", "c"));

    @ConfigPath("test.collection.set")
    public Set<String> COLLECTION_SET = new LinkedHashSet<>(List.of("a", "b", "c"));

    @ConfigPath("test.collection.queue")
    public Queue<String> COLLECTION_QUEUE = new ArrayDeque<>(List.of("a", "b", "c"));

    @ConfigPath("test.collection.map")
    public Map<String, String> COLLECTION_MAP = new HashMap<>(Map.of("a", "1", "b", "2", "c", "3"));

    @ConfigPath("test.color.red")
    public ChatColor COLOR_RED = ChatColor.RED;

    @ConfigPath("test.color.green")
    public ChatColor COLOR_GREEN = ChatColor.GREEN;

    @ConfigPath("test.color.blue")
    public ChatColor COLOR_BLUE = ChatColor.BLUE;

    @ConfigPath("test.enum.1")
    public EnumMock ENUM_1 = EnumMock.VALUE1;

    @ConfigPath("test.enum.2")
    public EnumMock ENUM_2 = EnumMock.VALUE2;

    @ConfigPath("test.enum.3")
    public EnumMock ENUM_3 = EnumMock.VALUE3;

    @ConfigPath("test.material")
    public Material MATERIAL = Material.BARRIER;

    @ConfigPath("test.component")
    public PineappleComponent CHAT = PineappleChat.component("<green></bold>Test");

    @ConfigPath("test.primitive.boolean")
    public boolean PRIMITIVE_BOOLEAN = true;

    @ConfigPath("test.primitive.int")
    public int PRIMITIVE_INT = 1;

    @ConfigPath("test.primitive.long")
    public long PRIMITIVE_LONG = 4294967299L;

    @ConfigPath("test.object.boolean")
    public Boolean OBJECT_BOOLEAN = Boolean.TRUE;

    @ConfigPath("test.object.int")
    public Integer OBJECT_INTEGER = 1;

    @ConfigPath("test.object.long")
    public Long OBJECT_LONG = Long.parseLong("4294967299");

    @ConfigPath("test.weightedrandom")
    public WeightedRandom<String> WEIGHTED_RANDOM = getWeightedRandom();

    @ConfigPath("test.private.package")
    String PACKAGE_PRIVATE_STRING = "package private string";

    @ConfigPath("test.private.full")
    private String FULL_PRIVATE_STRING = "full private string";

    public String getFullPrivateString() {
        return FULL_PRIVATE_STRING;
    }

    public WeightedRandom<String> getWeightedRandom() {
        WeightedRandom<String> weightedRandom = new WeightedRandom<>();
        weightedRandom.add(5, "a");
        weightedRandom.add(2.5, "b");
        weightedRandom.add(2.5, "c");
        return weightedRandom;
    }
}
