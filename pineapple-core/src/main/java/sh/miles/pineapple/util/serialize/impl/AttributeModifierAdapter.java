package sh.miles.pineapple.util.serialize.impl;

import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.util.HashMap;
import java.util.Map;

class AttributeModifierAdapter implements GenericSerializer<AttributeModifier> {

    public static final String NAME_KEY = "name";
    public static final String AMOUNT_KEY = "amount";
    public static final String OPERATION_KEY = "operation";

    @NotNull
    @Override
    public Map<String, Object> serialize(final AttributeModifier target) {
        final Map<String, Object> map = new HashMap<>();
        map.put(NAME_KEY, target.getName());
        map.put(AMOUNT_KEY, target.getAmount());
        map.put(OPERATION_KEY, target.getOperation().name());
        return map;
    }

    @NotNull
    @Override
    public AttributeModifier deserialize(@NotNull final Map<String, Object> map) throws FieldNotFoundException {
        final String name = (String) map.get(NAME_KEY);
        if (name == null) {
            throw new FieldNotFoundException(NAME_KEY, AttributeModifierAdapter.class);
        }
        final Double amount = (Double) map.get(AMOUNT_KEY);
        if (amount == null) {
            throw new FieldNotFoundException(AMOUNT_KEY, AttributeModifierAdapter.class);
        }
        final AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf((String) map.get(OPERATION_KEY));
        return new AttributeModifier(name, amount, operation);
    }

    @NotNull
    @Override
    public Class<AttributeModifier> getTypeClass() {
        return AttributeModifier.class;
    }
}
