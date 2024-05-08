package sh.miles.pineapple.util.serialization.adapter;

import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

import static sh.miles.pineapple.util.serialization.SerializedElement.object;

class AttributeModifierAdapter implements SerializedAdapter<AttributeModifier> {

    static final String UUID = "uuid";
    static final String NAME = "name";
    static final String AMOUNT = "amount";
    static final String OPERATION = "operation";
    static final String EQUIPMENT_SLOT = "equipment_slot";

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final AttributeModifier attributeModifier, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject parent = object();
        parent.add(UUID, attributeModifier.getUniqueId().toString());
        parent.add(NAME, attributeModifier.getName());
        parent.add(AMOUNT, attributeModifier.getAmount());
        parent.add(OPERATION, attributeModifier.getOperation().name());
        parent.add(EQUIPMENT_SLOT, attributeModifier.getSlot().name());
        return parent;
    }

    @NotNull
    @Override
    public AttributeModifier deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (!element.isObject()) {
            throw new SerializedAdaptationException("The given element for AttributeModifier's must be an object");
        }
        final SerializedObject object = element.getAsObject();
        final java.util.UUID uuid = java.util.UUID.fromString(object.getPrimitive(UUID).orThrow().getAsString());
        final String name = object.getPrimitive(NAME).orThrow().getAsString();
        final double amount = object.getPrimitive(AMOUNT).orThrow().getAsDouble();
        final AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(object.getPrimitive(OPERATION).orThrow().getAsString());
        final EquipmentSlot slot = EquipmentSlot.valueOf(object.getPrimitive(EQUIPMENT_SLOT).orThrow().getAsString());
        return new AttributeModifier(uuid, name, amount, operation, slot);
    }

    @Override
    public Class<?> getKey() {
        return AttributeModifierAdapter.class;
    }
}
