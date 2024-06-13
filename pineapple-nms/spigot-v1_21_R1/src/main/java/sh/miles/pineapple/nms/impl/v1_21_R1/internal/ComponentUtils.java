package sh.miles.pineapple.nms.impl.v1_21_R1.internal;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftChatMessage;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public final class ComponentUtils {

    private static final Gson gson;

    static {
        final Field gsonField;
        try {
            gsonField = ComponentSerializer.class.getDeclaredField("gson");
            gsonField.setAccessible(true);

            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle gsonHandle = lookup.unreflectGetter(gsonField);
            gson = (Gson) gsonHandle.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Translates the base component into a minecraft component
     *
     * @param components the component to translate
     * @return the minecraft component
     */
    public static Component toMinecraftChat(@NotNull final BaseComponent components) {
        return CraftChatMessage.fromJSON(ComponentSerializer.toString(components));
    }

    /**
     * Translates the given base component into a json string
     *
     * @param component the component to translate
     * @return the string
     */
    public static String toJsonString(@NotNull final BaseComponent component) {
        return ComponentSerializer.toString(component);
    }

    /**
     * Translates the NMS component into a minecraft component
     *
     * @param component the component to translate
     * @return the bungee component
     */
    public static BaseComponent toBungeeChat(@NotNull final Component component) {
        return deserializeBaseComponent(CraftChatMessage.toJSON(component));
    }

    /**
     * Translates the json string into a bungee component
     *
     * @param json the json to translate
     * @return th bungee component
     */
    public static BaseComponent toBungeeChat(@NotNull final String json) {
        return deserializeBaseComponent(json);
    }

    /**
     * Deserializes a base component into a single component instead of a legacy array
     *
     * @param json the json to turn into a BaseComponent
     * @return the BaseComponent
     */
    private static BaseComponent deserializeBaseComponent(final String json) {
        return gson.fromJson(JsonParser.parseString(json), BaseComponent.class);
    }

}
