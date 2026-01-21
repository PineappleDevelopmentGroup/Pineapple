package sh.miles.pineapple.api.multiblock.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;
import sh.miles.pineapple.api.multiblock.event.MultiblockDestroyEvent;

public final class PlayerMultiblockDestroyEvent extends MultiblockDestroyEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;

    @ApiStatus.Internal
    public PlayerMultiblockDestroyEvent(final ActiveMultiblock multiblock, final Player player, boolean cancelled) {
        super(multiblock, cancelled);
        this.player = player;
    }

    public Player player() {
        return this.player;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
