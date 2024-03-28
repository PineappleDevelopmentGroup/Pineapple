package sh.miles.pineapple.util.serialize.base;

import com.google.gson.reflect.TypeToken;

import java.util.Map;

public interface ComplexGenericSerializer<O> extends GenericSerializer<Map<String, Object>, O> {

    TypeToken<Map<String, Object>> TOKEN = new TypeToken<Map<String, Object>>() {
    };

    @Override
    default TypeToken<Map<String, Object>> getStoredType() {
        return TOKEN;
    }
}
