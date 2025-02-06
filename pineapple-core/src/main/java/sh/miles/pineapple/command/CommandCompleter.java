package sh.miles.pineapple.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Equivalent to {@link TabCompleter}
 *
 * @since 1.0.0-SNAPSHOT
 */
@FunctionalInterface
public interface CommandCompleter {

    /**
     * Completes tab completes
     *
     * @param sender the sender to complete
     * @param args   the args
     * @return the list string
     * @since 1.0.0-SNAPSHOT
     */
    List<String> complete(@NotNull final CommandSender sender, @NotNull final String[] args);

}
