package sh.miles.pineapple.config;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.config.annotation.ConfigEntry;
import sh.miles.pineapple.config.annotation.PostLoad;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class ConfigReloadable<T> {
    protected final File file;
    protected final List<ConfigField> fields = new ArrayList<>();
    private final Method postLoadMethod;

    protected ConfigReloadable(File file, Class<T> clazz) {
        this.file = file;
        this.postLoadMethod = findPostLoadMethod(clazz);

        cacheFields(clazz);
    }

    /**
     * Loads the config
     *
     * @return instance for chaining
     *
     * @since 1.0.0
     */
    public ConfigReloadable<T> load() {
        load(true);

        return this;
    }

    /**
     * Loads the config
     *
     * @param includeStatic should static fields be loaded
     * @return instance for chaining
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public ConfigReloadable<T> load(boolean includeStatic) {
        if (!prepareFile()) {
            // File error
            return this;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);

        for (ConfigField field : this.fields) {
            if (field.isStatic() && !includeStatic) {
                continue;
            }

            String path = field.getPath();

            if (!config.isSet(path)) {
                PineappleLib.getLogger().log(Level.WARNING, "No configuration entry found for {0}", path);
                continue;
            }

            ConfigType<?> type = ConfigType.get(field.getField());
            TypeAdapter<Object, Object> adapter = (TypeAdapter<Object, Object>) PineappleLib.getConfigurationManager().getAdapter(type);
            if (adapter == null) {
                PineappleLib.getLogger().log(Level.WARNING, "No configuration adapter found for {0}", type.getClass());
                continue;
            }

            Class<Object> saved = adapter.getSavedType();
            Object savedValue = getSavedValue(config, path, saved);
            if (!saved.isInstance(savedValue)) {
                PineappleLib.getLogger().log(Level.WARNING, "Wrong saved type for {0}, {1} != {2}", new Object[] { path, savedValue.getClass(), saved });
                continue;
            }

            Object readValue = adapter.read(saved.cast(savedValue));

            if (readValue == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Failed to read value for {0} of type {1}", new Object[] { path, type.getType() });
                continue;
            }

            try {
                setField(field, readValue);
            } catch (ReflectiveOperationException ex) {
                PineappleLib.getLogger().log(Level.WARNING, "Failed to read value for {0} of type {1}", new Object[] { path, type.getType() });
                ex.printStackTrace();
            }
        }

        if (this.postLoadMethod != null) {
            this.postLoad(this.postLoadMethod);
        }
        return this;
    }

    /**
     * Saves the defaults of the config
     *
     * @return instance for chaining
     *
     * @since 1.0.0
     */
    public ConfigReloadable<T> saveDefaults() {
        save(false);

        return this;
    }

    /**
     * Saves the config, replaces current values
     *
     * @return instance for chaining
     *
     * @since 1.0.0
     */
    public ConfigReloadable<T> save() {
        save(true);

        return this;
    }

    /**
     * Saves the config
     *
     * @param replace replace current values
     * @return instance for chaining
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public ConfigReloadable<T> save(boolean replace) {
        if (!prepareFile()) {
            // File error
            return this;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);

        for (ConfigField field : this.fields) {
            String path = field.getPath();

            if (!path.isEmpty()) {
                config.setComments(path, field.getComments());
            }

            ConfigType<?> type = ConfigType.get(field.getField());
            TypeAdapter<Object, Object> adapter = (TypeAdapter<Object, Object>) PineappleLib.getConfigurationManager().getAdapter(type);
            if (adapter == null) {
                PineappleLib.getLogger().log(Level.WARNING, "No configuration adapter found for {0}", type.getType());
                continue;
            }

            Object writeValue = getField(field);
            if (writeValue == null) {
                PineappleLib.getLogger().log(Level.WARNING, "null");
                continue;
            }

            Class<Object> saved = adapter.getSavedType();
            Class<Object> runtime = adapter.getRuntimeType();
            if (!runtime.isInstance(writeValue)) {
                PineappleLib.getLogger().log(Level.WARNING, "Wrong runtime type for {0}, {1} != {2}", new Object[] { path, writeValue.getClass(), runtime });
                continue;
            }

            Object existing = getSavedValue(config, path, saved);
            if (existing != null && !saved.isInstance(existing)) {
                PineappleLib.getLogger().log(Level.WARNING, "Wrong saved type for {0}, {1} != {2}", new Object[] { path, existing.getClass(), saved });
                existing = null;
            }

            Object value = adapter.write(runtime.cast(writeValue), saved.cast(existing), replace);
            if (value != null) {
                config.set(path, value);
                config.setComments(path, field.getComments());
            }
        }

        try {
            config.save(this.file);
        } catch (IOException ex) {
            PineappleLib.getLogger().log(Level.WARNING, "Failed to save configuration file {0}", this.file.getName());
            ex.printStackTrace();
        }
        return this;
    }

    protected abstract void setField(ConfigField field, Object value) throws ReflectiveOperationException;

    protected abstract Object getField(ConfigField field);

    protected abstract void postLoad(Method postLoadMethod);

    private Object getSavedValue(YamlConfiguration config, String path, Class<Object> expectedType) {
        Object saved = config.get(path);

        if (saved instanceof MemorySection section && Map.class.isAssignableFrom(expectedType)) {
            return section.getValues(false);
        }
        return saved;
    }

    private boolean prepareFile() {
        try {
            if (!this.file.getParentFile().exists() && !this.file.getParentFile().mkdirs()) {
                PineappleLib.getLogger().log(Level.WARNING, "Error creating file {0}", this.file.getName());
                return false;
            }

            if (!this.file.exists()) {
                return this.file.createNewFile();
            }

            return true;
        } catch (IOException ex) {
            PineappleLib.getLogger().log(Level.WARNING, "Error creating file {0}", this.file.getName());
            return false;
        }
    }

    private void cacheFields(Class<? super T> clazz) {
        int index = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigEntry.class)) {
                continue;
            }

            this.fields.add(index++, new ConfigField(field));
        }

        Class<? super T> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            cacheFields(superClass);
        }
    }

    private Method findPostLoadMethod(Class<? super T> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostLoad.class)) {
                return method;
            }
        }

        Class<? super T> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return findPostLoadMethod(superClass);
        }

        return null;
    }
}
