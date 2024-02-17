package sh.miles.pineapple.command.internal;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.command.Command;
import sh.miles.pineapple.command.CommandLabel;
import sh.miles.pineapple.command.internal.debug.DebugCommandManager;

/**
 * CommandManager for pineappls internal commands, shouldn't be interacted with by plugins
 */
@ApiStatus.Internal
public class PineappleCommandManager extends Command {

    /**
     * Construct a new PineappleCommandManager
     *
     * @param plugin the plugin
     * @param suffix the suffix
     */
    public PineappleCommandManager(Plugin plugin, String suffix) {
        super(new CommandLabel("pineapple-" + suffix, "pineapple-lib.command"));

        registerSubcommand(new DebugCommandManager(plugin));
    }
}
