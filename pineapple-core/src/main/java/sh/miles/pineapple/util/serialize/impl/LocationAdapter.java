package sh.miles.pineapple.util.serialize.impl;

import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.util.serialize.base.ComplexGenericSerializer;
import sh.miles.pineapple.util.serialize.base.GenericSerializer;
import sh.miles.pineapple.util.serialize.exception.FieldNotFoundException;

import java.util.HashMap;
import java.util.Map;

class LocationAdapter implements ComplexGenericSerializer<Location> {

    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";
    public static final String WORLD = "world";

    @NotNull
    @Override
    public Map<String, Object> serialize(final Location target) {
        final Map<String, Object> map = new HashMap<>();
        map.put(X, target.getX());
        map.put(Y, target.getY());
        map.put(Z, target.getZ());
        map.put(WORLD, target.getWorld().getName());
        return map;
    }

    @NotNull
    @Override
    public Location deserialize(@NotNull final Map<String, Object> map) throws FieldNotFoundException {
        final double x = (double) map.get(X);
        final double y = (double) map.get(Y);
        final double z = (double) map.get(Z);
        final World world = Bukkit.getWorld((String) map.get(WORLD));
        return new Location(world, x, y, z);
    }

    @NotNull
    @Override
    public TypeToken<Location> getComplexType() {
        return TypeToken.get(Location.class);
    }
}
