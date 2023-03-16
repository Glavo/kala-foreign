package kala.foreign.internal;

import kala.foreign.annotations.Extern;

import java.lang.reflect.AnnotatedElement;

@Extern
public final class ExternUtils {
    public static final Extern DEFAULT = ExternUtils.class.getAnnotation(Extern.class);

    public static Extern get(AnnotatedElement element) {
        Extern annotation = element.getAnnotation(Extern.class);
        return annotation != null ? annotation : DEFAULT;
    }
}
