package sh.miles.pineapple.api.multiblock.event.base;

import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;

@ApiStatus.Internal
public abstract class MultiblockEvent extends Event {

    private final ActiveMultiblock multiblock;

    @ApiStatus.Internal
    protected MultiblockEvent(ActiveMultiblock multiblock) {
        this.multiblock = multiblock;
    }

    public ActiveMultiblock multiblock() {
        return this.multiblock;
    }

}
