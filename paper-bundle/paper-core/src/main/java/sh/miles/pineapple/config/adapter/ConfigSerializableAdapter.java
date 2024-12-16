package sh.miles.pineapple.config.adapter;

import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.config.ConfigType;
import sh.miles.pineapple.config.adapter.base.ConfigSerializable;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Type;

class ConfigSerializableAdapter<S, R extends ConfigSerializable<S>> implements TypeAdapter<S, R> {

    private final Class<R> clazz;
    private Class<S> savedClass;
    private final MethodHandle deserializeMethod;

    /**
     * Creates a new ConfigSerializableAdapter
     *
     * @param type the type to adapt
     */
    @SuppressWarnings("unchecked")
    public ConfigSerializableAdapter(ConfigType<?> type) {
        this.clazz = (Class<R>) type.getType();

        for (Type interfaceType : this.clazz.getGenericInterfaces()) {
            ConfigType<?> configType = ConfigType.create(interfaceType);
            if (configType.getType() == ConfigSerializable.class) {
                this.savedClass = (Class<S>) configType.getComponentTypes().get(0).getType();
                break;
            }
        }
        this.deserializeMethod = ReflectionUtils.getMethod(this.clazz, "deserialize", new Class[]{this.savedClass});
    }

    @Override
    public Class<S> getSavedType() {
        return this.savedClass;
    }

    @Override
    public Class<R> getRuntimeType() {
        return this.clazz;
    }

    @Override
    public R read(S value) {
        return (R) ReflectionUtils.safeInvoke(this.deserializeMethod, value);
    }

    @Override
    public S write(R value, S existing, boolean replace) {
        return value.serialize(existing, replace);
    }
}
