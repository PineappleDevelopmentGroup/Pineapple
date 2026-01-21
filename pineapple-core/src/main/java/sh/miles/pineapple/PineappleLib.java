package sh.miles.pineapple;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sh.miles.pineapple.command.Command;
import sh.miles.pineapple.command.CommandLabel;
import sh.miles.pineapple.command.internal.PineappleCommandManager;
import sh.miles.pineapple.config.ConfigManager;
import sh.miles.pineapple.exception.AnomalyFactory;
import sh.miles.pineapple.gui.manage.GuiManager;
import sh.miles.pineapple.json.JsonHelper;
import sh.miles.pineapple.nms.annotations.NMS;
import sh.miles.pineapple.nms.api.PineappleNMS;
import sh.miles.pineapple.nms.loader.NMSLoader;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterRegistry;
import sh.miles.pineapple.util.serialization.bridges.gson.GsonSerializedBridge;
import sh.miles.pineapple.util.serialization.bridges.yaml.YamlSerializedBridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * The main library class for PineappleLib. That should be loaded using {@link PineappleLib#initialize(Plugin)}
 * <p>
 * Provides ease of access to many features of PineappleLib and puts them all into one place.
 *
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public final class PineappleLib {

    @Nullable
    private static PineappleLib instance;

    private final Plugin plugin;
    @Nullable
    private PineappleNMS nmsProvider;
    private final ConfigManager configurationManager;
    private final GuiManager guiManager;
    private final AnomalyFactory anomalyFactory;
    @Nullable
    private String version;

    /**
     * Creates a new instance of PineappleLib
     *
     * @param plugin the plugin
     * @param useNms whether or not to use NMS
     */
    private PineappleLib(final Plugin plugin, final boolean useNms) {
        this.plugin = plugin;
        if (useNms) {
            NMSLoader.INSTANCE.activate(plugin.getLogger());
            this.nmsProvider = NMSLoader.INSTANCE.getPineapple();
        }
        this.configurationManager = new ConfigManager();
        this.guiManager = new GuiManager(plugin);
        this.anomalyFactory = new AnomalyFactory(plugin.getLogger());
        loadVersion();

        // Serialized
        SerializedAdapterRegistry.INSTANCE.registerBridge(new YamlSerializedBridge(configurationManager));
    }

    /**
     * @return the configuration manager
     * @since 1.0.0-SNAPSHOT
     */
    public static ConfigManager getConfigurationManager() {
        return instance.configurationManager;
    }

    /**
     * @return the gui manager
     * @since 1.0.0-SNAPSHOT
     */
    public static GuiManager getGuiManager() {
        return instance.guiManager;
    }

    /**
     * Gets the PineappleNMS provider
     * <p>
     * Given That NMS was disabled this will throw an error
     *
     * @return PineappleNMS
     * @since 1.0.0-SNAPSHOT
     */
    @NMS
    public static PineappleNMS getNmsProvider() {
        NMSLoader.INSTANCE.verifyNMS();
        return instance.nmsProvider;
    }

    /**
     * Retrieves the pretty anomaly factory used for handling and prettifying exceptions
     *
     * @return the anomaly factory
     * @since 1.0.0-SNAPSHOT
     */
    public static AnomalyFactory getAnomalyFactory() {
        return instance.anomalyFactory;
    }

    /**
     * Retrieves the initialized plugin's logger
     *
     * @return the logger
     * @since 1.0.0-SNAPSHOT
     */
    public static Logger getLogger() {
        return instance.plugin.getLogger();
    }

    /**
     * @return the version
     * @since 1.0.0-SNAPSHOT
     */
    public static String getVersion() {
        return instance.version;
    }

    /**
     * Initializes PineappleLib
     *
     * @param plugin the plugin
     * @since 1.0.0-SNAPSHOT
     */
    public static void initialize(final Plugin plugin) {
        instance = new PineappleLib(plugin, false);
    }

    /**
     * Initializes PineappleLib
     *
     * @param plugin the plugin
     * @param useNms decides whether or not to use NMS
     * @since 1.0.0-SNAPSHOT
     */
    public static void initialize(final Plugin plugin, final boolean useNms) {
        instance = new PineappleLib(plugin, useNms);
    }

    /**
     * Registers a command to the server by using paper's {@link io.papermc.paper.command.brigadier.BasicCommand} class
     *
     * @param command the command to register
     * @since 1.0.0-SNAPSHOT
     */
    public static void registerCommand(final Command command) {
        final CommandLabel label = command.getCommandLabel();

        instance.plugin.getLifecycleManager()
            .registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar()
                .register(label.getName(), label.getDescription(), label.getAliases(), command)
            );
    }

    /**
     * Register Pineapple's internal commands with the plugin name as a suffix
     *
     * @since 1.0.0-SNAPSHOT
     */
    public void registerInternalCommands() {
        registerInternalCommands(plugin.getName());
    }

    /**
     * Register Pineapple's internal commands with a custom suffix
     *
     * @param commandSuffix the suffix
     * @since 1.0.0-SNAPSHOT
     */
    public void registerInternalCommands(String commandSuffix) {
        registerCommand(new PineappleCommandManager(this.plugin, commandSuffix.toLowerCase()));
    }

    /**
     * Cleans up and shutdown Pineapple and all of its functions
     */
    public static void cleanup() {
        instance = null;
    }

    private void loadVersion() {
        try (final BufferedReader reader = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream("/pineapple.version"), StandardCharsets.UTF_8)
        )) {
            this.version = reader.readLine();
        } catch (IOException ignored) {
        }
    }
}
