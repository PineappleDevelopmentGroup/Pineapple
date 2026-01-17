package sh.miles.pineapple.util.serialization.bridges.gson;

import com.google.gson.GsonBuilder;
import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.bridges.SerializedBridge;

/**
 * Bridge between Gson and Serialized
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public class GsonSerializedBridge implements SerializedBridge {

    private final GsonBuilder builder;

    /**
     * Creates a new GsonSerializedBridge
     *
     * @param builder the builder to use with the bridge
     */
    public GsonSerializedBridge(final GsonBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void register(final SerializedAdapter<?> adapter) {
        builder.registerTypeAdapter(adapter.getKey(), new SerializedAdapterToJsonAdapter<>(adapter.getKey()));
    }

}
