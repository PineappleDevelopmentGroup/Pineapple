package sh.miles.pineapple.nms.annotations;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a current NMS Feature is in a pull request that is likely to be merged. This means that the NMS method
 * may be phased out in the coming versions
 *
 * @since 1.0.0-SNAPSHOT
 */
@Documented
@ApiStatus.Internal
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PullRequested {
}
