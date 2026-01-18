package sh.miles.pineapple.api.multiblock.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;
import sh.miles.pineapple.api.multiblock.event.base.PlayerMultiblockEvent;

public final class PlayerMultiblockInteractEvent extends PlayerMultiblockEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final EquipmentSlot hand;
    private final Block anchor;
    private final boolean newlyFormed;
    private boolean useItemInHand = true;
    private boolean interactWithBlock = true;

    @ApiStatus.Internal
    public PlayerMultiblockInteractEvent(final ActiveMultiblock multiblock, final Player player, EquipmentSlot hand, Block anchor, boolean newlyFormed) {
        super(multiblock, player);
        this.hand = hand;
        this.anchor = anchor;
        this.newlyFormed = newlyFormed;
    }

    public EquipmentSlot hand() {
        return this.hand;
    }

    public Block anchor() {
        return this.anchor;
    }

    public boolean newlyFormed() {
        return this.newlyFormed;
    }

    public boolean useItemInHand() {
        return this.useItemInHand;
    }

    public boolean interactWithBlock() {
        return this.interactWithBlock;
    }

    public void useItemInHand(boolean useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    public void interactWithBlock(boolean interactWithBlock) {
        this.interactWithBlock = interactWithBlock;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
