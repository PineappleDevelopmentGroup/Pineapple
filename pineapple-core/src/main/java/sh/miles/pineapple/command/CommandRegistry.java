package sh.miles.pineapple.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.command.internal.PineappleCommandManager;

import java.lang.invoke.MethodHandle;

/**
 * A Command Registry for registering all commands to. This clas plays an important middle man role in-between, the
 * Bukkit command registration system and Pineapple's command system.
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class CommandRegistry {

    private final Plugin plugin;
    private final CommandMap commandMap;
    private final MethodHandle constructor;

    /**
     * Creates a new CommandRegistry
     *
     * @param plugin the plugin used
     */
    public CommandRegistry(@NotNull final Plugin plugin) {
        this.plugin = plugin;
        this.commandMap = ReflectionUtils.getField(Bukkit.getPluginManager(), "commandMap", CommandMap.class);
        this.constructor = ReflectionUtils.getConstructor(PluginCommand.class, new Class[]{String.class, Plugin.class});
    }

    /**
     * Registers a command to the server by using spigot's internal {@link PluginCommand} class
     *
     * @param command the command to register
     * @since 1.0.0-SNAPSHOT
     */
    public void register(@NotNull final Command command) {
        final CommandLabel label = command.getCommandLabel();
        final PluginCommand pluginCommand = (PluginCommand) ReflectionUtils.safeInvoke(this.constructor, label.getName(), plugin);
        if (pluginCommand == null) {
            throw new IllegalStateException("Creation of PluginCommand failed");
        }
        pluginCommand.setName(label.getName());
        pluginCommand.setAliases(label.getAliases());
        pluginCommand.setPermission(label.getPermission());
        pluginCommand.setUsage("/" + label.getName());
        pluginCommand.setExecutor((s, c, l, a) -> command.execute(s, a));
        pluginCommand.setTabCompleter((s, c, l, a) -> command.complete(s, a));

        if (!commandMap.register(plugin.getName(), pluginCommand)) {
            throw new IllegalStateException("Command with the name " + pluginCommand.getName() + " already exists");
        }
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
