package sh.miles.pineapple.api.multiblock.serialized;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import sh.miles.pineapple.api.multiblock.MultiblockPattern;
import sh.miles.pineapple.util.serialization.Serialized;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sh.miles.pineapple.util.serialization.SerializedElement.array;
import static sh.miles.pineapple.util.serialization.SerializedElement.object;

class MultiblockPatternSerializerTest {

    private MultiblockPatternSerializer serializer;

    @BeforeEach
    void setUp() {
        MockBukkit.mock();
        serializer = new MultiblockPatternSerializer();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Round Trip: Serialize -> Deserialize = Equivalent Object")
    void testRoundTrip() throws SerializedAdaptationException {
        // 1. Setup a complex pattern
        BlockData stone = Material.STONE.createBlockData();
        BlockData glass = Material.GLASS.createBlockData();

        MultiblockPattern original = MultiblockPattern.builder()
            .id("round_trip_test")
            .anchor(stone)
            .origin(5, 5, 5, 0) // Logical pivot
            .paletteAppend('s', stone)
            .paletteAppend('g', glass)
            .structureAppend(0, 0, 0, 's') // Anchor position
            .structureAppend(1, 2, 3, 'g') // Offset block
            .build();

        // 2. Serialize using the real adapter
        SerializedElement element = serializer.serialize(original, Serialized.INSTANCE);

        // 3. Inspect the SerializedObject (Sanity Check)
        SerializedObject root = element.getAsObject();
        assertEquals("round_trip_test", root.getPrimitive("id").orThrow("missing id").getAsString());
        assertTrue(root.has("pivot"), "Pivot should be serialized when non-zero");

        // 4. Deserialize back
        MultiblockPattern reconstructed = serializer.deserialize(element, Serialized.INSTANCE);

        // 5. Verify Integrity
        assertEquals(original.id(), reconstructed.id());
        assertEquals(original.anchor(), reconstructed.anchor());

        // Check Pivot
        assertEquals(5, reconstructed.originX());
        assertEquals(5, reconstructed.originY());
        assertEquals(5, reconstructed.originZ());

        // Check Structure
        assertEquals(stone, reconstructed.blockDataAt(0, 0, 0));
        assertEquals(glass, reconstructed.blockDataAt(1, 2, 3));
    }

    @Test
    @DisplayName("Deserialize: Manually constructed SerializedObject works")
    void testManualDeserialization() throws SerializedAdaptationException {
        // This tests that we can read data created by hand (simulating reading from disk/JSON)

        // Structure:
        // id: "manual_test"
        // anchor: "minecraft:dirt"
        // origin: {x:0, y:0, z:0}  <-- This is the offset
        // palette: { "X": "minecraft:stone" }
        // layers: [ ["X"] ]

        SerializedObject root = object();
        root.add("id", "manual_test");
        root.add("anchor", "minecraft:dirt");

        SerializedObject origin = object();
        origin.add("x", 0);
        origin.add("y", 0);
        origin.add("z", 0);
        root.add("origin", origin);

        SerializedObject palette = object();
        palette.add("X", "minecraft:stone");
        root.add("palette", palette);

        SerializedArray layers = array();
        SerializedArray layer0 = array();
        layer0.add("X");
        layers.add(layer0);
        root.add("layers", layers);

        // Execute Deserialization
        MultiblockPattern result = serializer.deserialize(root, Serialized.INSTANCE);

        // Assert
        assertNotNull(result);
        assertEquals("manual_test", result.id());
        assertEquals(Material.DIRT, result.anchor().getMaterial());
        assertEquals(Material.STONE, result.blockDataAt(0, 0, 0).getMaterial());
    }

    @Test
    @DisplayName("Serialize: Pivot is omitted if zero (Optimization Check)")
    void testPivotOptimization() throws SerializedAdaptationException {
        BlockData stone = Material.STONE.createBlockData();
        MultiblockPattern pattern = MultiblockPattern.builder()
            .id("no_pivot")
            .anchor(stone)
            .origin(0, 0, 0, 0) // Zero pivot
            .paletteAppend('x', stone)
            .structureAppend(0, 0, 0, 'x')
            .build();

        SerializedElement element = serializer.serialize(pattern, Serialized.INSTANCE);
        SerializedObject root = element.getAsObject();

        assertTrue(root.has("id"));
        // Use assertion that relies on your library's has() or get() returning null/empty
        // Assuming SerializedObject.has(String) exists based on your usage in deserialize
        assertFalse(root.has("pivot"), "Pivot object should be omitted from output when 0,0,0");
    }
}
