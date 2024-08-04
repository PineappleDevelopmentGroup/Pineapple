package sh.miles.pineapple.command;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides information to a command like name usage etc
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class CommandLabel {

    /**
     * Default description of the label
     */
    public static final String DEFAULT_DESCRIPTION = "A Command created with Pineapple";

    private final String name;
    private final String permission;
    private final String description;
    private final List<String> aliases;

    /**
     * The command label
     *
     * @param name        the name of the command
     * @param permission  the permission of the command
     * @param description the description of the command
     * @param aliases     the aliases of the command
     * @since 1.0.0-SNAPSHOT
     */
    public CommandLabel(@NotNull final String name, @NotNull final String permission, @NotNull final String description, List<String> aliases) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(permission);
        Preconditions.checkNotNull(description);
        Preconditions.checkNotNull(aliases);

        this.name = name;
        this.permission = permission;
        this.description = description;
        this.aliases = aliases;
    }

    /**
     * Creates CommandLabel
     *
     * @param name        name
     * @param permission  permission
     * @param description description
     * @since 1.0.0-SNAPSHOT
     */
    public CommandLabel(@NotNull final String name, @NotNull final String permission, @NotNull final String description) {
        this(name, permission, description, new ArrayList<>());
    }

    /**
     * Creates CommandLabel
     *
     * @param name       name
     * @param permission permission
     * @since 1.0.0-SNAPSHOT
     */
    public CommandLabel(@NotNull final String name, @NotNull final String permission) {
        this(name, permission, DEFAULT_DESCRIPTION);
    }

    /**
     * Retrieves the name
     *
     * @return a string name
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the permission
     *
     * @return a string permission
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public String getPermission() {
        return this.permission;
    }

    /**
     * Retrieves the description
     *
     * @return a string description
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public String getDescription() {
        return this.description;
    }

    /**
     * Retrieves the aliases
     *
     * @return a list of aliases
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<String> getAliases() {
        return new ArrayList<>(this.aliases);
    }
}
