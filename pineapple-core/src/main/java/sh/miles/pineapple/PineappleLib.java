package sh.miles.pineapple;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.command.CommandRegistry;
import sh.miles.pineapple.config.adapter.ConfigurationManager;
import sh.miles.pineapple.gui.manage.GuiManager;
import sh.miles.pineapple.nms.annotations.NMS;
import sh.miles.pineapple.nms.api.PineappleNMS;
import sh.miles.pineapple.nms.loader.NMSLoader;

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
public final class PineappleLib {

    private static PineappleLib instance;

    private final Plugin plugin;
    private PineappleNMS nmsProvider;
    private final CommandRegistry commandRegistry;
    private final ConfigurationManager configurationManager;
    private final GuiManager guiManager;
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
            NMSLoader.INSTANCE.activate();
            this.nmsProvider = NMSLoader.INSTANCE.getPineapple();
        }
        this.commandRegistry = new CommandRegistry(plugin);
        this.configurationManager = new ConfigurationManager();
        this.guiManager = new GuiManager(plugin);

        loadVersion();
    }

    /**
     * @return the configuration manager
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static ConfigurationManager getConfigurationManager() {
        return instance.configurationManager;
    }

    /**
     * @return the gui manager
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static GuiManager getGuiManager() {
        return instance.guiManager;
    }

    /**
     * @return the command register
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public static CommandRegistry getCommandRegistry() {
        return instance.commandRegistry;
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
     * Retrieves the initialized plugin's logger
     *
     * @return the logger
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
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
    @NMS
    public static void initialize(@NotNull final Plugin plugin) {
        instance = new PineappleLib(plugin, true);
    }

    /**
     * Initializes PineappleLib
     *
     * @param plugin the plugin
     * @param useNms decides whether or not to use NMS
     * @since 1.0.0-SNAPSHOT
     */
    public static void initialize(@NotNull final Plugin plugin, final boolean useNms) {
        instance = new PineappleLib(plugin, useNms);
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
