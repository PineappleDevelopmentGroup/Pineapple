package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

class PineappleComponentAdapter implements TypeAdapterString<PineappleComponent> {
    @Override
    public Class<PineappleComponent> getRuntimeType() {
        return PineappleComponent.class;
    }

    @Override
    public PineappleComponent fromString(String value) {
        return PineappleChat.component(value);
    }

    @Override
    public String toString(PineappleComponent value) {
        return value.getSource();
    }
}
