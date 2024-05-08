package sh.miles.pineapple.item;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.nms.annotations.NMS;
import sh.miles.pineapple.nms.api.PineappleNMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class ItemSpec {

    public static final int INT_DATA_UNSET = -999;

    private Material itemType;
    private int amount = 1;

    // Display Data
    private String name;
    private Function<String, BaseComponent> nameMutator = null;
    private final List<String> lore = new ArrayList<>(0);
    private Function<String, BaseComponent> loreMutator = null;
    private final List<ItemFlag> hideToolTips = new ArrayList<>(0);
    private int customModelData = INT_DATA_UNSET;
    private Function<String, BaseComponent> defaultTextMutator = TextComponent::new;
    // Display Data end

    // Attributes
    private final Multimap<Attribute, AttributeModifier> attributeModifiers = ArrayListMultimap.create(0, 0);
    // Attributes end

    // Enchantments
    private final Map<Enchantment, Integer> enchantments = new HashMap<>(0);
    // Enchantments end

    // Durability
    private int durability = INT_DATA_UNSET;
    private boolean unbreakable = false;
    // Durability end

    // Potion Effects
    private final List<PotionEffect> effects = new ArrayList<>(0);
    private Color potionColor;
    // Potion Effects end

    // Armor
    private ArmorTrim armorTrim;
    private Color armorColor;
    // Armor end

    // Armor Stands, Spawn Eggs, Item Frames and Paintings
    // SKIPPED
    // Armor Stands, Spawn Eggs, Item Frames and Paintings end

    // Book and Quills
    // SKIPPED
    // Book and Quills end

    // Written Books
    // SKIPPED
    // Written Books end

    // Knowledge Books
    private final Map<Enchantment, Integer> storedEnchantments = new HashMap<>(0);
    // Knowledge Books end

    // Buckets of Aquatic Mob
    private TropicalFish.Pattern fishPattern;
    private DyeColor fishPatternColor;
    private DyeColor fishBodyColor;
    // Buckets of Aquatic Mob end

    // Bundle
    // SKIPPED
    // Bundle end

    // Compasses
    private boolean isLodestoneTracked;
    private Location lodestoneLocation;
    // Compasses end

    // Crossbows
    private final List<ItemStack> chargedProjectiles = new ArrayList<>(0);
    // Crossbows end

    // Firework Rockets
    // SKIPPED
    // Firework Rockets end

    // Firework Stars
    // SKIPPED
    // Firework Stars end

    // Goat Horns
    // SKIPPED
    // Goat Horns end

    // Maps
    // SKIPPED
    // Maps end

    // Player Heads
    // SKIPPED
    // Player Heads end

    // Suspicious Stew
    // SKIPPED
    // Suspicious Stew end

    /**
     * Creates a new ItemSpec
     *
     * @param itemType the itemType to use for this spec
     * @since 1.0.0-SNAPSHOT
     */
    public ItemSpec(@NotNull final Material itemType) {
        checkArgument(itemType.isItem(), "The provided itemType of %s is not an item".formatted(itemType));
        this.itemType = itemType;
    }

    /**
     * Sets the ItemType of this ItemSpec
     *
     * @param itemType the itemType to use
     * @since 1.0.0-SNAPSHOT
     */
    public void setItemType(final Material itemType) {
        checkArgument(itemType != null, "The given item type must not be null");
        checkArgument(itemType.isItem(), "The provided itemType of %s is not an item".formatted(itemType));
        this.itemType = itemType;
    }

    /**
     * @return the item type
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Material getItemType() {
        return itemType;
    }

    /**
     * Sets the amount of this item between 0 and 65 exclusively
     *
     * @param amount the amount
     * @since 1.0.0-SNAPSHOT
     */
    public void setAmount(final int amount) {
        checkArgument(amount > 0 && amount < 65, "The given amount must be between 0 and 65 exclusively %d is not in this range".formatted(amount));
        this.amount = amount;
    }

    /**
     * Gets the amount items in this spec
     *
     * @return the amount of items
     * @since 1.0.0-SNAPSHOT
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Adds a lore line to the lore
     *
     * @param loreLine the lore line
     * @since 1.0.0-SNAPSHOT
     */
    public void addLoreLine(@NotNull final String loreLine) {
        checkArgument(loreLine != null, "the given loreLine must not be null");
        this.lore.add(loreLine);
    }

    /**
     * Sets the lore to the given list
     *
     * @param lore the lore to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setLore(@NotNull final List<String> lore) {
        checkArgument(lore != null, "The given lore must not be null");
        this.lore.clear();
        this.lore.addAll(lore);
    }

    /**
     * Sets the name of the item
     *
     * @param name the name to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setName(@NotNull final String name) {
        checkArgument(name != null, "The given name must not be null");
        this.name = name;
    }

    /**
     * Gets the name of the item
     *
     * @return the name of the item, or null
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * The name mutation function
     *
     * @param nameMutator the name mutator
     * @since 1.0.0-SNAPSHOT
     */
    public void setNameMutator(@NotNull final Function<String, BaseComponent> nameMutator) {
        checkArgument(nameMutator != null, "The name mutator must not be null");
        this.nameMutator = nameMutator;
    }

    /**
     * Gets the lore
     *
     * @return the lore
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<String> getLore() {
        return new ArrayList<>(lore);
    }

    /**
     * Sets the lore mutator for mutating to components
     *
     * @param loreMutator the lore mutator
     * @since 1.0.0-SNAPSHOT
     */
    public void setLoreMutator(@NotNull final Function<String, BaseComponent> loreMutator) {
        checkArgument(loreMutator != null, "The lore mutator must not be null");
        this.loreMutator = loreMutator;
    }

    /**
     * Sets the default text mutator to be used if no {@link #setNameMutator(Function)} or
     * {@link #setLoreMutator(Function)} have not been given a non null value
     *
     * @param defaultTextMutator the mutation to apply to transition a string to a BaseComponent
     * @since 1.0.0-SNAPSHOT
     */
    public void setDefaultTextMutator(@NotNull final Function<String, BaseComponent> defaultTextMutator) {
        checkArgument(defaultTextMutator != null, "The default text mutator must not be null");
        this.defaultTextMutator = defaultTextMutator;
    }

    /**
     * Adds a tooltip to the spec
     *
     * @param tooltip the tooltip to add
     * @since 1.0.0-SNAPSHOT
     */
    public void addHideToolTip(@NotNull final ItemFlag tooltip) {
        checkArgument(tooltip != null, "The tooltip must not be null");
        this.hideToolTips.add(tooltip);
    }

    /**
     * Sets the tooltips to hide
     *
     * @param tooltips the tooltips
     * @since 1.0.0-SNAPSHOT
     */
    public void setHideToolTips(@NotNull final List<ItemFlag> tooltips) {
        checkArgument(tooltips != null, "The given tooltips must not be null");
        this.hideToolTips.clear();
        this.hideToolTips.addAll(tooltips);
    }

    /**
     * Gets the hidden tooltips
     *
     * @return the hidden tooltips
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<ItemFlag> getHideToolTips() {
        return new ArrayList<>(hideToolTips);
    }

    /**
     * Sets the custom model data
     *
     * @param customModelData the custom model data
     * @since 1.0.0-SNAPSHOT
     */
    public void setCustomModelData(final int customModelData) {
        this.customModelData = customModelData;
    }

    /**
     * @return get the custom model data
     * @since 1.0.0-SNAPSHOT
     */
    public int getCustomModelData() {
        return customModelData;
    }

    /**
     * Add an attribute modifier
     *
     * @param attribute         the attribute
     * @param attributeModifier the attribute modifier to add
     * @since 1.0.0-SNAPSHOT
     */
    public void addAttributeModifier(@NotNull final Attribute attribute, @NotNull final AttributeModifier attributeModifier) {
        checkArgument(attributeModifier != null, "the given attribute modifier must not be null");
        this.attributeModifiers.put(attribute, attributeModifier);
    }

    /**
     * Sets the attribute modifiers
     *
     * @param attributeModifiers the attribute modifiers to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setAttributeModifiers(@NotNull final Multimap<Attribute, AttributeModifier> attributeModifiers) {
        checkArgument(attributeModifiers != null, "the given attribute modifiers must not be null");
        this.attributeModifiers.clear();
        this.attributeModifiers.putAll(attributeModifiers);
    }

    /**
     * Gets the attribute modifiers
     *
     * @return the attribute modifiers
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return ArrayListMultimap.create(this.attributeModifiers);
    }

    /**
     * adds an enchantment
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @since 1.0.0-SNAPSHOT
     */
    public void addEnchantment(@NotNull final Enchantment enchantment, final int level) {
        checkArgument(enchantment != null, "The enchantment must not be null");
        checkArgument(level > 0 && level < 256, "the level must be greater than 0");
        this.enchantments.put(enchantment, level);
    }

    /**
     * Sets all the enchantments
     *
     * @param enchantments the enchantments
     * @since 1.0.0-SNAPSHOT
     */
    public void setEnchantments(@NotNull final Map<Enchantment, Integer> enchantments) {
        checkArgument(enchantments != null, "The enchantments must not be null");
        this.enchantments.clear();
        this.enchantments.putAll(enchantments);
    }

    /**
     * Gets all the enchantments
     *
     * @return the enchantments
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Map<Enchantment, Integer> getEnchantments() {
        return new HashMap<>(enchantments);
    }

    /**
     * sets the durability
     *
     * @param durability the durability
     * @since 1.0.0-SNAPSHOT
     */
    public void setDurability(final int durability) {
        this.durability = durability;
    }

    /**
     * @return the durability
     * @since 1.0.0-SNAPSHOT
     */
    public int getDurability() {
        return durability;
    }

    /**
     * Sets unbreakable or not
     *
     * @param unbreakable whether to be unbreakable
     * @since 1.0.0-SNAPSHOT
     */
    public void setUnbreakable(final boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    /**
     * determines if unbreakable
     *
     * @return true if unbreakable
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * Adds a potion effect
     *
     * @param effect the effect to add
     * @since 1.0.0-SNAPSHOT
     */
    public void addPotionEffect(@NotNull final PotionEffect effect) {
        checkArgument(effect != null, "The given effect must not be null");
        this.effects.add(effect);
    }

    /**
     * Sets potion effects
     *
     * @param effects the effects to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setPotionEffects(@NotNull final List<PotionEffect> effects) {
        checkArgument(effects != null, "The given effects must not be null");
    }

    /**
     * Gets the potion effects
     *
     * @return the effects
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<PotionEffect> getEffects() {
        return new ArrayList<>(effects);
    }

    /**
     * Gets the potion color
     *
     * @param potionColor potion color
     * @since 1.0.0-SNAPSHOT
     */
    public void setPotionColor(@NotNull final Color potionColor) {
        checkArgument(potionColor != null, "potion color must not be null");
        this.potionColor = potionColor;
    }

    /**
     * Gets potion color
     *
     * @return potion color
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Color getPotionColor() {
        return potionColor;
    }

    /**
     * Sets the armor trim
     *
     * @param armorTrim the armor trim to set
     * @since 1.0.0-SNAPSHOT
     */
    public void setArmorTrim(@NotNull final ArmorTrim armorTrim) {
        checkArgument(armorTrim != null, "the armor trim must not be null");
        this.armorTrim = armorTrim;
    }

    /**
     * Gets the armor trim
     *
     * @return the armor trim
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public ArmorTrim getArmorTrim() {
        return armorTrim;
    }

    /**
     * Sets Armor color
     *
     * @param armorColor armor color
     */
    public void setArmorColor(@NotNull final Color armorColor) {
        checkArgument(armorColor != null, "armor color must not be null");
        this.armorColor = armorColor;
    }

    /**
     * Gets the armor color
     *
     * @return armor color
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Color getArmorColor() {
        return armorColor;
    }

    /**
     * adds an enchantment
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @since 1.0.0-SNAPSHOT
     */
    public void addStoredEnchantment(@NotNull final Enchantment enchantment, final int level) {
        checkArgument(enchantment != null, "The enchantment must not be null");
        checkArgument(level > 0 && level < 256, "the level must be greater than 0");
        this.storedEnchantments.put(enchantment, level);
    }

    /**
     * Sets all the enchantments
     *
     * @param enchantments the enchantments
     * @since 1.0.0-SNAPSHOT
     */
    public void setStoredEnchantments(@NotNull final Map<Enchantment, Integer> enchantments) {
        checkArgument(enchantments != null, "The enchantments must not be null");
        this.storedEnchantments.clear();
        this.storedEnchantments.putAll(enchantments);
    }

    /**
     * Gets stored enchantments
     *
     * @return stored enchantments
     */
    @NotNull
    public Map<Enchantment, Integer> getStoredEnchantments() {
        return new HashMap<>(storedEnchantments);
    }

    /**
     * Sets the fish pattern
     *
     * @param fishPattern fish pattern
     * @since 1.0.0-SNAPSHOT
     */
    public void setFishPattern(@NotNull final TropicalFish.Pattern fishPattern) {
        checkArgument(fishPattern != null, "the fish pattern must not be null");
        this.fishPattern = fishPattern;
    }

    /**
     * @return get fish pattern
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public TropicalFish.Pattern getFishPattern() {
        return fishPattern;
    }

    /**
     * Sets fish pattern color
     *
     * @param fishPatternColor fish pattern color
     * @since 1.0.0-SNAPSHOT
     */
    public void setFishPatternColor(@NotNull final DyeColor fishPatternColor) {
        checkArgument(fishPatternColor != null, "the fish pattern color must not be null");
        this.fishPatternColor = fishPatternColor;
    }

    /**
     * @return get fish pattern color
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public DyeColor getFishPatternColor() {
        return fishPatternColor;
    }

    /**
     * Sets the fish body color
     *
     * @param fishBodyColor fish body color
     */
    public void setFishBodyColor(@NotNull final DyeColor fishBodyColor) {
        checkArgument(fishBodyColor != null, "fish body color must not be null");
        this.fishBodyColor = fishBodyColor;
    }

    /**
     * @return get fish body color
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public DyeColor getFishBodyColor() {
        return fishBodyColor;
    }

    /**
     * sets if lodestone tracked
     *
     * @param lodestoneTracked status
     * @since 1.0.0-SNAPSHOT
     */
    public void setLodestoneTracked(final boolean lodestoneTracked) {
        isLodestoneTracked = lodestoneTracked;
    }

    /**
     * @return true if lodestone tracked
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isLodestoneTracked() {
        return isLodestoneTracked;
    }

    /**
     * Sets lodestone location
     *
     * @param lodestoneLocation lcoation
     * @since 1.0.0-SNAPSHOT
     */
    public void setLodestoneLocation(@NotNull final Location lodestoneLocation) {
        checkArgument(lodestoneLocation != null, "the location must not be null");
        this.lodestoneLocation = lodestoneLocation;
    }

    /**
     * Gets lodestone location location
     *
     * @return the location
     * @since 1.0.0-SNAPSHOT
     */
    @Nullable
    public Location getLodestoneLocation() {
        return lodestoneLocation;
    }

    /**
     * Adds charged projectile
     *
     * @param chargedProjectile the projectile to add
     * @since 1.0.0-SNAPSHOT
     */
    public void addChargedProjectile(@NotNull final ItemStack chargedProjectile) {
        checkArgument(chargedProjectile != null, "the projectile must not be null");
        this.chargedProjectiles.add(chargedProjectile);
    }

    /**
     * Sets charged projectiles
     *
     * @param chargedProjectiles the charged projectiles
     * @since 1.0.0-SNAPSHOT
     */
    public void setChargedProjectiles(@NotNull final List<ItemStack> chargedProjectiles) {
        checkArgument(chargedProjectiles != null, "the projectiles list must not be null");
        this.chargedProjectiles.clear();
        this.chargedProjectiles.addAll(chargedProjectiles);
    }

    /**
     * Gets all charged projectiles
     *
     * @return charged projectiles
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public List<ItemStack> getChargedProjectiles() {
        return new ArrayList<>(chargedProjectiles);
    }

    /**
     * Builds an ItemStack from the given specifications
     *
     * @return the ItemStack result
     */
    @NMS
    @NotNull
    public ItemStack buildSpec() {
        ItemStack item = new ItemStack(this.itemType, this.amount);
        final ItemMeta meta; // defined after NMS
        final PineappleNMS nms = PineappleLib.getNmsProvider();

        // Display Data
        if (name != null) { // must be done first
            item = nms.setItemDisplayName(item, this.nameMutator != null ? this.nameMutator.apply(name) : this.defaultTextMutator.apply(name));
        }

        if (!lore.isEmpty()) { // must be done second
            item = nms.setItemLore(item, lore.stream().map(this.loreMutator != null ? this.loreMutator : this.defaultTextMutator).toList());
        }

        // must be set here due to NMS
        meta = item.getItemMeta();

        meta.addItemFlags(this.hideToolTips.toArray(ItemFlag[]::new));
        if (this.customModelData != INT_DATA_UNSET) {
            meta.setCustomModelData(this.customModelData);
        }
        // Display Data end

        // Attributes
        this.attributeModifiers.forEach(meta::addAttributeModifier);
        // Attributes end

        // Enchantments
        this.enchantments.forEach((e, i) -> meta.addEnchant(e, i, true));
        // Enchantments end

        // Durability
        if (meta instanceof Damageable damageable) {
            if (this.durability != INT_DATA_UNSET) {
                damageable.setDamage(this.durability);
            }
            damageable.setUnbreakable(this.unbreakable);
        }
        // Durability end

        // Potion Effects
        if (meta instanceof PotionMeta potionMeta) {
            this.effects.forEach(potionEffect -> potionMeta.addCustomEffect(potionEffect, false));
            if (this.potionColor != null) {
                potionMeta.setColor(this.potionColor);
            }
        }
        // Potion Effects end

        // Armor
        if (meta instanceof ArmorMeta armorMeta) {
            if (this.armorTrim != null) {
                armorMeta.setTrim(this.armorTrim);
            }
        }

        if (meta instanceof LeatherArmorMeta armorMeta) {
            if (this.armorColor != null) {
                armorMeta.setColor(this.armorColor);
            }
        }
        // Armor end

        // Armor Stands, Spawn Eggs, Item Frames and Paintings
        // SKIPPED
        // Armor Stands, Spawn Eggs, Item Frames and Paintings end

        // Book and Quills
        // SKIPPED
        // Book and Quills end

        // Written Books
        // SKIPPED
        // Written Books end

        // Knowledge Books
        if (meta instanceof EnchantmentStorageMeta emeta) {
            this.storedEnchantments.forEach((e, i) -> emeta.addStoredEnchant(e, i, true));
        }
        // Knowledge Books end

        // Buckets of Aquatic Mob
        if (meta instanceof TropicalFishBucketMeta bucketMeta) {
            if (this.fishPattern != null) {
                bucketMeta.setPattern(this.fishPattern);
            }

            if (this.fishPatternColor != null) {
                bucketMeta.setPatternColor(this.fishPatternColor);
            }

            if (this.fishBodyColor != null) {
                bucketMeta.setBodyColor(this.getFishBodyColor());
            }
        }
        // Buckets of Aquatic Mob end

        // Bundle
        // SKIPPED
        // Bundle end

        // Compasses
        if (meta instanceof CompassMeta compassMeta) {
            compassMeta.setLodestoneTracked(this.isLodestoneTracked);
            if (this.lodestoneLocation != null) {
                compassMeta.setLodestone(lodestoneLocation);
            }
        }
        // Compasses end

        // Crossbows
        if (meta instanceof CrossbowMeta crossbowMeta) {
            crossbowMeta.setChargedProjectiles(this.chargedProjectiles);
        }
        // Crossbows end

        // Firework Rockets
        // SKIPPED
        // Firework Rockets end

        // Firework Stars
        // SKIPPED
        // Firework Stars end

        // Goat Horns
        // SKIPPED
        // Goat Horns end

        // Maps
        // SKIPPED
        // Maps end

        // Player Heads
        // SKIPPED
        // Player Heads end

        // Suspicious Stew
        // SKIPPED
        // Suspicious Stew end

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a new ItemSpec from an existing ItemStack
     *
     * @param item the item to convert to an ItemSpec
     * @return the ItemSpec
     * @since 1.0.0-SNAPSHOT
     */
    public static ItemSpec fromStack(@NotNull final ItemStack item) {
        final ItemSpec spec = new ItemSpec(item.getType());
        spec.amount = item.getAmount();

        final ItemMeta meta = item.getItemMeta();

        // Display Data
        spec.name = meta.hasDisplayName() ? meta.getDisplayName() : null;
        spec.lore.addAll(meta.hasLore() ? meta.getLore() : new ArrayList<>());
        spec.hideToolTips.addAll(meta.getItemFlags());
        spec.customModelData = meta.hasCustomModelData() ? meta.getCustomModelData() : INT_DATA_UNSET;
        // Display Data end

        // Attributes
        spec.attributeModifiers.putAll(meta.hasAttributeModifiers() ? meta.getAttributeModifiers() : ArrayListMultimap.create());
        // Attributes end

        // Enchantments
        spec.enchantments.putAll(meta.hasEnchants() ? meta.getEnchants() : new HashMap<>());
        // Enchantments end

        // Durability
        final Damageable dmeta = (Damageable) meta;
        spec.durability = dmeta.hasDamage() ? dmeta.getDamage() : INT_DATA_UNSET;
        spec.unbreakable = dmeta.isUnbreakable();
        // Durability end

        // Potion Effects
        if (meta instanceof PotionMeta pmeta) {
            spec.effects.addAll(pmeta.hasCustomEffects() ? pmeta.getCustomEffects() : new ArrayList<>());
            spec.potionColor = pmeta.getColor();
        }
        // Potion Effects end

        // Armor
        if (meta instanceof ArmorMeta ameta) {
            spec.armorTrim = ameta.getTrim();
        }

        if (meta instanceof LeatherArmorMeta lmeta) {
            spec.armorColor = lmeta.getColor();
        }
        // Armor end

        // Armor Stands, Spawn Eggs, Item Frames and Paintings
        // SKIPPED
        // Armor Stands, Spawn Eggs, Item Frames and Paintings end

        // Book and Quills
        // SKIPPED
        // Book and Quills end

        // Written Books
        // SKIPPED
        // Written Books end

        // Knowledge Books
        if (meta instanceof EnchantmentStorageMeta emeta) {
            spec.storedEnchantments.putAll(emeta.hasStoredEnchants() ? emeta.getStoredEnchants() : new HashMap<>());
        }
        // Knowledge Books end

        // Buckets of Aquatic Mob
        if (meta instanceof TropicalFishBucketMeta tmeta) {
            spec.fishPattern = tmeta.getPattern();
            spec.fishPatternColor = tmeta.getPatternColor();
            spec.fishBodyColor = tmeta.getBodyColor();
        }
        // Buckets of Aquatic Mob end

        // Bundle
        // SKIPPED
        // Bundle end

        // Compasses
        if (meta instanceof CompassMeta compassMeta) {
            spec.isLodestoneTracked = compassMeta.isLodestoneTracked();
            spec.lodestoneLocation = compassMeta.getLodestone();
        }
        // Compasses end

        // Crossbows
        if (meta instanceof CrossbowMeta cmeta) {
            spec.chargedProjectiles.addAll(cmeta.hasChargedProjectiles() ? cmeta.getChargedProjectiles() : new ArrayList<>());
        }
        // Crossbows end

        // Firework Rockets
        // SKIPPED
        // Firework Rockets end

        // Firework Stars
        // SKIPPED
        // Firework Stars end

        // Goat Horns
        // SKIPPED
        // Goat Horns end

        // Maps
        // SKIPPED
        // Maps end

        // Player Heads
        // SKIPPED
        // Player Heads end

        // Suspicious Stew
        // SKIPPED
        // Suspicious Stew end
        return spec;
    }
}
