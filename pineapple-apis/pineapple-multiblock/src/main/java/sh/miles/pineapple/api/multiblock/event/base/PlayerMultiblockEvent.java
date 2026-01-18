package sh.miles.pineapple.api.multiblock.event.base;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;

@ApiStatus.Internal
public abstract class PlayerMultiblockEvent extends MultiblockEvent {

    private final Player player;

    @ApiStatus.Internal
    protected PlayerMultiblockEvent(ActiveMultiblock multiblock, Player player) {
        super(multiblock);
        this.player = player;
    }

    public Player player() {
        return this.player;
    }

}
