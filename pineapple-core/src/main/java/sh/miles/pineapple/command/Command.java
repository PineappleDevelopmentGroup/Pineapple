package sh.miles.pineapple.command;

import com.google.common.base.Preconditions;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Command class that wraps the normal {@link BukkitCommand} and provides extra functionality without exposing necessary
 * access to command features
 *
 * @since 1.0.0-SNAPSHOT
 */
public class Command implements CommandExecutor, CommandCompleter {

    private final CommandLabel label;
    private final CommandSettings.Settings settings;
    private final Map<String, Command> subcommands;

    /**
     * Default NoArgs Executor
     */
    protected BiFunction<CommandSender, String[], Boolean> noArgExecutor = (s, a) -> true;

    /**
     * Creates SCommand
     *
     * @param label    label
     * @param settings settings
     * @since 1.0.0-SNAPSHOT
     */
    public Command(@NotNull final CommandLabel label, @NotNull final CommandSettings.Settings settings) {
        Preconditions.checkNotNull(label);
        Preconditions.checkNotNull(settings);

        this.label = label;
        this.settings = settings;
        this.subcommands = new HashMap<>();
    }

    /**
     * Creates SCommand
     *
     * @param label label
     * @since 1.0.0-SNAPSHOT
     */
    public Command(@NotNull final CommandLabel label) {
        this(label, CommandSettings.DEFAULT_COMMAND_SETTINGS);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!sender.hasPermission(label.getPermission())) {
            settings.sendPermissionMessage(sender);
            return true;
        }

        if (args.length == 0) {
            return noArgExecutor.apply(sender, args);
        }

        final Command subCommand = subcommands.get(args[0]);
        if (subCommand == null) {
            return true;
        }

        final String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return subCommand.execute(sender, subArgs);
    }

    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!sender.hasPermission(label.getPermission())) {
            return List.of();
        }

        if (args.length == 1) {
            return this.subcommands.keySet().stream().filter((String s) -> sender.hasPermission(subcommands.get(s).label.getPermission())).toList();
        }

        final Command subcommand = subcommands.getOrDefault(args[0], null);
        if (subcommand == null) {
            return List.of();
        }

        if (!sender.hasPermission(subcommand.label.getPermission())) {
            return List.of();
        }

        final String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return subcommand.complete(sender, subArgs);
    }

    /**
     * Registers a command under this command. (sub-command)
     *
     * @param command the command to register
     * @since 1.0.0-SNAPSHOT
     */
    public void registerSubcommand(@NotNull Command command) {
        this.subcommands.put(command.getCommandLabel().getName(), command);
    }

    /**
     * The command label
     *
     * @return command label
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CommandLabel getCommandLabel() {
        return this.label;
    }

    /**
     * The command settings
     *
     * @return command settings
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CommandSettings.Settings getSettings() {
        return settings;
    }
}
