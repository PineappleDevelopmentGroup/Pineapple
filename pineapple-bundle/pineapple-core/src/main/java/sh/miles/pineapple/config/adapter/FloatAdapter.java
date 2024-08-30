package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.config.adapter.base.TypeAdapter;

class FloatAdapter<T> implements TypeAdapter<T, Float> {

    private final Class<T> clazz;

    public FloatAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getSavedType() {
        return clazz;
    }

    @Override
    public Class<Float> getRuntimeType() {
        return Float.class;
    }

    @Override
    public T write(Float value, T existing, boolean replace) {
        if (existing != null && !replace) {
            return null;
        }

        return (T) value;
    }

    @Override
    public Float read(T saved) {
        return (Float) saved;
    }
}
