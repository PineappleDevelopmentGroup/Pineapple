package sh.miles.pineapple.updater;

import org.junit.jupiter.api.Test;
import sh.miles.pineapple.SimpleSemVersion;
import sh.miles.pineapple.ReflectionUtils;

import java.lang.invoke.MethodHandle;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleSemVersionTest {

    private static final MethodHandle MAJOR = ReflectionUtils.getFieldAsGetter(SimpleSemVersion.class, "major");
    private static final MethodHandle MINOR = ReflectionUtils.getFieldAsGetter(SimpleSemVersion.class, "minor");
    private static final MethodHandle PATCH = ReflectionUtils.getFieldAsGetter(SimpleSemVersion.class, "patch");
    private static final MethodHandle MODIFIER = ReflectionUtils.getFieldAsGetter(SimpleSemVersion.class, "modifier");

    @Test
    public void test_No_Suffix() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3");
        assertEquals(1, ReflectionUtils.safeInvoke(MAJOR.bindTo(version)));
        assertEquals(2, ReflectionUtils.safeInvoke(MINOR.bindTo(version)));
        assertEquals(3, ReflectionUtils.safeInvoke(PATCH.bindTo(version)));
        assertEquals(SimpleSemVersion.RELEASE, ReflectionUtils.safeInvoke(MODIFIER.bindTo(version)));
    }

    @Test
    public void test_Alpha() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-alpha");
        assertEquals(SimpleSemVersion.ALPHA, ReflectionUtils.safeInvoke(MODIFIER.bindTo(version)));
    }

    @Test
    public void test_Beta() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-beta");
        assertEquals(SimpleSemVersion.BETA, ReflectionUtils.safeInvoke(MODIFIER.bindTo(version)));
    }

    @Test
    public void test_Snapshot() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-snapshot");
        assertEquals(SimpleSemVersion.SNAPSHOT, ReflectionUtils.safeInvoke(MODIFIER.bindTo(version)));
    }

    @Test
    public void test_Hotfix() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-hotfix");
        assertEquals(SimpleSemVersion.HOTFIX, ReflectionUtils.safeInvoke(MODIFIER.bindTo(version)));
    }

    @Test
    public void test_Release() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-release");
        assertEquals(SimpleSemVersion.RELEASE, ReflectionUtils.safeInvoke(MODIFIER.bindTo(version)));
    }

    @Test
    public void test_Newer_Than_AlphaBeta() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-beta");
        SimpleSemVersion other = SimpleSemVersion.fromString("1.2.3-alpha");

        assertTrue(version.isNewerThan(other));
    }

    @Test
    public void test_Newer_Than_AlphaSnapshot() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-snapshot");
        SimpleSemVersion other = SimpleSemVersion.fromString("1.2.3-alpha");

        assertTrue(version.isNewerThan(other));
    }

    @Test
    public void test_Newer_Than_AlphaRelease() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-release");
        SimpleSemVersion other = SimpleSemVersion.fromString("1.2.3-alpha");

        assertTrue(version.isNewerThan(other));
    }

    @Test
    public void test_Newer_Than_BetaSnapshot() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-snapshot");
        SimpleSemVersion other = SimpleSemVersion.fromString("1.2.3-beta");

        assertTrue(version.isNewerThan(other));
    }

    @Test
    public void test_Newer_Than_ReleaseHotFix() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.3-hotfix");
        SimpleSemVersion other = SimpleSemVersion.fromString("1.2.3-release");

        assertFalse(version.isNewerThan(other));
    }

    @Test
    public void test_Newer_Than_ReleaseHotFix_Higher_ThanRelease() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.2-release");
        SimpleSemVersion other = SimpleSemVersion.fromString("1.2.4-hotfix");
        assertTrue(version.isNewerThan(other));
    }

    @Test
    public void test_Older_Than_ReleaseHotFix_Lower_ThanRelease() {
        SimpleSemVersion version = SimpleSemVersion.fromString("1.2.4-beta");
        SimpleSemVersion other = SimpleSemVersion.fromString("1.2.5-hotfix");
        assertTrue(other.isNewerThan(version));
    }
}
