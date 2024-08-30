package sh.miles.pineapple.command.internal.debug;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.command.Command;
import sh.miles.pineapple.command.CommandLabel;

/**
 * This class shouldn't need to interacted with by end plugins ever, contains debug command initialization
 */
@ApiStatus.Internal
public class DebugCommandManager extends Command {

    /**
     * Construct a new DebugCommandManager
     *
     * @param plugin the plugin
     */
    public DebugCommandManager(Plugin plugin) {
        super(new CommandLabel("debug", "pineapple-lib.command.debug"));

        registerSubcommand(new DebugVersionCommand(plugin));
    }
}
