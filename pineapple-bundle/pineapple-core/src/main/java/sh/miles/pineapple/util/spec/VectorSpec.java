package sh.miles.pineapple.util.spec;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a simple vector specification
 *
 * @param x the x of the vector
 * @param y the y of the vector
 * @param z the z of the vector
 * @since 1.0.0-SNAPSHOT
 */
public record VectorSpec(double x, double y, double z) {

    /**
     * Modifies the given location with this vector specification
     *
     * @param location the location to modify
     * @return the modified location
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Location modify(@NotNull final Location location) {
        return location.clone().add(x, y, z);
    }

    /**
     * Modifies the given vector with this vector specification
     *
     * @param vector the vector to modify
     * @return the modified vector
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    public Vector modify(@NotNull final Vector vector) {
        return vector.clone().add(new Vector(x, y, z));
    }
}
