package sh.miles.pineapple.command;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedCommand {

    private final CommandLabel label;
    private final CommandSettings.Settings settings;
    private final List<AdvancedCommand> subcommands;
    private final Map<String, ArgumentType<?>> arguments;

    /**
     * Creates Command
     *
     * @param label    label
     * @param settings settings
     * @since 1.0.0-SNAPSHOT
     */
    public AdvancedCommand(@NotNull final CommandLabel label, @NotNull final CommandSettings.Settings settings) {
        Preconditions.checkNotNull(label);
        Preconditions.checkNotNull(settings);

        this.label = label;
        this.settings = settings;
        this.subcommands = new ArrayList<>();
        this.arguments = new HashMap<>();
    }

    /**
     * Creates Command
     *
     * @param label label
     * @since 1.0.0-SNAPSHOT
     */
    public AdvancedCommand(@NotNull final CommandLabel label) {
        this(label, CommandSettings.DEFAULT_COMMAND_SETTINGS);
    }

    public void execute(@NotNull CommandContext<@NotNull CommandSourceStack> context) {
    }

    /**
     * Register an argument for a command
     * Use {@link io.papermc.paper.command.brigadier.argument.ArgumentTypes} for built-in or implement {@link ArgumentType}
     *
     * @param name argument name
     * @param argumentType argument type
     * @since 1.0.0-SNAPSHOT
     */
    public void registerArgument(@NotNull String name, @NotNull ArgumentType<?> argumentType) {
        this.arguments.put(name, argumentType);
    }

    /**
     * Registers a command under this command. (sub-command)
     *
     * @param command the command to register
     * @since 1.0.0-SNAPSHOT
     */
    public void registerSubcommand(@NotNull AdvancedCommand command) {
        this.subcommands.add(command);
    }

    public boolean canUse(CommandSender sender) {
        return sender.hasPermission(label.getPermission());
    }

    public CommandLabel getCommandLabel() {
        return label;
    }

    public CommandSettings.Settings getSettings() {
        return settings;
    }

    /**
     * Get the subcommands
     *
     * @return the commands
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public List<AdvancedCommand> getSubcommands() {
        return new ArrayList<>(subcommands);
    }

    /**
     * Get the arguments
     *
     * @return the commands
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Internal
    public Map<String, ArgumentType<?>> getArguments() {
        return new HashMap<>(arguments);
    }
}
