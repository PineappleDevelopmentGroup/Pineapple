package sh.miles.pineapple.util.serialization.adapter;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.function.Option;
import sh.miles.pineapple.function.Option.Some;
import sh.miles.pineapple.item.ItemSpec;
import sh.miles.pineapple.util.serialization.SerializedArray;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sh.miles.pineapple.util.serialization.SerializedElement.array;
import static sh.miles.pineapple.util.serialization.SerializedElement.object;

class ItemSpecAdapter implements SerializedAdapter<ItemSpec> {

    // Required
    static final String ITEM_TYPE = "item_type";
    static final String AMOUNT = "amount";
    // Required end

    // Display Data
    static final String NAME = "name";
    static final String LORE = "lore";
    static final String FLAGS = "flags";
    static final String CUSTOM_MODEL_DATA = "custom_model_data";
    // Display Data end

    // Attributes
    static final String ATTRIBUTES = "attributes";
    // Attributes end

    // Enchantments
    static final String ENCHANTMENTS = "enchantments";
    // Enchantments end

    // Durability
    static final String DURABILITY = "durability";
    static final String UNBREAKABLE = "unbreakable";
    // Durability end

    // Potion Effects
    static final String POTION_EFFECTS = "potion_effects";
    // Potion Effects end

    // Armor
    static final String ARMOR_TRIM = "armor_trim";
    static final String ARMOR_COLOR = "armor_color";
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
    static final String STORED_ENCHANTMENTS = "stored_enchantments";
    // Knowledge Books end

    // Buckets of Aquatic Mob
    static final String FISH_PATTERN = "fish_pattern";
    static final String FISH_PATTERN_COLOR = "fish_pattern_color";
    static final String FISH_BODY_COLOR = "fish_body_color";
    // Buckets of Aquatic Mob end

    // Bundle
    // SKIPPED
    // Bundle end

    // Compasses
    static final String LODESTONE_TRACKED = "lodestone_tracked";
    static final String LODESTONE_LOCATION = "lodestone_location";
    // Compasses end

    // Crossbows
    static final String CHARGED_PROJECTILES = "charged_projectiles";
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

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final ItemSpec spec, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject item = object();

        // Required
        item.add(ITEM_TYPE, spec.getItemType().toString().toLowerCase());
        item.add(AMOUNT, spec.getAmount());
        // Required end

        // Display Data
        if (spec.getName() != null) {
            item.add(NAME, spec.getName());
        }

        if (!spec.getLore().isEmpty()) {
            final var array = array(spec.getLore().size());
            for (final String s : spec.getLore()) {
                array.add(s);
            }
            item.add(LORE, array);
        }

        if (!spec.getHideToolTips().isEmpty()) {
            final var array = array(spec.getHideToolTips().size());
            for (final ItemFlag hideToolTip : spec.getHideToolTips()) {
                array.add(hideToolTip.toString());
            }
            item.add(FLAGS, array);
        }

        if (spec.getCustomModelData() != ItemSpec.INT_DATA_UNSET) {
            item.add(CUSTOM_MODEL_DATA, spec.getCustomModelData());
        }
        // Display Data end

        // Attributes
        final Multimap<Attribute, AttributeModifier> attributes = spec.getAttributeModifiers();
        if (!attributes.isEmpty()) {
            final var attributeObject = object();
            for (final Attribute attribute : attributes.keySet()) {
                final var modifiersArray = array();
                for (final AttributeModifier attributeModifier : attributes.get(attribute)) {
                    modifiersArray.add(context.serialize(attributeModifier, AttributeModifier.class));
                }
                attributeObject.add(attribute.name(), modifiersArray);
            }
            item.add(ATTRIBUTES, attributeObject);
        }
        // Attributes end

        // Enchantments
        if (!spec.getEnchantments().isEmpty()) {
            final var enchantmentObject = object();
            for (final Map.Entry<Enchantment, Integer> entry : spec.getEnchantments().entrySet()) {
                enchantmentObject.add(entry.getKey().getKey().toString(), entry.getValue());
            }
            item.add(ENCHANTMENTS, enchantmentObject);
        }
        // Enchantments end

        // Durability
        if (spec.getDurability() != ItemSpec.INT_DATA_UNSET) {
            item.add(DURABILITY, spec.getDurability());
        }

        if (spec.isUnbreakable()) {
            item.add(UNBREAKABLE, spec.isUnbreakable());
        }
        // Durability end

        // Potion Effects
        if (!spec.getEffects().isEmpty()) {
            final var array = array();
            spec.getEffects().forEach(e -> array.add(context.serialize(e)));
            item.add(POTION_EFFECTS, array);
        }
        // Potion Effects end

        // Armor
        if (spec.getArmorTrim() != null) {
            item.add(ARMOR_TRIM, context.serialize(spec.getArmorTrim()));
        }

        if (spec.getArmorColor() != null) {
            item.add(ARMOR_COLOR, context.serialize(spec.getArmorColor()));
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
        if (!spec.getStoredEnchantments().isEmpty()) {
            final var enchantmentObject = object();
            for (final Map.Entry<Enchantment, Integer> entry : spec.getEnchantments().entrySet()) {
                enchantmentObject.add(entry.getKey().getKey().toString(), entry.getValue());
            }
            item.add(ENCHANTMENTS, enchantmentObject);
        }
        // Knowledge Books end

        // Buckets of Aquatic Mob
        if (spec.getFishPattern() != null) {
            item.add(FISH_PATTERN, spec.getFishPattern().name());
        }

        if (spec.getFishPatternColor() != null) {
            item.add(FISH_PATTERN_COLOR, spec.getFishPatternColor().name());
        }

        if (spec.getFishBodyColor() != null) {
            item.add(FISH_BODY_COLOR, spec.getFishBodyColor().name());
        }
        // Buckets of Aquatic Mob end

        // Bundle
        // SKIPPED
        // Bundle end

        // Compasses
        if (spec.isLodestoneTracked()) {
            item.add(LODESTONE_TRACKED, spec.isLodestoneTracked());
        }

        if (spec.getLodestoneLocation() != null) {
            item.add(LODESTONE_LOCATION, context.serialize(spec.getLodestoneLocation()));
        }
        // Compasses end

        // Crossbows
        if (!spec.getChargedProjectiles().isEmpty()) {
            final var array = array();
            spec.getChargedProjectiles().forEach(e -> array.add(context.serialize(ItemSpec.fromStack(e))));
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

        return item;
    }

    @NotNull
    @Override
    public ItemSpec deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject object = element.getAsObject();

        // Required
        final Material itemType = Material.matchMaterial(object.getPrimitive(ITEM_TYPE).orThrow().getAsString());
        final int amount = (object.getPrimitive(AMOUNT) instanceof Some<SerializedPrimitive> primitive) ? primitive.some().getAsInt() : 1;
        final ItemSpec spec = new ItemSpec(itemType);
        spec.setAmount(amount);
        // Required End

        // Display Data
        final String name = object.getPrimitive(NAME) instanceof Some<SerializedPrimitive> primitive ? primitive.some().getAsString() : null;

        if (name != null) {
            spec.setName(name);
        }

        final Option<SerializedArray> array = object.getArray(LORE);
        if (array instanceof Some<SerializedArray> serializedArray) {
            final List<String> lore = new ArrayList<>();
            for (final SerializedElement arrayItem : serializedArray.some()) {
                lore.add(arrayItem.getAsPrimitive().getAsString());
            }
            if (!lore.isEmpty()) {
                spec.setLore(lore);
            }
        }

        final Option<SerializedArray> flagsArray = object.getArray(FLAGS);
        if (flagsArray instanceof Some<SerializedArray> serializedArray) {
            final List<ItemFlag> flags = new ArrayList<>();
            for (final SerializedElement arrayItem : serializedArray.some()) {
                flags.add(ItemFlag.valueOf(arrayItem.getAsPrimitive().getAsString().toUpperCase()));
            }
            if (!flags.isEmpty()) {
                spec.setHideToolTips(flags);
            }
        }

        final int customModelData = object.getPrimitive(CUSTOM_MODEL_DATA) instanceof Some<SerializedPrimitive> primitive ? primitive.some().getAsInt() : ItemSpec.INT_DATA_UNSET;
        if (customModelData != ItemSpec.INT_DATA_UNSET) {
            spec.setCustomModelData(customModelData);
        }
        // Display Data end

        // Attributes
        final Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
        final Option<SerializedObject> attributeObjectOption = object.getObject(ATTRIBUTES);
        if (attributeObjectOption instanceof Some<SerializedObject> serializedObject) {
            final var attributeObject = serializedObject.some();
            for (final Map.Entry<String, SerializedElement> entry : attributeObject.entrySet()) {
                List<AttributeModifier> modifiers = new ArrayList<>();
                final var modifiersArray = entry.getValue().getAsArray();
                for (final SerializedElement modifierElement : modifiersArray) {
                    modifiers.add(context.deserialize(modifierElement, AttributeModifier.class));
                }
                attributes.putAll(Attribute.valueOf(entry.getKey().toUpperCase()), modifiers);
            }
        }

        if (!attributes.isEmpty()) {
            spec.setAttributeModifiers(attributes);
        }
        // Attributes end

        // Enchantments
        final Map<Enchantment, Integer> enchantments = new HashMap<>();
        final Option<SerializedObject> enchantmentObjectOption = object.getObject(ENCHANTMENTS);
        if (enchantmentObjectOption instanceof Some<SerializedObject> serializedObject) {
            final var enchantmentObject = serializedObject.some();
            for (final Map.Entry<String, SerializedElement> entry : enchantmentObject.entrySet()) {
                enchantments.put(Registry.ENCHANTMENT.get(NamespacedKey.fromString(entry.getKey())), entry.getValue().getAsPrimitive().getAsInt());
            }
        }

        if (!enchantments.isEmpty()) {
            spec.setEnchantments(enchantments);
        }
        // Enchantments end

        // Durability
        final int durability = object.getPrimitive(DURABILITY) instanceof Some<SerializedPrimitive> primitive ? primitive.some().getAsInt() : ItemSpec.INT_DATA_UNSET;
        if (durability != ItemSpec.INT_DATA_UNSET) {
            spec.setDurability(durability);
        }

        final boolean unbreakable = object.getPrimitive(UNBREAKABLE) instanceof Some<SerializedPrimitive> primitive && primitive.some().getAsBoolean();
        if (unbreakable) {
            spec.setUnbreakable(true);
        }
        // Durability end

        // Potion Effects
        final List<PotionEffect> effects = new ArrayList<>();
        if (object.getArray(POTION_EFFECTS) instanceof Some<SerializedArray> potionArraySome) {
            final var potionArray = potionArraySome.some();
            for (final SerializedElement potionElement : potionArray) {
                effects.add(context.deserialize(potionElement, PotionEffect.class));
            }
        }

        if (!effects.isEmpty()) {
            spec.setPotionEffects(effects);
        }
        // Potion Effects end

        // Armor
        if (object.getObject(ARMOR_TRIM) instanceof Some<SerializedObject> armorTrimSome) {
            spec.setArmorTrim(context.deserialize(armorTrimSome.some(), ArmorTrim.class));
        }

        if (object.getPrimitive(ARMOR_COLOR) instanceof Some<SerializedPrimitive> armorColorSome) {
            spec.setArmorColor(context.deserialize(armorColorSome.some(), Color.class));
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
        final Map<Enchantment, Integer> storedEnchantments = new HashMap<>();
        if (object.getObject(STORED_ENCHANTMENTS) instanceof Some<SerializedObject> someEnchantments) {
            final var enchantmentObject = someEnchantments.some();
            for (final Map.Entry<String, SerializedElement> entry : enchantmentObject.entrySet()) {
                storedEnchantments.put(Registry.ENCHANTMENT.get(NamespacedKey.fromString(entry.getKey())), entry.getValue().getAsPrimitive().getAsInt());
            }
        }

        if (!storedEnchantments.isEmpty()) {
            spec.setStoredEnchantments(storedEnchantments);
        }
        // Knowledge Books end

        // Buckets of Aquatic Mob
        if (object.getPrimitive(FISH_PATTERN) instanceof Some<SerializedPrimitive> somePattern) {
            spec.setFishPattern(TropicalFish.Pattern.valueOf(somePattern.some().getAsString()));
        }

        if (object.getPrimitive(FISH_PATTERN_COLOR) instanceof Some<SerializedPrimitive> somePatternColor) {
            spec.setFishPatternColor(DyeColor.valueOf(somePatternColor.some().getAsString().toUpperCase()));
        }

        if (object.getPrimitive(FISH_BODY_COLOR) instanceof Some<SerializedPrimitive> someBodyColor) {
            spec.setFishBodyColor(DyeColor.valueOf(someBodyColor.some().getAsString().toUpperCase()));
        }
        // Buckets of Aquatic Mob end

        // Bundle
        // SKIPPED
        // Bundle end

        // Compasses
        final boolean lodestoneTracked = object.getPrimitive(LODESTONE_TRACKED) instanceof Some<SerializedPrimitive> someLodestoneTracked && someLodestoneTracked.some().getAsBoolean();
        if (lodestoneTracked) {
            spec.setLodestoneTracked(true);
        }

        if (object.getObject(LODESTONE_LOCATION) instanceof Some<SerializedObject> someLodestoneLocation) {
            spec.setLodestoneLocation(context.deserialize(someLodestoneLocation.some(), Location.class));
        }
        // Compasses end

        // Crossbows
        if (object.getArray(CHARGED_PROJECTILES) instanceof Some<SerializedArray> someProjectiles) {
            final List<ItemStack> stacks = new ArrayList<>();
            for (final SerializedElement serializedElement : someProjectiles.some()) {
                stacks.add(context.deserialize(serializedElement, ItemSpec.class).buildSpec());
            }

            if (!stacks.isEmpty()) {
                spec.setChargedProjectiles(stacks);
            }
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

    @Override
    public Class<?> getKey() {
        return ItemSpec.class;
    }
}
