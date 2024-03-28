package sh.miles.pineapple.util.serialize.base;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ComplexGenericSerializer<O> extends GenericSerializer<Map<String, Object>, O> {

    TypeToken<Map<String, Object>> TOKEN = new TypeToken<Map<String, Object>>() {
    };

    @NotNull
    @Override
    default TypeToken<Map<String, Object>> getStoredType() {
        return TOKEN;
    }
}
