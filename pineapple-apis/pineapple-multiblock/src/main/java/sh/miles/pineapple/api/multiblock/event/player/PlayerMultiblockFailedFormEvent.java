package sh.miles.pineapple.api.multiblock.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;
import sh.miles.pineapple.api.multiblock.event.base.PlayerMultiblockEvent;

public class PlayerMultiblockFailedFormEvent extends PlayerMultiblockEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public PlayerMultiblockFailedFormEvent(final ActiveMultiblock multiblock, final Player player) {
        super(multiblock, player);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
