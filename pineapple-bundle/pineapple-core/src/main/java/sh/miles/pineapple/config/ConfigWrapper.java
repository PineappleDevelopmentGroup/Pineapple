package sh.miles.pineapple.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.config.adapter.base.TypeAdapter;
import sh.miles.pineapple.config.annotation.ConfigPath;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Deprecated(forRemoval = true)
public class ConfigWrapper {

    private final List<ConfigField> fields = new ArrayList<>();
    private final File file;

    /**
     * Constructs a new ConfigWrapper
     *
     * @param file the file
     * @param clazz the config class
     */
    public ConfigWrapper(File file, Class<?> clazz) {
        PineappleLib.getLogger().warning("ConfigWrapper in use, please note ConfigWrapper is now deprecated and should be replaced by Configuration");
        this.file = file;

        cacheFields(clazz);

        if (this.fields.isEmpty()) {
            PineappleLib.getLogger().log(Level.WARNING, "Creating config {1} with no fields", this.file.getName());
        }
    }

    /**
     * Save a config to file
     *
     * @param replace should replace
     * @return instance for chaining
     */
    @SuppressWarnings("unchecked")
    public ConfigWrapper save(boolean replace) {
        if (!setupFile()) {
            PineappleLib.getLogger().log(Level.WARNING, "Unable to create config {0}", this.file.getName());
            return this;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);

        ConfigManager configManager = PineappleLib.getConfigurationManager();
        for (ConfigField field : this.fields) {
            field.setVisible();
            String path = field.getPath();

            boolean mapOnly = field.getRuntimeClass() == Map.class && path.isEmpty() && this.fields.size() == 1;

            if (!path.isEmpty()) {
                config.setComments(path, field.getComments());
            }

            TypeAdapter<Object, Object> adapter = (TypeAdapter<Object, Object>) configManager.getTypeAdapter(ConfigType.get(field.getField()));
            if (adapter == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Unable to find type adapter for field: {0} with path: {1} in {2}", objArr(field.getField().getName(), field.getPath(), this.file.getName()));
                field.setHidden();
                continue;
            }


            Object toSave = getValue(field.getField());
            if (toSave == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Found null value while trying to save for field name: {0}, path: {1} in config {2}", objArr(field.getField().getName(), path, this.file.getName()));
            }

            Object existing = config.get(path);
            if (existing != null) {
                if (!replace) {
                    continue;
                }
            }

            if (existing instanceof ConfigurationSection section) {
                existing = section.getValues(false);
            }

            Object value = adapter.write(toSave, existing, replace);
            if (value == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Found null value from TypeAdapter: {0} while trying to save config {1}", objArr(adapter.getClass().getTypeName(), this.file.getName()));
                field.setHidden();
                continue;
            }

            if (mapOnly) {
                Map<String, Object> saving = (Map<String, Object>) value;
                for (Map.Entry<String, Object> entry : saving.entrySet()) {
                    config.set(entry.getKey(), entry.getValue());
                }
                field.setHidden();
                continue;
            }


            config.set(path, value);
            field.setHidden();
        }

        try {
            config.save(this.file);
        } catch (IOException ex) {
            PineappleLib.getLogger().log(Level.WARNING, ex, () -> "Unable to save config %s".formatted(this.file.getName()));
        }
        return this;
    }

    /**
     * Loads a config from file
     *
     * @return instance for chaining
     */
    @SuppressWarnings("unchecked")
    public ConfigWrapper load() {
        if (!setupFile()) {
            PineappleLib.getLogger().log(Level.WARNING, "Unable to load file for {0}", this.file.getName());
            return this;
        }

        ConfigManager configManager = PineappleLib.getConfigurationManager();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);

        for (ConfigField field : this.fields) {
            field.setVisible();
            String path = field.getPath();

            boolean mapOnly = field.getRuntimeClass() == Map.class && path.isEmpty() && this.fields.size() == 1;
            TypeAdapter<Object, Object> typeAdapter = (TypeAdapter<Object, Object>) configManager.getTypeAdapter(ConfigType.get(field.getField()));

            if (typeAdapter == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Unable to find type adapter for field: {0} with path: {1} in {2}", objArr(field.getField().getName(), field.getPath(), this.file.getName()));
                field.setHidden();
                continue;
            }

            if (mapOnly) {
                Map<String, Object> fromConfig = new HashMap<>();
                for (String key : config.getKeys(false)) {
                    fromConfig.put(key, config.get(key));
                }
                Map<?, ?> deserialized = new HashMap<>((Map<?, ?>) typeAdapter.read(fromConfig));
                setField(field.getField(), deserialized);
                field.setHidden();
                return this;
            }

            Object configValue = config.get(path);
            if(configValue == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Config value retrieved is null for {0}", path);
                field.setHidden();
                continue;
            }

            Object unserialized;
            if (configValue instanceof ConfigurationSection section) {
                unserialized = typeAdapter.read(section.getValues(false));
            } else {
                unserialized = typeAdapter.read(configValue);
            }

            if (unserialized == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Deserialized value for path: {0}, is null from TypeAdapter {1}", objArr(path, typeAdapter.getClass().getTypeName()));
                field.setHidden();
                return this;
            }

            setField(field.getField(), unserialized);
            field.setHidden();
        }
        return this;
    }

    private boolean setupFile() {
        try {
            if (this.file.isDirectory()) {
                return false;
            }

            if (!this.file.getParentFile().exists() && !this.file.getParentFile().mkdirs()) {
                return false;
            }

            if (!this.file.exists()) {
                return this.file.createNewFile();
            }

            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void cacheFields(Class<?> clazz) {
        int i = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigPath.class)) {
                continue;
            }
            this.fields.add(i++, new ConfigField(field));
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            cacheFields(superClass);
        }
    }

    private Object getValue(Field field) {
        try {
            return field.get(null);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Unable to access field: " + field.getName(), ex);
        }
    }

    private void setField(Field field, Object value) {
        try {
            field.set(null, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Unable to set field value: " + field.getName(), ex);
        }
    }

    private Object[] objArr(Object... objects) {
        return objects;
    }
}
