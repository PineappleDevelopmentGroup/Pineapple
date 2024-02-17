package sh.miles.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.pineapple.function.ThrowingSupplier;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Provides a handful of utilities for reflection
 *
 * @since 1.0.0-SNAPSHOT
 */
public final class ReflectionUtils {

    private static final MethodHandles.Lookup lookup;

    static {
        lookup = MethodHandles.lookup();
    }

    /**
     * Utility class
     */
    private ReflectionUtils() {
    }

    /**
     * Creates a new instance of a class from the provided class and constructor parameters
     *
     * @param clazz  the class to make a new instance of
     * @param params the parameters to pass in to the constructor
     * @param <T>    the class T
     * @return a new instance of the class with the parameters or null if errors occur
     * @since 1.0.0-SNAPSHOT
     */
    public static <T> T newInstance(@NotNull final Class<T> clazz, @NotNull final Object[] params) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(params);

        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor(classesFromParameters(params));
            constructor.setAccessible(true);
            final T instance = constructor.newInstance(params);
            constructor.setAccessible(false);
            return instance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Creates a new instance of a class from the provided class path and constructor paramaters
     *
     * @param classPath the classPath
     * @param params    the parameters
     * @param <T>       the Type
     * @return the intended instance
     * @since 1.0.0-SNAPSHOT
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(@NotNull final String classPath, @NotNull final Object[] params) {
        Objects.requireNonNull(classPath);
        Objects.requireNonNull(params);

        try {
            final Class<T> clazz = (Class<T>) Class.forName(classPath);
            return newInstance(clazz, params);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Attempts to retrieve a fields value from the specified info
     *
     * @param instance  the instance of the object to get the field from
     * @param fieldName the name of the field
     * @param fieldType the type of the field
     * @param <T>       The Field Type
     * @return the value of the field or null if errors occur or the field is null
     * @since 1.0.0-SNAPSHOT
     */
    public static <T> T getField(@NotNull final Object instance, @NotNull final String fieldName, @NotNull final Class<T> fieldType) {
        return getField(instance.getClass(), instance, fieldName, fieldType);
    }

    /**
     * Attempts to retrieve a static field's value from the specified info
     *
     * @param parentClass the parentClass of the field
     * @param fieldName   the fields name
     * @param fieldType   the type of the field
     * @param <T>         the field type
     * @return the value of the field or null if errors occur or the field is null
     * @since 1.0.0-SNAPSHOT
     */
    public static <T> T getField(@NotNull final Class<?> parentClass, @NotNull final String fieldName, @NotNull final Class<T> fieldType) {
        return getField(parentClass, null, fieldName, fieldType);
    }

    /**
     * Attempts to retrieve a fields value from the specified info
     *
     * @param parentClass the parentClass which contains the field
     * @param instance    the instance of the class if the field isn't static
     * @param fieldName   the name of the field to get
     * @param fieldType   the type of the field
     * @param <T>         the Field Type
     * @return the value of the field or null if errors occur or the field is null
     * @since 1.0.0-SNAPSHOT
     */
    private static <T> T getField(@NotNull final Class<?> parentClass, @Nullable final Object instance, @NotNull final String fieldName, @NotNull final Class<T> fieldType) {
        Objects.requireNonNull(parentClass);
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(fieldType);

        try {
            final Field field = parentClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            final T value = fieldType.cast(field.get(instance));
            field.setAccessible(false);
            return value;
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            return null;
        }
    }

    /**
     * Creates an array of classes from an array of object parameters
     *
     * @param parameters the objects to convert to parameters
     * @return an array of parameters
     * @since 1.0.0-SNAPSHOT
     */
    @Contract("_ -> new")
    public static Class<?>[] classesFromParameters(@NotNull final Object[] parameters) {
        Objects.requireNonNull(parameters);

        final Class<?>[] clazzes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            clazzes[i] = parameters[i].getClass();
        }

        return clazzes;
    }

    /**
     * Uses the MethodHandle API to get a constructor from the provided class with the provided parameters
     *
     * @param clazz      the class to get the constructor from
     * @param parameters the parameters
     * @return the method handle
     * @since 1.0.0-SNAPSHOT
     */
    public static MethodHandle getConstructor(Class<?> clazz, Class<?>[] parameters) {
        try {
            final Constructor<?> constructor = clazz.getDeclaredConstructor(parameters);
            return accessAndReturn(constructor, () -> lookup.unreflectConstructor(constructor));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Uses the MethodHandle API to get a method from the provided class with the provided name and params
     *
     * @param clazz      the class to get the method from
     * @param methodName the method name
     * @param parameters the parameters of the method
     * @return the method handle
     * @since 1.0.0-SNAPSHOT
     */
    public static MethodHandle getMethod(Class<?> clazz, String methodName, Class<?>[] parameters) {
        try {
            final Method method = clazz.getDeclaredMethod(methodName, parameters);
            return accessAndReturn(method, () -> lookup.unreflect(method));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * Uses the MethodHandle API to get the MethodHandle attached the provided field getter on the given class
     *
     * @param clazz     the class to get the field from
     * @param fieldName the field name
     * @return the method handle associated with the class and field
     * @since 1.0.0-SNAPSHOT
     */
    public static MethodHandle getFieldAsGetter(Class<?> clazz, String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            return accessAndReturn(field, () -> lookup.unreflectGetter(field));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Uses the MethodHandle API to get the MethodHandle attached to the provided field setter on a given class
     *
     * @param clazz     the class to get the field from
     * @param fieldName the field name
     * @return the method handle associated with the class and field
     * @since 1.0.0-SNAPSHOT
     */
    public static MethodHandle getFieldAsSetter(Class<?> clazz, String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            return accessAndReturn(field, () -> lookup.unreflectSetter(field));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Attempts to safely invoke a method handle if a failure occurs this method throws a runtime exception
     *
     * @param handle     the handle
     * @param parameters the parameters
     * @return the object created from the invocation
     * @since 1.0.0-SNAPSHOT
     */
    public static Object safeInvoke(MethodHandle handle, Object... parameters) {
        try {
            return handle.invokeWithArguments(parameters);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the given accessible object accessible and then returns the result of hte provided function
     *
     * @param accessibleObject the accessible object
     * @param function         the function
     * @param <T>              the type T
     * @param <R>              the type R
     * @return the return type of the provided function
     * @since 1.0.0-SNAPSHOT
     */
    private static <T extends AccessibleObject, R> R accessAndReturn(T accessibleObject, ThrowingSupplier<R> function) throws Exception {
        accessibleObject.setAccessible(true);
        final R result = function.get();
        accessibleObject.setAccessible(false);
        return result;
    }


}
