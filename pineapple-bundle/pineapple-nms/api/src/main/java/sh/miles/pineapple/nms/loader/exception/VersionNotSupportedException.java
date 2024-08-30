package sh.miles.pineapple.nms.loader.exception;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.nms.loader.MinecraftVersion;

public class VersionNotSupportedException extends RuntimeException {

    public VersionNotSupportedException(@NotNull MinecraftVersion version) {
        super("The version %s is not currently supported seek support if you believe this is a mistake".formatted(version.getInternalName()));
    }

}
