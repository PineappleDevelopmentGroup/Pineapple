package sh.miles.pineapple.util.serialization.bridges.yaml;

import org.jspecify.annotations.NullMarked;
import sh.miles.pineapple.config.ConfigManager;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.bridges.SerializedBridge;

@NullMarked
public class YamlSerializedBridge implements SerializedBridge {

    private final ConfigManager handler;

    public YamlSerializedBridge(final ConfigManager handler) {
        this.handler = handler;
    }

    @Override
    public void register(final SerializedAdapter<?> adapter) {
        handler.registerTypeAdapter(new SerializedAdapterToYamlAdapter<>(adapter.getKey()));
    }
}
