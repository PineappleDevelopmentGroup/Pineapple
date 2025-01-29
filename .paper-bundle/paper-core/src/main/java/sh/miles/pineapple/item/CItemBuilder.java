package sh.miles.pineapple.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.datacomponent.DataComponentType.NonValued;
import io.papermc.paper.datacomponent.DataComponentType.Valued;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.Unbreakable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A Useful ItemBuilder that provides a variety of methods used to build ItemStack's easily.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class CItemBuilder {

    public static final String TEXTURE_URL = "http://textures.minecraft.net/texture/";

    private ItemStack stack;

    private CItemBuilder() {
    }

    private CItemBuilder(ItemType itemType) {
        this(itemType, 1);
    }

    private CItemBuilder(ItemType itemType, int amount) {
        this.stack = itemType.createItemStack(amount);
    }

    /**
     * Modifies the given component type
     *
     * @param componentType the component type to modify
     * @param modification  the modification function
     * @param <T>           the type of the valued component
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public <T> CItemBuilder modify(@NotNull final Valued<T> componentType, @NotNull final Function<@Nullable T, @Nullable T> modification) {
        final T modded = modification.apply(this.stack.getData(componentType));
        if (modded == null) {
            this.stack.unsetData(componentType);
        }
        this.stack.setData(componentType, modded);
        return this;
    }

    /**
     * Modifies the given NonValued component
     *
     * @param componentType the component type to modify
     * @param status        the applied status to set the component type to, true being set, false being unset
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CItemBuilder modify(@NotNull final NonValued componentType, final boolean status) {
        final boolean exists = this.stack.hasData(componentType);
        if (exists && !status) {
            this.stack.unsetData(componentType);
        } else if (!exists && status) {
            this.stack.setData(componentType);
        }

        return this;
    }

    /**
     * Sets the name of this item
     *
     * @param name the name of the item to set
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public final CItemBuilder name(@NotNull final Component name) {
        modify(DataComponentTypes.ITEM_NAME, (o) -> name);
        return this;
    }

    /**
     * Adds to the lore of this item
     *
     * @param lore the lore to add
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public final CItemBuilder lore(@NotNull final List<Component> lore) {
        modify(DataComponentTypes.LORE, (old) -> {
            if (old == null) {
                return ItemLore.lore().addLines(lore).build();
            }

            return ItemLore.lore().addLines(old.lines()).addLines(lore).build();
        });
        return this;
    }

    /**
     * Adds to the lore of this item
     *
     * @param lore the lore to add
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public final CItemBuilder lore(@NotNull final Component... lore) {
        return lore(Arrays.asList(lore));
    }

    /**
     * Adds an enchantment to this item
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public final CItemBuilder enchantment(@NotNull final Enchantment enchantment, final int level) {
        modify(DataComponentTypes.ENCHANTMENTS, (old) -> {
            if (old == null) {
                return ItemEnchantments.itemEnchantments().add(enchantment, level).build();
            }

            return ItemEnchantments.itemEnchantments().addAll(old.enchantments()).add(enchantment, level).build();
        });
        return this;
    }

    /**
     * Adds a stored enchantment to this item
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public final CItemBuilder storedEnchantment(@NotNull final Enchantment enchantment, final int level) {
        modify(DataComponentTypes.STORED_ENCHANTMENTS, (old) -> {
            if (old == null) {
                return ItemEnchantments.itemEnchantments().add(enchantment, level).build();
            }

            return ItemEnchantments.itemEnchantments().addAll(old.enchantments()).add(enchantment, level).build();
        });
        return this;
    }

    /**
     * Changes the item's unbreakable status
     *
     * @param status the status, true for unbreakable, false for not
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public final CItemBuilder unbreakable(boolean status) {
        modify(DataComponentTypes.UNBREAKABLE, (old) -> {
            if (old != null && status) {
                return old;
            }

            if (status) {
                return Unbreakable.unbreakable(true);
            }

            return null;
        });
        return this;
    }

    /**
     * Hides every tooltip on this item
     *
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CItemBuilder hideAllTooltips() {
        modify(DataComponentTypes.ENCHANTMENTS, (old) -> {
            if (old != null) {
                return old.showInTooltip(false);
            }

            return null;
        });

        modify(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, true);
        modify(DataComponentTypes.HIDE_TOOLTIP, true);

        modify(DataComponentTypes.STORED_ENCHANTMENTS, (old) -> {
            if (old != null) {
                return old.showInTooltip(false);
            }

            return null;
        });

        modify(DataComponentTypes.DYED_COLOR, (old) -> {
            if (old != null) {
                return old.showInTooltip(false);
            }

            return null;
        });

        modify(DataComponentTypes.TRIM, (old) -> {
            if (old != null) {
                return old.showInTooltip(false);
            }

            return null;
        });

        return this;
    }

    /**
     * Sets persistent data on this item. This method should only be used if only one or two persistent data keys are
     * added. {@link #persistentData(Consumer)} is much more performant at scale.
     *
     * @param key   the key of the data
     * @param type  the type of the data
     * @param value the data value
     * @param <P>   the primitive type
     * @param <C>   the complex type
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @ApiStatus.Obsolete
    public <P, C> CItemBuilder persistentData(@NotNull final NamespacedKey key, @NotNull final PersistentDataType<P, C> type, @NotNull final C value) {
        final ItemMeta meta = this.stack.getItemMeta(); // this task gets heavier :(
        meta.getPersistentDataContainer().set(key, type, value);
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Manages persistent data on this item.
     *
     * @param modify the container function.
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CItemBuilder persistentData(@NotNull final Consumer<PersistentDataContainer> modify) {
        final ItemMeta meta = this.stack.getItemMeta();
        modify.accept(meta.getPersistentDataContainer());
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets custom model data on this item
     *
     * @param modelData the model data
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CItemBuilder customModelData(final int modelData) {
        modify(DataComponentTypes.CUSTOM_MODEL_DATA, (data) -> CustomModelData.customModelData(modelData));
        return this;
    }

    /**
     * Applies an item model to this item
     *
     * @param modelKey the model texture key
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CItemBuilder itemModel(@NotNull final Key modelKey) {
        modify(DataComponentTypes.ITEM_MODEL, (model) -> modelKey);
        return this;
    }

    /**
     * Applies a skull texture to this item
     *
     * @param link the link of the skull texture
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CItemBuilder skullTexture(@NotNull final String link) {
        modify(DataComponentTypes.PROFILE, (old) -> {
            final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            final PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL(link));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            profile.setTextures(textures);
            return ResolvableProfile.resolvableProfile(profile);
        });
        return this;
    }

    /**
     * Applies a skull texture to this item
     *
     * @param player the player skull to use
     * @return this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public CItemBuilder skullTexture(@NotNull final OfflinePlayer player) {
        modify(DataComponentTypes.PROFILE, (old) -> {
            return ResolvableProfile.resolvableProfile(player.getPlayerProfile());
        });
        return this;
    }

    /**
     * Builds the item builder
     *
     * @return a clone of the builder stack
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public ItemStack build() {
        return this.stack.clone();
    }

    /**
     * Returns the raw modifiable ItemStack from this builder
     * <p>
     * This method is marginally faster than {@link #build()} but runs the risk of the returned ItemStack being
     * modified.
     *
     * @return the raw mutable ItemStack from this builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public ItemStack buildRaw() {
        return this.stack;
    }
}
