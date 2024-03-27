package sh.miles.pineapple.util.serialize.impl;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.item.ItemSpec;
import sh.miles.pineapple.util.serialize.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.util.Map;

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
        return null;
    }

    @NotNull
    @Override
    public Class<ItemSpec> getTypeClass() {
        return ItemSpec.class;
    }
}
