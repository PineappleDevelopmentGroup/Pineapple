package sh.miles.pineapple.config;

import sh.miles.pineapple.config.annotation.Comment;
import sh.miles.pineapple.config.annotation.ConfigPath;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigField {

    private final Field field;
    private List<String> comments;
    private final Class<?> runtimeClass;
    private String path;

    /**
     * Create a config field wrapper from {@link Field}
     *
     * @param field the field
     */
    public ConfigField(Field field) {
        this.field = field;
        this.runtimeClass = this.field.getType();

        setup();
    }

    private void setup() {
        this.path = this.field.getAnnotation(ConfigPath.class).value();

        for (Comment comment : this.field.getAnnotationsByType(Comment.class)) {
            if (this.comments == null) {
                this.comments = new ArrayList<>();
            }
            this.comments.add(comment.value());
        }
    }

    public Field getField() {
        return field;
    }

    public List<String> getComments() {
        return comments;
    }

    public String getPath() {
        return path;
    }

    public Class<?> getRuntimeClass() {
        return runtimeClass;
    }
}
