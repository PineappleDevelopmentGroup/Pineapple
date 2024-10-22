package sh.miles.pineapple.nms.loader;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.ReflectionUtils;
import sh.miles.pineapple.nms.annotations.NMS;
import sh.miles.pineapple.nms.api.PineappleNMS;
import sh.miles.pineapple.nms.loader.exception.VersionNotSupportedException;

import java.util.logging.Logger;

/**
 * Provides Management for NMS Classes
 */
@NMS
public final class NMSLoader {

    public static final NMSLoader INSTANCE = new NMSLoader();
    private static final String PATH = "sh.miles.pineapple.nms.impl.%s.%s";

    private PineappleNMS handler;
    private boolean active = false;

    private NMSLoader() {
    }

    /**
     * Activates PineappleNMS and supplies a loader to the NMSLoader
     *
     * @param logger the logger to log with
     * @throws IllegalStateException        if PineappleNMS is already active
     * @throws VersionNotSupportedException given the server is on is not supported
     * @since 1.0.0-SNAPSHOT
     */
    @NMS
    public void activate(@NotNull final Logger logger) throws IllegalStateException, VersionNotSupportedException {
        Preconditions.checkState(!this.active, "You can not active PineappleNMS while it is active");
        try {
            if (MinecraftVersion.CURRENT_DEPRECATED) {
                logger.warning("You are using Minecraft Version %s which PineappleLib is now deprecating and will be removing support for in 1.21.3.".formatted(MinecraftVersion.CURRENT.getName()));
                logger.warning("We recommend you update your server's version as newer versions of the game contain bug and security fixes.");
            }
            var clazz = Class.forName(PATH.formatted(MinecraftVersion.CURRENT.getProtocolVersion(), PineappleNMS.class.getSimpleName() + "Impl"));
            this.handler = (PineappleNMS) ReflectionUtils.safeInvoke(ReflectionUtils.getConstructor(clazz, new Class[0]));
        } catch (ClassNotFoundException e) {
            throw new VersionNotSupportedException(MinecraftVersion.CURRENT);
        }
        this.active = true;
    }

    /**
     * Disables PineappleNMS and fails all other interactions
     */
    public void disable() {
        this.handler = null;
        this.active = false;
    }

    /**
     * Verifies whether or not NMS active
     *
     * @throws IllegalStateException thrown if NMS is not active
     */
    public void verifyNMS() throws IllegalArgumentException {
        if (!this.active) {
            throw new IllegalStateException("This method can not be accessed when NMS is disabled");
        }
    }

    /**
     * Gets whether or not the NMSLoader is active.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Gets the Pineapple
     * <p>
     * The pineapple(Ananas comosus) is a tropical plant with an edible fruit; it is the most economically significant
     * plant in the family Bromeliaceae.
     * <p>
     * The pineapple is indigenous to South America, where it has been cultivated for many centuries. The introduction
     * of the pineapple to Europe in the 17th century made it a significant cultural icon of luxury. Since the 1820s,
     * pineapple has been commercially grown in greenhouses and many tropical plantations.
     *
     * @return the pineapple
     * @since 1.0.0-SNAPSHOT
     */
    @NMS
    public PineappleNMS getPineapple() {
        return this.handler;
    }

}
