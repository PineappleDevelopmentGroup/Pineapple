package sh.miles.pineapple.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents CommandSettings that can be applied to a command for enhanced feature sets
 *
 * @since 1.0.0-SNAPSHOT
 */
public class CommandSettings {

    private static final Component DEFAULT_PERMISSION_MESSAGE = Component.text("You do not have permission for this");
    private static final Component DEFAULT_INVALID_SENDER_MESSAGE = Component.text("You are not a valid sender for this command");
    public static final Settings DEFAULT_COMMAND_SETTINGS = new Settings(DEFAULT_PERMISSION_MESSAGE,
            DEFAULT_INVALID_SENDER_MESSAGE
    );

    private Component permissionMessage;
    private Component invalidSenderMessage;

    /**
     * Sets the permission message
     *
     * @param permissionMessage the message to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setPermissionMessage(Component permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    /**
     * Sets the invalid sender message
     *
     * @param invalidSenderMessage the invalid sender message to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setInvalidSenderMessage(Component invalidSenderMessage) {
        this.invalidSenderMessage = invalidSenderMessage;
    }

    /**
     * Builds the CommandSettings instance into a immutable {@link Settings} record which can not be modified
     *
     * @return a Settings instance
     * @since 1.0.0-SNAPSHOT
     */
    public Settings build() {
        return new Settings(permissionMessage, invalidSenderMessage);
    }

    /**
     * Gets default permission
     *
     * @return the component
     * @since 1.0.0-SNAPSHOT
     */
    public static Component getDefaultPermissionMessage() {
        return DEFAULT_PERMISSION_MESSAGE;
    }

    /**
     * Gets default invalid sender
     *
     * @return the base component
     * @since 1.0.0-SNAPSHOT
     */
    public static Component getDefaultInvalidSenderMessage() {
        return DEFAULT_INVALID_SENDER_MESSAGE;
    }

    /**
     * A Build Settings Object comprised from the Settings Builder
     *
     * @param permissionMessage    the permission message
     * @param invalidSenderMessage the invalid sender message
     * @since 1.0.0-SNAPSHOT
     */
    public record Settings(Component permissionMessage, Component invalidSenderMessage) {

        /**
         * Sends the permission message
         *
         * @param sender the sender
         * @since 1.0.0-SNAPSHOT
         */
        public void sendPermissionMessage(CommandSender sender) {
            if (permissionMessage != null) {
                sender.sendMessage(permissionMessage);
            }
        }

        /**
         * sends the invalid sender message
         *
         * @param sender the sender
         * @since 1.0.0-SNAPSHOT
         */
        @ApiStatus.Obsolete
        public void sendInvalidSenderMessage(CommandSender sender) {
            if (invalidSenderMessage != null) {
                sender.sendMessage(invalidSenderMessage);
            }
        }
    }

}
