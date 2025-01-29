package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

@SuppressWarnings("rawtypes")
class EnumAdapter<R extends Enum> implements TypeAdapterString<R> {
    private final Class<R> clazz;

    @SuppressWarnings("unchecked")
    public EnumAdapter(Class<?> clazz) {
        this.clazz = (Class<R>) clazz;
    }

    @Override
    public Class<R> getRuntimeType() {
        return this.clazz;
    }

    @Override
    public String toString(R value) {
        return value.name();
    }

    @SuppressWarnings("unchecked")
    @Override
    public R fromString(String value) {
        return (R) Enum.valueOf(this.clazz, value);
    }
}
