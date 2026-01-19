package sh.miles.pineapple.api.multiblock;

import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import sh.miles.pineapple.api.multiblock.event.MultiblockLoadEvent;
import sh.miles.pineapple.api.multiblock.event.MultiblockUnloadEvent;
import sh.miles.pineapple.util.PdcUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActiveMultiblocksTest {

    private ServerMock server;
    private WorldMock world;
    private ActiveMultiblocks registry;
    private MultiblockPatterns patternsManager;

    // Fixtures
    private MultiblockPattern simplePattern;
    private MultiblockPatternGroup groupMock;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        world = server.addSimpleWorld("world");
        registry = new ActiveMultiblocks();
        patternsManager = mock(MultiblockPatterns.class);

        // 1. Create a concrete pattern for testing
        // Structure: Anchor(0,0,0) and a block at (1,0,0)
        BlockData stone = Material.STONE.createBlockData();
        simplePattern = MultiblockPattern.builder()
            .id("test_pattern")
            .anchor(stone)
            .paletteAppend('s', stone)
            .structureAppend(0, 0, 0, 's')
            .structureAppend(1, 0, 0, 's')
            .build();

        // 2. Mock the Manager/Group lookup chain for Loading tests
        groupMock = mock(MultiblockPatternGroup.class);
        when(patternsManager.get("test_pattern")).thenReturn(groupMock);
        // Ensure that requesting any rotation returns our base pattern
        when(groupMock.rotation(anyInt())).thenReturn(simplePattern);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Nested
    @DisplayName("Registry & Spatial Logic")
    class RegistryTests {

        @Test
        @DisplayName("Register: Adds multiblock and updates lookup")
        void testRegister() {
            Location loc = new Location(world, 10, 64, 10);
            ActiveMultiblock mb = new ActiveMultiblock(simplePattern, loc);

            registry.register(mb);

            assertNotNull(registry.get(loc), "Should retrieve multiblock by anchor");
            assertTrue(mb.dirty, "Newly registered multiblocks should be dirty");
            assertFalse(mb.onDisk, "Newly registered multiblocks are not on disk yet");
        }

        @Test
        @DisplayName("Destroy: Removes from memory")
        void testDestroy() {
            Location loc = new Location(world, 10, 64, 10);
            ActiveMultiblock mb = new ActiveMultiblock(simplePattern, loc);
            registry.register(mb);

            registry.destroy(mb);

            assertNull(registry.get(loc), "Should be removed from registry");
        }

        @Test
        @DisplayName("Conflicts: Detects overlapping blocks")
        void testConflicts() {
            // MB1: Anchored at (10, 64, 10). Occupies (10,64,10) and (11,64,10).
            ActiveMultiblock mb1 = new ActiveMultiblock(simplePattern, new Location(world, 10, 64, 10));
            registry.register(mb1);

            // MB2: Anchored at (11, 64, 10). Occupies (11,64,10) and (12,64,10).
            // Conflict: They share (11, 64, 10).
            ActiveMultiblock mb2 = new ActiveMultiblock(simplePattern, new Location(world, 11, 64, 10));

            assertTrue(registry.conflicts(mb2), "Should detect conflict at shared coordinate");

            // MB3: Anchored at (20, 64, 20). No overlap.
            ActiveMultiblock mb3 = new ActiveMultiblock(simplePattern, new Location(world, 20, 64, 20));
            assertFalse(registry.conflicts(mb3), "Should not conflict with distant structure");
        }
    }

    @Nested
    @DisplayName("Persistence (Save/Load)")
    class PersistenceTests {

        @Test
        @DisplayName("Save: Persists data to Chunk PDC")
        void testSave() {
            Location loc = new Location(world, 10, 64, 10);
            Chunk chunk = loc.getChunk();
            ActiveMultiblock mb = new ActiveMultiblock(simplePattern, loc);

            registry.register(mb);
            registry.save(patternsManager, chunk);

            // Verify PDC state
            PersistentDataContainer pdc = chunk.getPersistentDataContainer();
            NamespacedKey anchorsKey = new NamespacedKey("pineapple", "multiblock_anchors");
            assertTrue(pdc.has(anchorsKey, PersistentDataType.LIST.longs()), "Chunk should have anchor list");

            NamespacedKey specificKey = PdcUtils.locationKey("pineapple", 10, 64, 10);
            assertTrue(pdc.has(specificKey, PersistentDataType.TAG_CONTAINER), "Chunk should have specific multiblock data");

            assertTrue(mb.onDisk, "Multiblock should be marked as onDisk");
            assertFalse(mb.dirty, "Multiblock should be marked as clean (not dirty)");
        }

        @Test
        @DisplayName("Load: Reconstructs ActiveMultiblock from Disk")
        void testLoad() {
            // 1. Setup Data on Disk
            Location loc = new Location(world, 50, 70, 50);
            ActiveMultiblock original = new ActiveMultiblock(simplePattern, loc);
            registry.register(original);
            registry.save(patternsManager, loc.getChunk());

            // 2. Create NEW Registry (Simulate Restart)
            ActiveMultiblocks newRegistry = new ActiveMultiblocks();

            // 3. Load
            newRegistry.load(patternsManager, loc.getChunk());

            // 4. Verify
            ActiveMultiblock loaded = newRegistry.get(loc);
            assertNotNull(loaded, "Should load multiblock from PDC");
            assertEquals(original.pattern().id(), loaded.pattern().id());
            assertEquals(loc, loaded.anchor());
        }

        @Test
        @DisplayName("Destroy: Cleans up Disk Data")
        void testDestroyCleanup() {
            Location loc = new Location(world, 10, 64, 10);
            ActiveMultiblock mb = new ActiveMultiblock(simplePattern, loc);

            // Save first
            registry.register(mb);
            registry.save(patternsManager, loc.getChunk());

            // Then destroy
            registry.destroy(mb);

            // Verify PDC is empty for this block
            NamespacedKey key = PdcUtils.locationKey("pineapple", 10, 64, 10);
            assertFalse(loc.getChunk().getPersistentDataContainer().has(key, PersistentDataType.TAG_CONTAINER));
        }

        @Test
        @DisplayName("Isolation: Save ignores other chunks")
        void testChunkIsolation() {
            Location loc1 = new Location(world, 0, 64, 0);       // Chunk 0,0
            Location loc2 = new Location(world, 100, 64, 100);   // Chunk 6,6

            registry.register(new ActiveMultiblock(simplePattern, loc1));
            registry.register(new ActiveMultiblock(simplePattern, loc2));

            // Save ONLY Chunk 0,0
            registry.save(patternsManager, loc1.getChunk());

            // Chunk 0,0 has data
            assertTrue(loc1.getChunk().getPersistentDataContainer().has(
                new NamespacedKey("pineapple", "multiblock_anchors"), PersistentDataType.LIST.longs()));

            // Chunk 6,6 is empty
            assertFalse(loc2.getChunk().getPersistentDataContainer().has(
                new NamespacedKey("pineapple", "multiblock_anchors"), PersistentDataType.LIST.longs()));
        }
    }

    @Nested
    @DisplayName("Event Integration")
    class EventTests {

        @Test
        @DisplayName("Save fires UnloadEvent for data storage")
        void testUnloadEvent() {
            Location loc = new Location(world, 10, 64, 10);
            ActiveMultiblock mb = new ActiveMultiblock(simplePattern, loc);
            registry.register(mb);

            Key customKey = Key.key("plugin", "data");
            AtomicBoolean eventFired = new AtomicBoolean(false);

            // Listener
            server.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
                @org.bukkit.event.EventHandler
                public void onUnload(MultiblockUnloadEvent event) {
                    eventFired.set(true);
                    event.edit(customKey, c -> c.set(new NamespacedKey("plugin", "val"), PersistentDataType.INTEGER, 99));
                }
            }, MockBukkit.createMockPlugin());

            // Act
            registry.save(patternsManager, loc.getChunk());

            // Assert
            assertTrue(eventFired.get());

            // Verify data inside chunk
            PersistentDataContainer chunkPdc = loc.getChunk().getPersistentDataContainer();
            PersistentDataContainer mbPdc = chunkPdc.get(PdcUtils.locationKey("pineapple", 10, 64, 10), PersistentDataType.TAG_CONTAINER);
            PersistentDataContainer storage = mbPdc.get(new NamespacedKey("pineapple", "multiblock_data"), PersistentDataType.TAG_CONTAINER);

            assertTrue(storage.has(new NamespacedKey("pineapple", "data"), PersistentDataType.TAG_CONTAINER));
        }

        @Test
        @DisplayName("Load fires LoadEvent for data retrieval")
        void testLoadEvent() {
            Location loc = new Location(world, 10, 64, 10);
            registry.register(new ActiveMultiblock(simplePattern, loc));

            // 1. Save data via event
            Key customKey = Key.key("plugin", "data");
            server.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
                @org.bukkit.event.EventHandler
                public void onUnload(MultiblockUnloadEvent event) {
                    event.edit(customKey, c -> c.set(new NamespacedKey("plugin", "val"), PersistentDataType.INTEGER, 123));
                }
            }, MockBukkit.createMockPlugin());
            registry.save(patternsManager, loc.getChunk());

            // 2. New Registry for loading
            ActiveMultiblocks newRegistry = new ActiveMultiblocks();
            AtomicBoolean eventFired = new AtomicBoolean(false);

            // 3. Load Event Listener
            server.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
                @org.bukkit.event.EventHandler
                public void onLoad(MultiblockLoadEvent event) {
                    eventFired.set(true);
                    // Assume event.get(Key) exists as established in previous context
                    PersistentDataContainer data = event.get(customKey);
                    assertNotNull(data);
                    assertEquals(123, data.get(new NamespacedKey("plugin", "val"), PersistentDataType.INTEGER));
                }
            }, MockBukkit.createMockPlugin());

            // 4. Act
            newRegistry.load(patternsManager, loc.getChunk());

            assertTrue(eventFired.get());
        }
    }
}