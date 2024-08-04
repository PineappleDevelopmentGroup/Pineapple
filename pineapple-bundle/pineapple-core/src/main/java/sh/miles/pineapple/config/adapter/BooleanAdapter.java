package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.config.adapter.base.TypeAdapter;

class BooleanAdapter<T> implements TypeAdapter<T, T> {

    private final Class<T> clazz;

    public BooleanAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getSavedType() {
        return clazz;
    }

    @Override
    public Class<T> getRuntimeType() {
        return clazz;
    }

    @Override
    public T write(T value, T existing, boolean replace) {
        if (existing != null && !replace) {
            return null;
        }

        return value;
    }

    @Override
    public T read(T saved) {
        return (T) saved;
    }
}
