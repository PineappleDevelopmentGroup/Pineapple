package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

class StringAdapter implements TypeAdapterString<String> {

    @Override
    public Class<String> getRuntimeType() {
        return String.class;
    }

    @Override
    public String fromString(String value) {
        return value;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}
