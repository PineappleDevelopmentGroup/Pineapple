package sh.miles.pineapple.util.spec;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

/**
 * Represents the basic sound Specification
 *
 * @param sound    the sound to make
 * @param category the sound category to play it on
 * @param volume   the volume of the sound
 * @param pitch    the pitch of the sound
 * @since 1.0.0-SNAPSHOT
 */
@NullMarked
public record SoundSpec(Sound sound, SoundCategory category, float volume, float pitch) {
    /**
     * Creates a SoundSpec
     *
     * @param sound  the sound to make
     * @param volume the volume of the sound
     * @param pitch  the pitch of the sound
     * @since 1.0.0-SNAPSHOT
     */
    public SoundSpec(Sound sound, float volume, float pitch) {
        this(sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Plays the sound specification at the given location
     *
     * @param location the location to play the sound at
     * @since 1.0.0-SNAPSHOT
     */
    public void play(final Location location) {
        location.getWorld().playSound(location, sound, category, volume, pitch);
    }

    /**
     * Plays the sound for the given player
     *
     * @param player the player to play the sound for
     * @since 1.0.0-SNAPSHOT
     */
    public void play(final Player player) {
        player.playSound(player, sound, category, volume, pitch);
    }
}
