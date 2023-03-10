package kala.foreign;

import jdk.internal.access.JavaLangAccess;
import jdk.internal.access.SharedSecrets;
import jdk.internal.misc.Unsafe;

import java.util.function.BiConsumer;

public final class Foreign {

    private static final int REQUIRED_JAVA_VERSION = 19;

    private static final boolean useLambdaMetafactory = getBooleanOption("kala.foreign.useLambdaMetafactory", true);
    private static final boolean useDynamicClassLoad = getBooleanOption("kala.foreign.useDynamicClassLoad", true);

    static final Unsafe UNSAFE;

    static {
        // Currently, Panama is still in preview status, so we must work on a specific Java version
        if (Runtime.version().feature() != REQUIRED_JAVA_VERSION) {
            if (Runtime.version().feature() < REQUIRED_JAVA_VERSION) {
                throw new Error("Please update to Java " + REQUIRED_JAVA_VERSION);
            } else {
                throw new Error("The current version of Kala Foreign does not support Java " + Runtime.version().feature()
                        + ", please update Kala Foreign");
            }
        }

        Module theModule = Foreign.class.getModule();

        JavaLangAccess javaLangAccess;
        try {
            javaLangAccess = SharedSecrets.getJavaLangAccess();
        } catch (IllegalAccessError e) {
            throw new Error(
                    "Kala Foreign requires JVM option: --add-opens=java.base/jdk.internal.access="
                            + (theModule.isNamed() ? theModule.getName() : "ALL-UNNAMED"));
        }

        // Enable Native Access
        if (theModule.isNamed()) {
            javaLangAccess.addEnableNativeAccess(theModule);
        } else {
            javaLangAccess.addEnableNativeAccessAllUnnamed();
        }

        // Add Opens
        BiConsumer<Module, String> addOpens = theModule.isNamed()
                ? ((m, p) -> javaLangAccess.addOpens(m, p, theModule))
                : javaLangAccess::addOpensToAllUnnamed;
        Module javaBase = Object.class.getModule();

        addOpens.accept(javaBase, "jdk.internal.misc");

        UNSAFE = Unsafe.getUnsafe();

    }

    private static boolean getBooleanOption(String name, boolean defaultValue) {
        String value = System.getProperty(name);
        return value == null ? defaultValue : "true".equalsIgnoreCase(value);
    }

    public static void init() {
    }
}
