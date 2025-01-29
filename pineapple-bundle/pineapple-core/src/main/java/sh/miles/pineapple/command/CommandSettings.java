package sh.miles.pineapple.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents CommandSettings that can be applied to a command for enhanced feature sets
 *
 * @since 1.0.0-SNAPSHOT
 */
public class CommandSettings {

    private static final BaseComponent DEFAULT_PERMISSION_MESSAGE = new TextComponent("You do not have permission for this");
    private static final BaseComponent DEFAULT_INVALID_SENDER_MESSAGE = new TextComponent("You are not a valid sender for this command");
    public static final Settings DEFAULT_COMMAND_SETTINGS = new Settings(DEFAULT_PERMISSION_MESSAGE, DEFAULT_INVALID_SENDER_MESSAGE);

    private BaseComponent permissionMessage;
    private BaseComponent invalidSenderMessage;

    /**
     * Sets the permission message
     *
     * @param permissionMessage the message to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setPermissionMessage(BaseComponent permissionMessage) {
        this.permissionMessage = permissionMessage.duplicate();
    }

    /**
     * Sets the invalid sender message
     *
     * @param invalidSenderMessage the invalid sender message to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setInvalidSenderMessage(BaseComponent invalidSenderMessage) {
        this.invalidSenderMessage = invalidSenderMessage.duplicate();
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
    public static BaseComponent getDefaultPermissionMessage() {
        return DEFAULT_PERMISSION_MESSAGE.duplicate();
    }

    /**
     * Gets default invalid sender
     *
     * @return the base component
     * @since 1.0.0-SNAPSHOT
     */
    public static BaseComponent getDefaultInvalidSenderMessage() {
        return DEFAULT_INVALID_SENDER_MESSAGE.duplicate();
    }

    /**
     * A Build Settings Object comprised from the Settings Builder
     *
     * @param permissionMessage    the permission message
     * @param invalidSenderMessage the invalid sender message
     * @since 1.0.0-SNAPSHOT
     */
    public record Settings(BaseComponent permissionMessage, BaseComponent invalidSenderMessage) {

        /**
         * Sends the permission message
         *
         * @param sender the sender
         * @since 1.0.0-SNAPSHOT
         */
        public void sendPermissionMessage(CommandSender sender) {
            if (permissionMessage != null) {
                sender.spigot().sendMessage(permissionMessage);
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
                sender.spigot().sendMessage(invalidSenderMessage);
            }
        }
    }

}
