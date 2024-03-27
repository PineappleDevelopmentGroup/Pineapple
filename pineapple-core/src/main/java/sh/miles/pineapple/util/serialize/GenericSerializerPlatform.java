package sh.miles.pineapple.util.serialize;

/**
 * Platform implementation for GenericSerializers
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface GenericSerializerPlatform {

    /**
     * Registers the given GenericSerializer to the desired platform
     *
     * @param serializer the generic serializer
     * @since 1.0.0-SNAPSHOT
     */
    void registerToPlatform(GenericSerializer<?> serializer);

}
