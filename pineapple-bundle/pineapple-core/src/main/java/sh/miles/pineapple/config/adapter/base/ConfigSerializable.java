package sh.miles.pineapple.config.adapter.base;

/**
 * A static deserialize(type) method is needed for this to function
 *
 * @param <S> the Saved type
 */
public interface ConfigSerializable<S> {
    S serialize(S existing, boolean replace);
}
