package sh.miles.pineapple.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents CommandSettings that can be applied to a command for enhanced feature sets
 *
 * @since 1.0.0-SNAPSHOT
 */
public record CommandSettings(@NotNull Component noPermission, @NotNull Component invalidUsage) {

    /**
     * Represents generic command settings
     */
    public static final CommandSettings GENERIC = new CommandSettings(
            Component.text("You do not have permission to execute this command!").color(NamedTextColor.RED),
            Component.text("You did not enter a valid command usage!").color(NamedTextColor.RED)
    );

}
