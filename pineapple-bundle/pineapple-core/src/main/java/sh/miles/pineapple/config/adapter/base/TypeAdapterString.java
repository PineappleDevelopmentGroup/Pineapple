package sh.miles.pineapple.config.adapter.base;

/**
 * @param <R> Runtime/Code type
 */
public interface TypeAdapterString<R> extends TypeAdapter<String, R> {

    @Override
    default Class<String> getSavedType() {
        return String.class;
    }

    @Override
    default String write(R value, String existing, boolean replace) {
        if (existing != null && !replace) {
            return null;
        }

        return toString(value);
    }

    @Override
    default R read(String saved) {
        return fromString(saved);
    }

    R fromString(String value);

    String toString(R value);
}
