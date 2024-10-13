package sh.miles.pineapple.nms.api.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents all components related to chat
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface ChatComponents {

    /**
     * Sends a message to the given player
     *
     * @param player the player to send the message to
     * @since 1.0.0-SNAPSHOT
     */
    void sendMessage(@NotNull final Player player, @NotNull final Component component);

    /**
     * Sends a title to the given player
     *
     * @param player the player to send the title to
     * @param title  the title to send
     * @since 1.0.0-SNAPSHOT
     */
    void sendTitle(@NotNull final Player player, @NotNull final Title title);

    /**
     * Broadcasts a component to the entire server
     *
     * @param component the component to send
     * @since 1.0.0-SNAPSHOT
     */
    void broadcast(@NotNull final Component component);
}
