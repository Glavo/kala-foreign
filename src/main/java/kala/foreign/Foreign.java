package kala.foreign;

import jdk.internal.access.JavaLangAccess;
import jdk.internal.access.SharedSecrets;
import jdk.internal.misc.Unsafe;

import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public final class Foreign {

    static final System.Logger LOGGER = System.getLogger("kala.foreign");

    private static final int REQUIRED_JAVA_VERSION = 19;
    private static final boolean useDynamicClassLoad = Boolean.parseBoolean(System.getProperty("kala.foreign.useDynamicClassLoad", "true"));

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

    public static <T> T load(Class<T> clazz, SymbolLookup symbolLookup, Linker linker) {
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException(clazz + " must be abstract");
        }

        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (Modifier.isAbstract(method.getModifiers())) {
                methods.add(method);
            }
        }

        return null; // TODO
    }

}
