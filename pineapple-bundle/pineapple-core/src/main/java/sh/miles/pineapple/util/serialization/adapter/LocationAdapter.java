package sh.miles.pineapple.util.serialization.adapter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.function.Option;
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext;
import sh.miles.pineapple.util.serialization.SerializedElement;
import sh.miles.pineapple.util.serialization.SerializedObject;
import sh.miles.pineapple.util.serialization.SerializedPrimitive;
import sh.miles.pineapple.util.serialization.SerializedSerializeContext;
import sh.miles.pineapple.util.serialization.exception.SerializedAdaptationException;

class LocationAdapter implements SerializedAdapter<Location> {

    static final String X = "x";
    static final String Y = "y";
    static final String Z = "z";
    static final String PITCH = "pitch";
    static final String YAW = "yaw";
    static final String WORLD = "world";

    @NotNull
    @Override
    public SerializedElement serialize(@NotNull final Location location, @NotNull final SerializedSerializeContext context) throws SerializedAdaptationException {
        final SerializedObject object = SerializedElement.object();
        object.add(WORLD, location.getWorld().getName());
        object.add(X, location.getX());
        object.add(Y, location.getY());
        object.add(Z, location.getZ());
        if (location.getPitch() != 0) {
            object.add(PITCH, location.getPitch());
        }
        if (location.getYaw() != 0) {
            object.add(YAW, location.getYaw());
        }
        return object;
    }

    @NotNull
    @Override
    public Location deserialize(@NotNull final SerializedElement element, @NotNull final SerializedDeserializeContext context) throws SerializedAdaptationException {
        final SerializedObject object = element.getAsObject();
        final World world = Bukkit.getWorld(object.getPrimitive(WORLD).orThrow().getAsString());
        final double x = object.getPrimitive(X).orThrow().getAsDouble();
        final double y = object.getPrimitive(Y).orThrow().getAsDouble();
        final double z = object.getPrimitive(Z).orThrow().getAsDouble();
        final float pitch = (object.getPrimitive(PITCH) instanceof Option.Some<SerializedPrimitive> primitive) ? (float) primitive.some().getAsDouble() : 0.0f;
        final float yaw = (object.getPrimitive(YAW) instanceof Option.Some<SerializedPrimitive> primitive) ? (float) primitive.some().getAsDouble() : 0.0f;
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public Class<?> getKey() {
        return Location.class;
    }
}
