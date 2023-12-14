package sh.miles.pineapple.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.ReflectionUtils;

import java.lang.invoke.MethodHandle;
import java.util.Map;

/**
 * A Command Registry for registering all commands to. This clas plays an important middle man role in-between, the
 * Bukkit command registration system and Pineapple's command system.
 */
public final class CommandRegistry {

    private final CommandMap commandMap;
    private final Map<String, org.bukkit.command.Command> knownCommands;
    private final MethodHandle constructor;

    @SuppressWarnings("unchecked")
    public CommandRegistry() {
        this.commandMap = ReflectionUtils.getField(Bukkit.getPluginManager(), "commandMap", CommandMap.class);
        this.knownCommands = (Map<String, org.bukkit.command.Command>) ReflectionUtils.getField(commandMap, "knownCommands", Map.class);
        this.constructor = ReflectionUtils.getConstructor(PluginCommand.class, new Class[]{String.class, Plugin.class});
    }

    /**
     * Registers a command to the server by using spigot's internal {@link PluginCommand} class
     *
     * @param plugin  the plugin to register the command with
     * @param command the command to register
     */
    public void register(@NotNull final Plugin plugin, @NotNull final Command command) {
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

}
