package sh.miles.pineapple.util.serialization.adapter.bukkit;

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
    static final String NAME = "name";
    static final String AMOUNT = "amount";
    static final String OPERATION = "operation";
    static final String EQUIPMENT_SLOT_GROUP = "equipment_slot_goup";

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final AttributeModifier attributeModifier, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = object();
        parent.add(KEY, context.serialize(attributeModifier.getKey()));
        parent.add(NAME, attributeModifier.getName());
        parent.add(AMOUNT, attributeModifier.getAmount());
        parent.add(OPERATION, attributeModifier.getOperation().name());
        parent.add(EQUIPMENT_SLOT_GROUP, attributeModifier.getSlotGroup().toString());
        return parent;
    }

    @NotNull
    @Override
    public AttributeModifier deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (!element.isObject()) {
            throw new SerializedAdaptationException("The given element for AttributeModifier's must be an object");
        }
        final SerializedObject object = element.getAsObject();
        final NamespacedKey key = context.deserialize(object.getPrimitive(KEY).orThrow(), NamespacedKey.class);
        final String name = object.getPrimitive(NAME).orThrow().getAsString();
        final double amount = object.getPrimitive(AMOUNT).orThrow().getAsDouble();
        final AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(object.getPrimitive(OPERATION).orThrow().getAsString());
        final EquipmentSlotGroup slotGroup = EquipmentSlotGroup.getByName(object.getPrimitive(EQUIPMENT_SLOT_GROUP).orThrow().getAsString());
        return new AttributeModifier(key, amount, operation, slotGroup);
    }

    @Override
    public Class<?> getKey() {
        return AttributeModifierAdapter.class;
    }
}
