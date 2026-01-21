package sh.miles.pineapple.api.multiblock;

import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.world.WorldMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActiveMultiblockTest {

    private WorldMock world;

    @BeforeEach
    void setUp() {
        MockBukkit.mock();
        world = new WorldMock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Nested
    @DisplayName("Initialization & Physics")
    class InitializationTests {

        @Test
        @DisplayName("Constructor calculates absolute bounds and constituents correctly")
        void testCalculations() {
            // Given: A pattern with blocks at (0,0,0) and (1,0,0)
            BlockData stone = Material.STONE.createBlockData();
            MultiblockPattern pattern = MultiblockPattern.builder()
                .id("physics_test")
                .anchor(stone)
                .paletteAppend('s', stone)
                .structureAppend(0, 0, 0, 's')
                .structureAppend(1, 0, 0, 's')
                .build();

            // When: Active at (10, 64, 10)
            Location anchor = new Location(world, 10, 64, 10);
            ActiveMultiblock active = new ActiveMultiblock(pattern, anchor);

            // Then: Constituents should be shifted
            List<Vector> constituents = active.constituents();
            assertEquals(2, constituents.size());
            assertTrue(constituents.contains(new Vector(10, 64, 10)));
            assertTrue(constituents.contains(new Vector(11, 64, 10)));

            // Then: Bounds should be shifted
            // Pattern bounds: Min(0,0,0) Max(1,0,0)
            // Active bounds: Min(10,64,10) Max(11,64,10)
            assertEquals(10, active.bounds().minX());
            assertEquals(11, active.bounds().maxX());
        }

        @Test
        @DisplayName("Anchor getter returns defensive copy")
        void testAnchorImmutability() {
            BlockData stone = Material.STONE.createBlockData();
            MultiblockPattern pattern = createSimplePattern(stone);
            Location original = new Location(world, 100, 100, 100);

            ActiveMultiblock active = new ActiveMultiblock(pattern, original);

            // Act: Modify the returned location
            active.anchor().add(50, 50, 50);

            // Assert: Internal state remains unchanged
            assertEquals(original, active.anchor());
            assertNotSame(original, active.anchor(), "Should return a new object instance");
        }
    }

    @Nested
    @DisplayName("Data Management")
    class DataTests {

        private ActiveMultiblock active;
        private final Key key = Key.key("test", "data");

        @BeforeEach
        void setUpData() {
            BlockData stone = Material.STONE.createBlockData();
            active = new ActiveMultiblock(createSimplePattern(stone), new Location(world, 0, 0, 0));
        }

        @Test
        @DisplayName("Put/Get cycle works for valid types")
        void testPutGet() {
            active.putData(key, "Hello World");

            String result = active.getData(key, String.class);
            assertEquals("Hello World", result);
        }

        @Test
        @DisplayName("GetData throws exception on type mismatch")
        void testTypeMismatchGet() {
            active.putData(key, 12345); // Integer

            assertThrows(IllegalArgumentException.class, () -> {
                active.getData(key, String.class); // Requesting String
            });
        }

        @Test
        @DisplayName("RemoveData returns value and clears map")
        void testRemove() {
            active.putData(key, "To Remove");

            String removed = active.removeData(key, String.class);

            assertEquals("To Remove", removed);
            assertNull(active.getData(key, String.class));
        }

        @Test
        @DisplayName("RemoveData throws exception on type mismatch")
        void testTypeMismatchRemove() {
            active.putData(key, 12345);

            assertThrows(IllegalArgumentException.class, () -> {
                active.removeData(key, String.class);
            });
        }
    }

    @Nested
    @DisplayName("Dirty State Logic")
    class DirtyStateTests {
        // NOTE: These tests access the package-private 'dirty' field directly.
        // This confirms the internal state change required for persistence.

        private ActiveMultiblock active;
        private final Key key = Key.key("test", "dirty");

        @BeforeEach
        void setUpDirty() {
            BlockData stone = Material.STONE.createBlockData();
            active = new ActiveMultiblock(createSimplePattern(stone), new Location(world, 0, 0, 0));
            // Ensure clean start
            active.dirty = false;
        }

        @Test
        @DisplayName("putData marks dirty")
        void testPutDirty() {
            active.putData(key, "val");
            assertTrue(active.dirty, "putData should mark instance as dirty");
        }

        @Test
        @DisplayName("removeData marks dirty")
        void testRemoveDirty() {
            active.putData(key, "val");
            active.dirty = false; // Reset

            active.removeData(key, String.class);
            assertTrue(active.dirty, "removeData should mark instance as dirty");
        }

        @Test
        @DisplayName("markDirty explicitly sets flag")
        void testExplicitDirty() {
            active.markDirty();
            assertTrue(active.dirty);
        }
    }

    // Helper
    private MultiblockPattern createSimplePattern(BlockData data) {
        return MultiblockPattern.builder()
            .id("simple")
            .anchor(data)
            .paletteAppend('x', data)
            .structureAppend(0, 0, 0, 'x')
            .build();
    }
}