package sh.miles.pineapple.config.type;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.config.ConfigField;
import sh.miles.pineapple.config.ConfigManager;
import sh.miles.pineapple.config.ConfigType;
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

public class Configuration {

    private final File file;
    private final Class<?> clazz;

    private final List<ConfigField> fields = new ArrayList<>();
    private Object instance;

    /**
     * Create a new configuration. Should only be used by ConfigManager
     *
     * @param file the configs file
     * @param configClass the class to retrieve fields from
     * @param instance the instance to retrieve values from if required
     */
    public Configuration(@NotNull File file, @NotNull Class<?> configClass, @Nullable Object instance) {
        this.file = file;
        this.clazz = configClass;
        this.instance = instance;

        setup();
    }

    private void setup() {
        cacheFields(clazz);

        // Handle any other setup
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

    /**
     * Load the config from config using paths from found fields
     *
     * @return instance for chaining
     */
    public Configuration load() {
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
            TypeAdapter<Object, Object> typeAdapter = (TypeAdapter<Object, Object>) configManager.getTypeAdapter(
                    ConfigType.get(field.getField()));

            if (typeAdapter == null) {
                PineappleLib.getLogger()
                        .log(Level.WARNING, "Unable to find type adapter for field: {0} with path: {1} in {2}",
                                objArr(field.getField().getName(), field.getPath(), this.file.getName())
                        );
                field.setHidden();
                continue;
            }

            if (mapOnly) {
                Map<String, Object> fromConfig = new HashMap<>();
                for (String key : config.getKeys(false)) {
                    fromConfig.put(key, config.get(key));
                }
                Map<?, ?> deserialized = new HashMap<>((Map<?, ?>) typeAdapter.read(fromConfig));
                this.setField(field.getField(), deserialized);
                field.setHidden();
                break;
            }

            Object configValue = config.get(path);
            if (configValue == null) {
                PineappleLib.getLogger().log(Level.WARNING, "Config value retrieved is null for {0}", path);
                field.setHidden();
                continue;
            }

            Object deserialized;
            if (configValue instanceof ConfigurationSection section) {
                deserialized = typeAdapter.read(section.getValues(false));
            } else {
                deserialized = typeAdapter.read(configValue);
            }

            if (deserialized == null) {
                PineappleLib.getLogger()
                        .log(Level.WARNING, "Deserialized value for path: {0}, is null from TypeAdapter {1}",
                                objArr(path, typeAdapter.getClass().getTypeName())
                        );
                field.setHidden();
                return this;
            }

            this.setField(field.getField(), deserialized);
            field.setHidden();
        }
        return this;
    }

    /**
     * Saves the config to its respective file
     *
     * @param replace Should the config replace existing values
     * @return instance for chaining
     */
    public Configuration save(boolean replace) {
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


            Object toSave = this.getValue(field.getField());
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
                break;
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

    private void setField(Field field, Object value) {
        FieldModifier.setField(field, value, instance);
    }

    private Object getValue(Field field) {
        return FieldModifier.getValue(field, instance);
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
            PineappleLib.getLogger().log(Level.WARNING, ex, () -> "SetupFile Error Details:");
            return false;
        }
    }

    private Object[] objArr(Object... objects) {
        return objects;
    }
}
