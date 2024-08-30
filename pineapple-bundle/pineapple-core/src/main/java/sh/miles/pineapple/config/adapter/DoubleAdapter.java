package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.config.adapter.base.TypeAdapter;

class DoubleAdapter<T> implements TypeAdapter<T, Double> {

    private final Class<T> clazz;

    public DoubleAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getSavedType() {
        return clazz;
    }

    @Override
    public Class<Double> getRuntimeType() {
        return Double.class;
    }

    @Override
    public T write(Double value, T existing, boolean replace) {
        if (existing != null && !replace) {
            return null;
        }

        return (T) value;
    }

    @Override
    public Double read(T saved) {
        return (Double) saved;
    }
}
