package sh.miles.pineapple.api.multiblock.event;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import sh.miles.pineapple.api.multiblock.ActiveMultiblock;
import sh.miles.pineapple.util.PdcUtils;

import java.util.function.Consumer;

public class MultiblockUnloadEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PersistentDataContainer dataStore;
    private final ActiveMultiblock activeMultiblock;

    @ApiStatus.Internal
    public MultiblockUnloadEvent(ActiveMultiblock multiblock, PersistentDataContainer dataStore) {
        this.dataStore = dataStore;
        this.activeMultiblock = multiblock;
    }

    public void store(Key key, PersistentDataContainer data) {
        this.dataStore.set(PdcUtils.keyToLegacy(key), PersistentDataType.TAG_CONTAINER, data);
    }

    public void edit(Key key, Consumer<PersistentDataContainer> edit) {
        NamespacedKey dumKey = PdcUtils.keyToLegacy(key);
        PersistentDataContainer container;
        if (!this.dataStore.has(dumKey)) {
            container = this.dataStore.getAdapterContext().newPersistentDataContainer();
        } else {
            container = this.dataStore.get(dumKey, PersistentDataType.TAG_CONTAINER);
        }

        edit.accept(container);
        this.dataStore.set(dumKey, PersistentDataType.TAG_CONTAINER, container);
    }

    public ActiveMultiblock multiblock() {
        return this.activeMultiblock;
    }

    public PersistentDataContainer newContainer() {
        return this.dataStore.getAdapterContext().newPersistentDataContainer();
    }


    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
