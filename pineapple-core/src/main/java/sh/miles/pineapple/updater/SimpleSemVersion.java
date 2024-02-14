package sh.miles.pineapple.updater;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Objects;

/**
 * SimpleSemVer represents a semantic version
 *
 * @since 1.0.0-SNAPSHOT
 */
public class SimpleSemVersion {

    public static final byte RELEASE = 4;
    public static final byte HOTFIX = 3;
    public static final byte SNAPSHOT = 2;
    public static final byte BETA = 1;
    public static final byte ALPHA = 0;
    private static final BiMap<Byte, String> MODIFIER_LABEL = ImmutableBiMap.of(
            (byte) 0b100, "release",
            (byte) 0b11, "hotfix",
            (byte) 0b10, "snapshot",
            (byte) 0b01, "beta",
            (byte) 0b00, "alpha"
    );

    private final int major;
    private final int minor;
    private final int patch;
    private byte modifier;

    private SimpleSemVersion(int major, int minor, int patch, byte modifier) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.modifier = modifier;
    }

    private SimpleSemVersion(int major, int minor, int patch) {
        this(major, minor, patch, RELEASE);
    }

    /**
     * Parses a string representation of a SimpleSemVersion into a SimpleSemVersion object.
     *
     * @param version the string representation of the SimpleSemVersion
     * @return the parsed SimpleSemVersion object
     * @since 1.0.0-SNAPSHOT
     */
    public static SimpleSemVersion fromString(String version) {
        String[] split = version.split("-");
        String[] numParts = split[0].split("\\.");
        String suffix = split.length > 1 ? split[1] : "";

        if (numParts.length != 3) {
            throw new IllegalArgumentException("String must be in the format major.minor.patch");
        }

        SimpleSemVersion semVer;

        try {
            semVer = new SimpleSemVersion(Integer.parseInt(numParts[0]), Integer.parseInt(numParts[1]),
                    Integer.parseInt(numParts[2]));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("String must be in the format major.minor.patch-SUFFIX (optional suffix)");
        }

        if (!suffix.isEmpty()) {
            semVer.modifier = MODIFIER_LABEL.inverse().get(suffix.toLowerCase());
        }

        return semVer;
    }

    /**
     * Check if the current SimpleSemVersion object is newer than the provided SimpleSemVersion object.
     *
     * @param other The SimpleSemVersion object to compare against.
     * @return Returns true if the current version is newer, false otherwise.
     * @since 1.0.0-SNAPSHOT
     */
    public boolean isNewerThan(SimpleSemVersion other) {
        // 1.0.0-BETA is lower than 1.0.0-SNAPSHOT
        if (this.modifier < other.modifier) {
            return false;
        } else if (this.modifier > other.modifier) {
            return true;
        }

        return !equals(other) && (this.major > other.major) || (this.major == other.major && this.minor > other.minor) || (this.major == other.major && this.minor == other.minor && this.patch > other.patch) || (this.major == other.major && this.minor == other.minor && this.patch == other.patch && this.modifier >= other.modifier);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final SimpleSemVersion that)) {
            return false;
        }
        return major == that.major && minor == that.minor && patch == that.patch && modifier == that.modifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, modifier);
    }

    @Override
    public String toString() {
        return this.major + "." + this.minor + "." + this.patch + MODIFIER_LABEL.get(this.modifier);
    }
}
