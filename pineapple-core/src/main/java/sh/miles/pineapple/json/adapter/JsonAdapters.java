package sh.miles.pineapple.json.adapter;

import com.google.gson.GsonBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Medium class used to register all TypeAdapter's provided by pineapple to a GsonBuilder
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class JsonAdapters {

    private JsonAdapters() {
    }

    /**
     * Registers all TypeAdapter's to a GsonBuilder
     * <p>
     * Below is a list of all adapter's registered by default
     * <ul>
     * <li>{@link ItemStack}</li>
     * <li>{@link UUID}</li>
     * </ul>
     *
     * @param builder the GsonBuilder to register them too
     * @since 1.0.0-SNAPSHOT
     */
    public static void registerAll(GsonBuilder builder) {
        builder.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter());
    }

}
