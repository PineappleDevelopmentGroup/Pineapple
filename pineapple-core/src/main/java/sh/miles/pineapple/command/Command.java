package sh.miles.pineapple.command;

import com.google.common.base.Preconditions;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Command class that wraps the normal {@link BasicCommand} and provides extra functionality without exposing necessary
 * access to command features
 *
 * @since 1.0.0-SNAPSHOT
 */
public class Command implements BasicCommand {

    private final CommandLabel label;
    private final CommandSettings.Settings settings;
    private final Map<String, Command> subcommands;

    /**
     * Default NoArgs Executor
     */
    protected BiConsumer<CommandSender, String[]> noArgExecutor = (s, a) -> {};

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
    public void execute(CommandSourceStack sourceStack, String[] args) {
        CommandSender executor = sourceStack.getExecutor();
        if (!canUse(executor)) {
            settings.sendPermissionMessage(executor);
            return;
        }

        if (args.length == 0) {
            noArgExecutor.accept(executor, args);
            return;
        }

        final Command subCommand = subcommands.get(args[0]);
        if (subCommand == null) {
            return;
        }

        final String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        subCommand.execute(sourceStack, subArgs);
    }

    @Override
    public Collection<String> suggest(CommandSourceStack sourceStack, String[] args) {
        CommandSender executor = sourceStack.getExecutor();
        if (!canUse(executor)) {
            return List.of();
        }

        if (args.length == 0 || args.length == 1) {
            return this.subcommands.keySet().stream().filter((String s) -> executor.hasPermission(subcommands.get(s).label.getPermission())).toList();
        }

        final Command subcommand = subcommands.getOrDefault(args[0], null);
        if (subcommand == null) {
            return List.of();
        }

        if (!executor.hasPermission(subcommand.label.getPermission())) {
            return List.of();
        }

        final String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return subcommand.suggest(sourceStack, subArgs);
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

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission(label.getPermission());
    }
}
