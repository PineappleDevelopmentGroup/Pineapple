package sh.miles.pineapple.command;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.command.internal.PineappleCommandManager;

/**
 * A Command Registry for registering all commands to. This clas plays an important middle man role in-between, the
 * Bukkit command registration system and Pineapple's command system.
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class CommandRegistry {

    private final Plugin plugin;

    /**
     * Creates a new CommandRegistry
     *
     * @param plugin the plugin used
     */
    public CommandRegistry(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a command to the server by using spigot's internal {@link PluginCommand} class
     *
     * @param command the command to register
     * @since 1.0.0-SNAPSHOT
     */
    public void register(@NotNull final Command command) {
        final CommandLabel label = command.getCommandLabel();

        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar()
            .register(label.getName(), label.getDescription(), label.getAliases(), command)
        );
    }

    /**
     * Register Pineapple's internal commands with the plugin name as a suffix
     */
    public void registerInternalCommands() {
        registerInternalCommands(plugin.getName());
    }

    /**
     * Register Pineapple's internal commands with a custom suffix
     *
     * @param commandSuffix the suffix
     */
    public void registerInternalCommands(String commandSuffix) {
        register(new PineappleCommandManager(this.plugin, commandSuffix.toLowerCase()));
    }
}
