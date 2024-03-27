package sh.miles.pineapple.util.serialize.impl;

import com.google.common.collect.Multimaps;
import com.google.gson.reflect.TypeToken;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.item.ItemSpec;
import sh.miles.pineapple.util.serialize.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ItemSpecAdapter implements GenericSerializer<ItemSpec> {

    public static final String ITEM_TYPE_KEY = "item_type";
    public static final String AMOUNT_KEY = "amount";

    // Display Data
    public static final String NAME_KEY = "name";
    public static final String LORE_KEY = "lore";
    public static final String HIDE_TOOLTIPS_KEY = "hide_tooltips";
    public static final String CUSTOM_MODEL_DATA_KEY = "custom_model_data";
    // Display Data end

    // Attributes
    public static final String ATTRIBUTES_KEY = "attributes";
    // Attributes end

    // Enchantments
    public static final String ENCHANTMENTS_KEY = "enchantments";
    // Enchantments end

    // Durability
    public static final String DURABILITY_KEY = "durability";
    public static final String UNBREAKABLE_KEY = "unbreakable";
    // Durability end

    // Potion Effects
    public static final String POTION_EFFECTS_KEY = "potion_effects";
    public static final String POTION_COLOR_KEY = "potion_color";
    // Potion Effects end

    // Armor
    public static final String ARMOR_TRIM_KEY = "armor_trim";
    public static final String ARMOR_COLOR_KEY = "armor_color";
    // Armor end

    // Knowledge Books
    public static final String STORED_ENCHANTMENTS_KEY = "stored_enchantments";
    // Knowledge Books end

    // Buckets of Aquatic Mob
    public static final String FISH_PATTERN_KEY = "fish_pattern";
    public static final String FISH_PATTERN_COLOR_KEY = "fish_pattern_color";
    public static final String FISH_BODY_COLOR_KEY = "fish_body_color";
    // Buckets of Aquatic Mob end

    // Compasses
    public static final String LODESTONE_TRACKED_KEY = "lodestone_tracked";
    public static final String LODESTONE_LOCATION_KEY = "lodestone_location";
    // Compasses end

    // Crossbows
    public static final String CHARGED_PROJECTILES_KEY = "charged_projectiles";
    // Crossbows end

    @NotNull
    @Override
    public Map<String, Object> serialize(final ItemSpec target) {
        return null;
    }

    @NotNull
    @Override
    public ItemSpec deserialize(@NotNull final Map<String, Object> map) throws FieldNotFoundException {
        final Material itemType = Material.matchMaterial((String) map.get(ITEM_TYPE_KEY));
        if (itemType == null) {
            throw new FieldNotFoundException(ITEM_TYPE_KEY, ItemSpecAdapter.class);
        }
        final ItemSpec spec = new ItemSpec(itemType);

        final int amount = (int) map.getOrDefault(AMOUNT_KEY, 1);
        spec.setAmount(amount);

        // Display Data
        final String name = (String) map.get(NAME_KEY);
        if (name != null) {
            spec.setName(name);
        }

        final List<String> lore = (List<String>) map.get(LORE_KEY);
        if (lore != null) {
            spec.setLore(lore);
        }

        final List<String> hideToolTips = (List<String>) map.get(HIDE_TOOLTIPS_KEY);
        if (hideToolTips != null) {
            spec.setHideToolTips(hideToolTips.stream().map(ItemFlag::valueOf).toList());
        }

        final Integer customModelData = (Integer) map.get(CUSTOM_MODEL_DATA_KEY);
        if (customModelData != null) {
            spec.setCustomModelData(customModelData);
        }
        // Display Data end

        // Attributes
        final Map<String, Object> attributeMap = (Map<String, Object>) map.get(ATTRIBUTES_KEY);
        if (attributeMap != null) {
            Map<Attribute, Collection<AttributeModifier>> multimap = attributeMap.entrySet().stream().collect(Collectors.toMap((entry) -> Attribute.valueOf(entry.getKey()), (entry) -> {
                final String key = entry.getKey();
                final List<Map<String, Object>> value = (List<Map<String, Object>>) entry.getValue();
                final List<AttributeModifier> modifiers = new ArrayList<>();
                value.forEach((attributeModifierMap) -> {
                    modifiers.add(GenericSerializable.INSTANCE.deserialize(attributeModifierMap, AttributeModifier.class));
                });
                return modifiers;
            }));
            spec.setAttributeModifiers(Multimaps.newListMultimap(multimap, ArrayList::new));
        }
        // Attributes end

        // Enchantments
        final Map<String, Object> enchantments = (Map<String, Object>) map.get(ENCHANTMENTS_KEY);
        if (enchantments != null) {
            spec.setEnchantments((Map<Enchantment, Integer>) GenericSerializable.INSTANCE.deserialize(enchantments, new TypeToken<Map<Enchantment, Integer>>() {
            }.getRawType()));
        }
        // Enchantments end

        // Durability
        final Integer durability = (Integer) map.get(DURABILITY_KEY);
        if (durability != null) {
            spec.setDurability(durability);
        }

        final Boolean unbreakable = (Boolean) map.get(UNBREAKABLE_KEY);
        if (unbreakable != null) {
            spec.setUnbreakable(unbreakable);
        }
        // Durability end

        // Potion Effects
        final List<Map<String, Object>> effects = (List<Map<String, Object>>) map.get(POTION_EFFECTS_KEY);
        if (effects != null) {
            spec.setPotionEffects(effects.stream().map(effectMap -> GenericSerializable.INSTANCE.deserialize(effectMap, PotionEffect.class)).toList());
        }

        final String potionColor = (String) map.get(POTION_COLOR_KEY);
        if (potionColor != null) {
            spec.setPotionColor(org.bukkit.Color.fromRGB(Color.decode(potionColor).getRGB()));
        }
        // Potion Effects end

        // Armor
        final Map<String, Object> armorTrim = (Map<String, Object>) map.get(ARMOR_TRIM_KEY);
        if (armorTrim != null) {
            spec.setArmorTrim(GenericSerializable.INSTANCE.deserialize(armorTrim, ArmorTrim.class));
        }

        final String armorColor = (String) map.get(ARMOR_COLOR_KEY);
        if (armorColor != null) {
            spec.setArmorColor(org.bukkit.Color.fromRGB(Color.decode(armorColor).getRGB()));
        }
        // Armor end

        // Knowledge Books
        final Map<String, Object> storedEnchantments = (Map<String, Object>) map.get(STORED_ENCHANTMENTS_KEY);
        if (enchantments != null) {
            spec.setStoredEnchantments((Map<Enchantment, Integer>) GenericSerializable.INSTANCE.deserialize(enchantments, new TypeToken<Map<Enchantment, Integer>>() {
            }.getRawType()));
        }
        // Knowledge Books end

        // Buckets of Aquatic Mob
        final String fishPattern = (String) map.get(FISH_PATTERN_KEY);
        if (fishPattern != null) {
            spec.setFishPattern(TropicalFish.Pattern.valueOf(fishPattern));
        }

        final String fishPatternColor = (String) map.get(FISH_PATTERN_COLOR_KEY);
        if (fishPatternColor != null) {
            spec.setFishPatternColor(DyeColor.getByColor(org.bukkit.Color.fromRGB(Color.decode(fishPatternColor).getRGB())));
        }

        final String fishBodyColor = (String) map.get(FISH_BODY_COLOR_KEY);
        if (fishBodyColor != null) {
            spec.setFishBodyColor(DyeColor.getByColor(org.bukkit.Color.fromRGB(Color.decode(fishBodyColor).getRGB())));
        }
        // Buckets of Aquatic Mob end

        // Compass
        final Boolean lodestoneTracked = (Boolean) map.get(LODESTONE_TRACKED_KEY);
        if (lodestoneTracked != null) {
            spec.setLodestoneTracked(lodestoneTracked);
        }

        final Map<String, Object> lodestoneLocation = (Map<String, Object>) map.get(LODESTONE_LOCATION_KEY);
        if (lodestoneLocation != null) {
            spec.setLodestoneLocation(GenericSerializable.INSTANCE.deserialize(lodestoneLocation, Location.class));
        }
        // Compass end

        // Crossbows
        final List<Map<String, Object>> chargedProjectiles = (List<Map<String, Object>>) map.get(CHARGED_PROJECTILES_KEY);
        if (chargedProjectiles != null) {
            spec.setChargedProjectiles(chargedProjectiles.stream().map((cp) -> GenericSerializable.INSTANCE.deserialize(cp, ItemSpec.class).buildSpec()).toList());
        }
        // Crossbows end

        return spec;
    }

    @NotNull
    @Override
    public Class<ItemSpec> getTypeClass() {
        return ItemSpec.class;
    }
}
