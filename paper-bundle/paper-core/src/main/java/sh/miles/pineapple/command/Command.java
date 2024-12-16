package sh.miles.pineapple.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Command class that wraps the normal {@link BasicCommand} and provides extra functionality without exposing
 * unnecessary access to command features
 *
 * @since 1.0.0-SNAPSHOT
 */
public class Command implements BasicCommand {

    private final CommandLabel label;
    private final CommandSettings settings;
    private final Map<String, Command> subcommands;

    public Command(@NotNull final CommandLabel label) {
        this(label, CommandSettings.GENERIC, new ArrayList<>());
    }

    public Command(@NotNull final CommandLabel label, @NotNull final CommandSettings settings) {
        this(label, settings, new ArrayList<>());
    }

    public Command(@NotNull final CommandLabel label, @NotNull List<Command> subcommands) {
        this(label, CommandSettings.GENERIC, subcommands);
    }

    public Command(@NotNull final CommandLabel label, @NotNull final CommandSettings settings, @NotNull final List<Command> subcommands) {
        this.label = label;
        this.settings = settings;
        this.subcommands = subcommands.stream().mapMulti((Command command, Consumer<Stream<Pair<String, Command>>> consumer) -> {
            final List<String> list = new ArrayList<>(command.label.getAliases());
            list.add(command.label.getName());
            consumer.accept(list.stream().map((key) -> Pair.of(key, command)));
        }).flatMap((stream) -> stream).collect(
                Collectors.toMap(
                        Pair::left,
                        Pair::right
                )
        );
    }

    @Override
    public void execute(@NotNull final CommandSourceStack source, @NotNull final String @NotNull [] args) {
        final CommandSender sender = source.getSender();
        if (!sender.hasPermission(this.label.getPermission())) {
            sender.sendMessage(this.settings.noPermission());
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(this.settings.invalidUsage());
            return;
        }

        final Command subCommand = this.subcommands.get(args[0]);
        if (subCommand == null) {
            sender.sendMessage(this.settings.invalidUsage());
            return;
        }

        subCommand.execute(source, cut(args));
    }

    @NotNull
    @Override
    public Collection<String> suggest(final @NotNull CommandSourceStack source, final @NotNull String @NotNull [] args) {
        if (subcommands.isEmpty()) {
            return List.of();
        }

        if (args.length == 0) {
            return List.of();
        }

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], this.subcommands.keySet(), new ArrayList<>());
        }

        final Command subCommand = this.subcommands.get(args[0]);
        if (subCommand == null) {
            return List.of();
        }

        return subCommand.suggest(source, cut(args));
    }

    @NotNull
    protected static String @NotNull [] cut(@NotNull final String @NotNull [] args) {
        final String[] copy = new String[args.length - 1];
        System.arraycopy(args, 1, copy, 0, copy.length);
        return copy;
    }
}
