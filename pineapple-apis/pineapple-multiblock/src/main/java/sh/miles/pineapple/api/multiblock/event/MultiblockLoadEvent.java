package sh.miles.pineapple.api.multiblock.event;

import net.kyori.adventure.key.Key;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;
import sh.miles.pineapple.util.PdcUtils;

public class MultiblockLoadEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PersistentDataContainer dataStore;
    private final ActiveMultiblock activeMultiblock;

    @ApiStatus.Internal
    public MultiblockLoadEvent(ActiveMultiblock multiblock, PersistentDataContainer container) {
        this.dataStore = container;
        this.activeMultiblock = multiblock;
    }

    public boolean has(Key key) {
        return this.dataStore.has(PdcUtils.keyToLegacy(key));
    }

    public PersistentDataContainer get(Key key) {
        return this.dataStore.get(PdcUtils.keyToLegacy(key), PersistentDataType.TAG_CONTAINER);
    }

    public ActiveMultiblock multiblock() {
        return this.activeMultiblock;
    }


    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
