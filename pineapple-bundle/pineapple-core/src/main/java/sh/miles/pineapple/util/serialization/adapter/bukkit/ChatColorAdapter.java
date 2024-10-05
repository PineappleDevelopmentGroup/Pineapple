package sh.miles.pineapple.util.serialization.adapter.bukkit;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class ChatColorAdapter implements SerializedAdapter<ChatColor> {

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final ChatColor obj, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        return SerializedElement.primitive("#" + Integer.toHexString(obj.getColor().getRGB()).substring(2));
    }

    @NotNull
    @Override
    public ChatColor deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        if (element.isPrimitive()) {
            return ChatColor.of(element.getAsPrimitive().getAsString());
        }
        throw new SerializedAdaptationException("ChatColor's can only be adapted from primitive strings");
    }

    @Override
    public Class<?> getKey() {
        return ChatColor.class;
    }
}
