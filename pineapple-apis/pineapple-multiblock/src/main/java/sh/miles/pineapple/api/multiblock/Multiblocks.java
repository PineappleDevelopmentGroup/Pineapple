package sh.miles.pineapple.api.multiblock;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.api.multiblock.serialized.MultiblockPatternSerializer;
import sh.miles.pineapple.json.JsonHelper;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterRegistry;
import sh.miles.pineapple.util.serialization.bridges.gson.GsonSerializedBridge;

/**
 * The central entry point for the Stratus Multiblock API.
 * <p>
 * This interface provides access to the primary subsystems: pattern management,
 * active structure tracking, and world matching logic.
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class Multiblocks {

    @Nullable
    private static Multiblocks instance = null;

    private final MultiblockPatterns patterns;
    private final ActiveMultiblocks active;
    private final MultiblockPatternMatcher matcher;

    private Multiblocks(JsonHelper jsonHelper) {
        this.patterns = new MultiblockPatterns(jsonHelper.getGson());
        this.active = new ActiveMultiblocks();
        this.matcher = new MultiblockPatternMatcher(this.patterns);
    }

    /**
     * Gets the manager responsible for registering, storing, and retrieving
     * {@link MultiblockPattern} definitions.
     *
     * @return the pattern manager
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPatterns patterns() {
        return this.patterns;
    }

    /**
     * Gets the registry responsible for tracking {@link ActiveMultiblock}
     * instances currently existing in the world.
     *
     * @return the active multiblock registry
     * @since 1.0.0-SNAPSHOT
     */
    public ActiveMultiblocks active() {
        return this.active;
    }

    /**
     * Gets the matcher service used to detect patterns within the world.
     *
     * @return the pattern matcher
     * @since 1.0.0-SNAPSHOT
     */
    public MultiblockPatternMatcher matcher() {
        return this.matcher;
    }

    /**
     * Setup the singleton instance of the Multiblocks API.
     *
     * @return the API instance
     * @throws IllegalStateException if the Stratus API is not initialized
     * @since 1.0.0-SNAPSHOT
     */
    public static Multiblocks setup() {
        SerializedAdapterRegistry.INSTANCE.register(new MultiblockPatternSerializer());
        final JsonHelper helper = new JsonHelper((builder) -> {
            SerializedAdapterRegistry.INSTANCE.registerBridge(new GsonSerializedBridge(builder));
        });
        instance = new Multiblocks(helper);
        return instance;
    }

    public static Multiblocks multiblocks() {
        if (instance == null) {
            throw new IllegalStateException(
                "Mutiblocks#setup should be called before trying to retrieve this instance");
        }
        return instance;
    }

    public static void cleanup() {
        instance = null;
    }
}
