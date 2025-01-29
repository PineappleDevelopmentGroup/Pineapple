package sh.miles.pineapple.config.adapter;


import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

class LongAdapter<T> implements TypeAdapterString<T> {

    private final Class<T> clazz;

    public LongAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public Class<T> getRuntimeType() {
        return clazz;
    }

    @Override
    public T fromString(String value) {
        return (T) (Object) Long.parseLong(value);
    }

    @Override
    public String toString(T value) {
        return String.valueOf(value);
    }
}
