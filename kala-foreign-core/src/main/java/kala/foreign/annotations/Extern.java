package kala.foreign.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Extern {
    String name() default DEFAULT;

    String charset() default DEFAULT;

    String callingConvention() default DEFAULT;

    Class<?> nativeType() default Extern.class;

    boolean isJNI() default false;

    Class<?> nativeReturnType() default Extern.class;

    String DEFAULT = "<default value>";
}
