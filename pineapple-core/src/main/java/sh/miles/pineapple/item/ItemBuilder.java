package sh.miles.pineapple.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * A Useful ItemBuilder that provides a variety of methods used to build ItemStack's easily.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class ItemBuilder {

    private static final String TEXTURE_URL = "http://textures.minecraft.net/texture/";

    private ItemStack stack;
    private ItemMeta meta;

    private ItemBuilder() {
    }

    /**
     * Creates a new ItemBuilder from the given material
     *
     * @param material the material
     * @since 1.0.0-SNAPSHOT
     */
    private ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Creates a new ItemBuilder from the given material and amount
     *
     * @param material the material
     * @param amount   the amount
     * @since 1.0.0-SNAPSHOT
     */
    private ItemBuilder(Material material, int amount) {
        this.stack = new ItemStack(material, amount);
        this.meta = stack.getItemMeta();
    }

    /**
     * Creates a new ItemBuilder from the given material
     *
     * @param material the material
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    /**
     * Creates a new ItemBuilder from the given material and amount
     *
     * @param material the material
     * @param amount   the amount
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public static ItemBuilder of(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    /**
     * Creates an ItemBuilder that modifies the currently provided ItemStack
     *
     * @param stack the stack to modify
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public static ItemBuilder modifyStack(ItemStack stack) {
        ItemBuilder builder = new ItemBuilder();
        builder.stack = stack;
        builder.meta = stack.getItemMeta();
        return builder;
    }

    /**
     * Creates an ItemBuilder that clones and then modifies the provided ItemStack.
     *
     * @param stack the item stack to clone, then modify
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public static ItemBuilder modifyStackClone(ItemStack stack) {
        return modifyStack(stack.clone());
    }

    /**
     * Modifies the ItemMeta of the given specific class for this item
     *
     * @param metaType the meta type
     * @param action   the modification to be done
     * @param <T>      the type of ItemMeta
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public <T extends ItemMeta> ItemBuilder modify(Class<T> metaType, Consumer<T> action) {
        if (!metaType.isAssignableFrom(meta.getClass())) {
            return this;
        }

        action.accept(metaType.cast(meta));
        return this;
    }


    /**
     * Changes the name of the ItemStack
     *
     * @param name the name to set on the item
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder name(@NotNull final Component name) {
        this.meta.displayName(name);

        return this;
    }

    /**
     * Changes the lore of the ItemStack
     *
     * @param lore the lore to set on the item
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder lore(@NotNull final List<Component> lore) {
        List<Component> itemLore = getLore();
        itemLore.addAll(lore);

        this.meta.lore(itemLore);
        return this;
    }

    /**
     * Changes the lore of the ItemStack
     *
     * @param lore the lore to add on the item
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder lore(Component... lore) {
        return lore(Arrays.asList(lore));
    }

    /**
     * Enchants the given item at the given level
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Adds a stored enchant to the item given it is an EnchantedBook
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder storedEnchantment(Enchantment enchantment, int level) {
        return modify(EnchantmentStorageMeta.class, meta -> meta.addStoredEnchant(enchantment, level, true));
    }

    /**
     * Applies every enchantment in the map to the item
     *
     * @param enchantmentAndLevel the enchantment level map
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantmentAndLevel) {
        for (Map.Entry<Enchantment, Integer> entry : enchantmentAndLevel.entrySet()) {
            this.meta.addEnchant(entry.getKey(), entry.getValue(), true);
        }
        return this;
    }

    /**
     * Applies every enchantment to the item given its an EnchantmentBOok
     *
     * @param enchantmentAndLevel the enchantment level map
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder storedEnchantments(Map<Enchantment, Integer> enchantmentAndLevel) {
        for (final Map.Entry<Enchantment, Integer> entry : enchantmentAndLevel.entrySet()) {
            storedEnchantment(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Sets the item to unbreakable
     *
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder unbreakable() {
        this.meta.setUnbreakable(true);
        return this;
    }

    /**
     * Adds ItemFlag's to the ItemStack
     *
     * @param flags the flags to add
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder flags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    /**
     * Adds persistent data to the given meta
     *
     * @param key   the key
     * @param type  the type
     * @param value the value
     * @param <T>   the complex type
     * @param <Z>   the primitive type
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public <T, Z> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        this.meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    /**
     * Allows a function to be executed on the given meta
     *
     * @param consumer the function to be executed on the PersistentDataContainer
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder persistentData(Consumer<PersistentDataContainer> consumer) {
        consumer.accept(this.meta.getPersistentDataContainer());
        return this;
    }

    /**
     * Adds model data to the given item
     *
     * @param data the data
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder modelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    /**
     * Adds an AttributeModifier to the given ItemStack
     *
     * @param attribute the attribute
     * @param modifier  the modifier
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder modifier(Attribute attribute, AttributeModifier modifier) {
        this.meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * Sets the skull texture using the base64 string
     *
     * @param texture the base64 string
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder skullTexture(String texture) {
        return modify(SkullMeta.class, meta -> {
                    PlayerProfile profile = Bukkit.createProfile(UUID.nameUUIDFromBytes(texture.getBytes()));
                    PlayerTextures textures = profile.getTextures();
                    try {
                        textures.setSkin((new URI(TEXTURE_URL + texture)).toURL());
                    } catch (Exception ignored) {
                        return;
                    }

                    profile.setTextures(textures);
                    ((SkullMeta) this.meta).setPlayerProfile(profile);
                }
        );
    }

    /**
     * Sets the skull texture using the given player
     *
     * @param player the player
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder skullTexture(Player player) {
        if (!(this.meta instanceof SkullMeta)) {
            return this;
        }
        ((SkullMeta) this.meta).setOwningPlayer(player);
        return this;
    }

    /**
     * Sets the DyeColor on the given item
     *
     * @param color the color
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder dyeColor(Color color) {
        if (!(this.meta instanceof LeatherArmorMeta)) {
            return this;
        }

        ((LeatherArmorMeta) this.meta).setColor(color);
        return this;
    }

    /**
     * Sets the potionColor on the given item
     *
     * @param color the color
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder potionColor(Color color) {
        if (!(this.meta instanceof PotionMeta)) {
            return this;
        }

        ((PotionMeta) this.meta).setColor(color);
        return this;
    }

    /**
     * Sets the PotionEffect on the given potion item
     *
     * @param effect the effect
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder potionEffect(PotionEffect effect) {
        if (!(this.meta instanceof PotionMeta)) {
            return this;
        }

        ((PotionMeta) this.meta).addCustomEffect(effect, true);
        return this;
    }

    /**
     * Sets Multiple PotionEffects on the given potion item
     *
     * @param effects the effects
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder potionEffects(PotionEffect... effects) {
        if (!(this.meta instanceof PotionMeta potionMeta)) {
            return this;
        }

        for (PotionEffect effect : effects) {
            potionMeta.addCustomEffect(effect, true);
        }

        return this;
    }

    /**
     * Disables rendering the tooltip of an item
     *
     * @return the ItemBuilder
     * @since 1.0.0-SNAPSHOT
     */
    public ItemBuilder hideTooltip() {
        this.meta.setHideTooltip(true);

        return this;
    }

    private List<Component> getLore() {
        return this.meta.hasLore() ? this.meta.lore() : new ArrayList<>();
    }

    /**
     * Builds The ItemStack
     *
     * @return the item stack
     * @since 1.0.0-SNAPSHOT
     */
    public ItemStack build() {
        stack.setItemMeta(meta);


        return stack;
    }
}
