package sh.miles.pineapple.json;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.json.adapter.JsonAdapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * A utility to help with Json read and write operations
 *
 * @since 1.0.0-SNAPSHOT
 */
public class JsonHelper {

    private final Gson gson;

    /**
     * Creates the JsonHelper by using the GsonBuilder with the provided arguments
     *
     * @param build the builder function
     * @since 1.0.0-SNAPSHOT
     */
    public JsonHelper(@NotNull final Consumer<GsonBuilder> build) {
        final GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        JsonAdapters.registerAll(builder);
        build.accept(builder);
        this.gson = builder.create();
    }

    /**
     * Creates a JsonAdapter from a List of JsonAdapter's and registers them according to their settings
     *
     * @param adapters the adapters to register with this JsonHelper
     * @since 1.0.0-SNAPSHOT
     */
    public JsonHelper(@NotNull final List<JsonAdapter<?>> adapters) {
        final GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        JsonAdapters.registerAll(builder);
        for (final JsonAdapter<?> adapter : adapters) {
            if (adapter.isHierarchy()) {
                builder.registerTypeHierarchyAdapter(adapter.getAdapterType(), adapter);
            } else {
                builder.registerTypeAdapter(adapter.getAdapterType(), adapter);
            }
        }
        this.gson = builder.create();
    }

    /**
     * Creates a JsonAdapter from an Array of JsonAdapter's and registers them according to their settings
     *
     * @param adapters the adapters to register with this JsonHelper
     * @since 1.0.0-SNAPSHOT
     */
    public JsonHelper(@NotNull final JsonAdapter<?>... adapters) {
        this(Arrays.asList(adapters));
    }

    /**
     * Adapts a file into an array of objects using this JsonHelper
     * <p>
     * This adaptation process is mostly useful for Registry type JsonFiles that heavily lean on the requirements of
     * storing a list of objects.
     *
     * @param plugin     the plugin
     * @param file       the file
     * @param arrayClazz the array class
     * @param <T>        the type of the array return
     * @return the array of objects
     * @throws IllegalStateException thrown under the exception where no file is found
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public <T> T[] asArray(@NotNull final Plugin plugin, @NotNull String file, Class<T[]> arrayClazz) throws IllegalStateException {
        Preconditions.checkArgument(arrayClazz.isArray(), "An array class must be passed");
        try {
            return gson.fromJson(new FileReader(new File(plugin.getDataFolder(), file)), arrayClazz);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * Gets the gson object associated with this JsonHelper for more fine tuned parsing
     *
     * @return the gson
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Gson getGson() {
        return gson;
    }

}
