package sh.miles.pineapple.json.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.function.Either;
import sh.miles.pineapple.function.Option;
import sh.miles.pineapple.json.JsonAdapter;
import sh.miles.pineapple.nms.loader.NMSLoader;
import sh.miles.pineapple.PineappleLib;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemStackAdapter implementation for JsonAdapters
 * <p>
 * Note if you need to reflect this class you are doing something wrong.
 *
 * @since 1.0.0-SNAPSHOT
 */
@ApiStatus.Internal
class ItemStackAdapter implements JsonAdapter<ItemStack> {

    public static final String ITEM_TYPE = "item_type";
    public static final String NAME = "name";
    public static final String LORE = "lore";
    public static final String ENCHANTMENT = "enchantment";
    public static final String HIDE_TOOL_TIP = "hide_tool_tip";
    public static final String MODEL_DATA = "model_data";

    @Override
    public ItemStack deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        JsonObject parent = json.getAsJsonObject();
        Material itemType = parseItemType(parent);
        ItemStack item = new ItemStack(itemType);
        final Option<Either<BaseComponent, String>> possibleName = parseName(parent);

        if (possibleName instanceof Option.Some<Either<BaseComponent, String>> some) {
            final var either = some.some();
            if (either.isLeft()) {
                item = PineappleLib.getNmsProvider().setItemDisplayName(item, either.leftOrThrow());
            } else {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(either.rightOrThrow());
                item.setItemMeta(meta);
            }
        }

        final Option<Either<List<BaseComponent>, List<String>>> possibleLore = parseLore(parent);
        if (possibleLore instanceof Option.Some<Either<List<BaseComponent>, List<String>>> some) {
            final var either = some.some();
            if (either.isLeft()) {
                item = PineappleLib.getNmsProvider().setItemLore(item, either.leftOrThrow());
            } else {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(either.rightOrThrow());
                item.setItemMeta(meta);
            }
        }

        parseEnchantment(parent).forEach(item::addEnchantment);
        final ItemMeta meta = item.getItemMeta();
        parseItemFlag(parent).forEach(meta::addItemFlags);

        if (parent.has(MODEL_DATA)) {
            meta.setCustomModelData(parent.get(MODEL_DATA).getAsInt());
        }

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public JsonElement serialize(final ItemStack src, final Type typeOfSrc, final JsonSerializationContext context) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public Class<ItemStack> getAdapterType() {
        return ItemStack.class;
    }

    @NotNull
    private static Material parseItemType(JsonObject parent) {
        final JsonElement temp = parent.get(ITEM_TYPE);
        if (temp == null) {
            throw new IllegalStateException("Unable to adapt material");
        }

        Material itemType = Material.matchMaterial(temp.getAsString());
        if (itemType == null) {
            throw new IllegalStateException("Unable to adapt material");
        }
        return itemType;
    }

    @Nullable
    private static Option<Either<BaseComponent, String>> parseName(JsonObject parent) {
        final JsonElement temp = parent.get(NAME);
        if (temp == null) {
            return null;
        }
        final String name = temp.getAsString();

        if (name == null) {
            return Option.none();
        }

        if (NMSLoader.INSTANCE.isActive()) {
            return Option.some(Either.left(PineappleChat.parse(name)));
        }

        return Option.some(Either.right(PineappleChat.parseLegacy(name)));
    }

    @NotNull
    private static Option<Either<List<BaseComponent>, List<String>>> parseLore(JsonObject parent) {
        final JsonArray loreArray = parent.getAsJsonArray(LORE);
        if (loreArray == null) {
            return Option.none();
        }

        if (NMSLoader.INSTANCE.isActive()) {
            final List<BaseComponent> lore = new ArrayList<>(loreArray.size());
            for (final JsonElement jsonElement : loreArray) {
                lore.add(PineappleChat.parse(jsonElement.getAsString()));
            }
            return Option.some(Either.left(lore));
        }

        final List<String> lore = new ArrayList<>(loreArray.size());
        for (final JsonElement jsonElement : loreArray) {
            lore.add(PineappleChat.parseLegacy(jsonElement.getAsString()));
        }

        return Option.some(Either.right(lore));
    }

    @NotNull
    private static Map<Enchantment, Short> parseEnchantment(JsonObject parent) {
        final JsonObject enchantObject = parent.getAsJsonObject(ENCHANTMENT);
        if (enchantObject == null) {
            return new HashMap<>();
        }
        final Map<Enchantment, Short> map = new HashMap<>();
        for (final Map.Entry<String, JsonElement> entry : enchantObject.entrySet()) {
            final String key = entry.getKey();
            final short level = entry.getValue().getAsShort();
            final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key));
            if (enchantment == null) {
                throw new IllegalStateException("Invalid Enchantment key");
            }
            map.put(enchantment, level);
        }
        return map;
    }


    @NotNull
    private static List<ItemFlag> parseItemFlag(JsonObject parent) {
        final JsonArray tipArray = parent.getAsJsonArray(HIDE_TOOL_TIP);
        if (tipArray == null) {
            return new ArrayList<>();
        }
        final List<ItemFlag> tip = new ArrayList<>();
        for (final JsonElement element : tipArray) {
            final String raw = element.getAsString();
            try {
                tip.add(ItemFlag.valueOf(raw.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(e);
            }
        }
        return tip;
    }

    @Override
    public boolean isHierarchy() {
        return true;
    }
}
