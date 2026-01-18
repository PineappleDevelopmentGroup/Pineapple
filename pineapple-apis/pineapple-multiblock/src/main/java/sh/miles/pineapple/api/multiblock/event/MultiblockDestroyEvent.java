package sh.miles.pineapple.api.multiblock.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;
import sh.miles.pineapple.api.multiblock.event.base.MultiblockEvent;

public class MultiblockDestroyEvent extends MultiblockEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    @ApiStatus.Internal
    public MultiblockDestroyEvent(ActiveMultiblock multiblock, boolean cancelled) {
        super(multiblock);
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
