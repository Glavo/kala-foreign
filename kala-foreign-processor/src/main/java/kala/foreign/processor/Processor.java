package kala.foreign.processor;

import javassist.*;
import kala.foreign.annotations.Extern;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public final class Processor {
    private final ClassPool classPool;
    private final boolean keepModifyTime;
    private final List<Path> files;

    private Processor(ClassPool classPool, boolean keepModifyTime, List<Path> files) {
        this.classPool = classPool;
        this.keepModifyTime = keepModifyTime;
        this.files = files;
    }


    private void processFile(Path file) throws Exception {
        CtClass cls;
        try (var input = Files.newInputStream(file)) {
            cls = classPool.makeClass(input);
        }

        var classExternAnnotation = (Extern) cls.getAnnotation(Extern.class);
        if (classExternAnnotation == null || !classExternAnnotation.isForeign())
            return;

        FileTime lastModifiedTime = Files.getLastModifiedTime(file);

        var nativeMethods = Arrays.stream(cls.getDeclaredMethods())
                .filter(it -> Modifier.isNative(it.getModifiers()))
                .toList();

        if (nativeMethods.isEmpty())
            return;

        CtConstructor classInitializer = cls.makeClassInitializer();

        for (CtMethod nativeMethod : nativeMethods) {
            try {
                nativeMethod.setModifiers(nativeMethod.getModifiers() & ~Modifier.NATIVE);

            } catch (Throwable e) {
                System.err.println("An error occurred while processing method " + nativeMethod.toString());
                throw e;
            }
        }

        try (var output = new DataOutputStream(Files.newOutputStream(file))) {
            cls.toBytecode(output);
        }
        if (keepModifyTime)
            Files.setLastModifiedTime(file, lastModifiedTime);
    }

    public int run() {
        for (Path file : files) {
            try {
                processFile(file);
            } catch (Throwable e) {
                System.err.println("Failed to process file " + file);
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static final class Builder {
        private ClassPool classPool = ClassPool.getDefault();
        private boolean keepModifyTime = false;
        private final List<Path> files = new ArrayList<>();

        public Builder keepModifyTime(boolean value) {
            this.keepModifyTime = value;
            return this;
        }

        public Builder addFile(Path file) {
            this.files.add(file);
            return this;
        }

        public ClassPool getClassPool() {
            return classPool;
        }

        public Processor build() {
            return new Processor(classPool, keepModifyTime, files);
        }
    }

    public static void main(String[] args) throws Exception {
        Builder builder = new Builder();

        Consumer<String> missArg = option -> {
            System.err.println("option '" + option + "' requires argument");
            System.exit(1);
        };

        List<String> files = null;

        loop:
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-c", "-cp", "--cp", "-class-path", "--class-path", "-classpath", "--classpath" -> {
                    if (i == args.length - 1)
                        missArg.accept(arg);
                    else
                        builder.getClassPool().appendClassPath(args[++i]);
                }
                case "-keep-modify-time", "--keep-modify-time" -> builder.keepModifyTime(true);
                default -> {
                    files = Arrays.asList(args).subList(i, args.length - 1);
                    break loop;
                }
            }
        }

        if (files == null) {
            System.err.println("require input files");
            System.exit(1);
        }

        for (String file : files) {
            builder.addFile(Path.of(file));
        }

        System.exit(builder.build().run());
    }
}
