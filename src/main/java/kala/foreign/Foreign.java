package kala.foreign;

import jdk.internal.access.JavaLangAccess;
import jdk.internal.access.SharedSecrets;
import jdk.internal.misc.Unsafe;
import kala.foreign.annotations.Extern;
import kala.foreign.internal.ExternUtils;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class Foreign {

    static final System.Logger LOGGER = System.getLogger("kala.foreign");

    private static final int REQUIRED_JAVA_VERSION = 20;
    private static final boolean useDynamicClassLoad;

    static final Unsafe UNSAFE;
    static final MethodHandles.Lookup lookup;

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

        String useDynamicClassLoadProperty = System.getProperty("kala.foreign.useDynamicClassLoad");
        if (useDynamicClassLoadProperty != null) {
            useDynamicClassLoad = Boolean.parseBoolean(useDynamicClassLoadProperty);
        } else {
            boolean hasClassfile = false;

            try {
                Class.forName("org.glavo.classfile.Classfile", false, Foreign.class.getClassLoader());
                hasClassfile = true;
            } catch (ClassNotFoundException ignored) {
            }

            useDynamicClassLoad = hasClassfile;
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
        lookup = MethodHandles.lookup();
    }

    private record NativeMethodInfo(String name, MethodType javaType, FunctionDescriptor nativeDescriptor) {
    }

    private static MethodHandle unreflect(Method method) {
        try {
            return lookup.unreflect(method);
        } catch (IllegalAccessException e) {
            IllegalAccessError err = new IllegalAccessError(e.getMessage());
            err.initCause(e);
            throw err;
        }
    }

    public static <T> T load(Class<T> clazz, SymbolLookup symbolLookup, Linker linker) {
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException(clazz + " must be abstract");
        }

        Extern classAnnotation = ExternUtils.get(clazz);

        List<NativeMethodInfo> info = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                continue;
            }

            Extern methodAnnotation = ExternUtils.get(method);
            Class<?>[] parameterTypes = method.getParameterTypes();
            MethodType javaType = MethodType.methodType(method.getReturnType(), parameterTypes);



        }

        return null; // TODO
    }

    public static <T> T findFunction(Class<T> clazz, SymbolLookup symbolLookup, Linker linker) {
        return null; // TODO
    }
}
