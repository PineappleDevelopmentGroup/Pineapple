package sh.miles.pineapple.util.serialization.adapter.bukkit;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import static sh.miles.pineapple.util.serialization.SerializedElement.object;

class AttributeModifierAdapter implements SerializedAdapter<AttributeModifier> {

    static final String KEY = "key";
    static final String AMOUNT = "amount";
    static final String OPERATION = "operation";
    static final String EQUIPMENT_SLOT = "equipment_slot";

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final AttributeModifier attributeModifier, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = object();
        parent.add(KEY, attributeModifier.key().toString());
        parent.add(AMOUNT, attributeModifier.getAmount());
        parent.add(OPERATION, attributeModifier.getOperation().name());
        parent.add(EQUIPMENT_SLOT, attributeModifier.getSlotGroup().toString());
        return parent;
    }

    @NotNull
    @Override
    public AttributeModifier deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (!element.isObject()) {
            throw new SerializedAdaptationException("The given element for AttributeModifier's must be an object");
        }
        final SerializedObject object = element.getAsObject();
        final NamespacedKey key = NamespacedKey.fromString(object.getPrimitive(KEY).orThrow().getAsString());
        final double amount = object.getPrimitive(AMOUNT).orThrow().getAsDouble();
        final AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(object.getPrimitive(OPERATION).orThrow().getAsString());
        final EquipmentSlotGroup slot = EquipmentSlotGroup.getByName(object.getPrimitive(EQUIPMENT_SLOT).orThrow().getAsString());
        return new AttributeModifier(key, amount, operation, slot);
    }

    @Override
    public Class<?> getKey() {
        return AttributeModifierAdapter.class;
    }
}
