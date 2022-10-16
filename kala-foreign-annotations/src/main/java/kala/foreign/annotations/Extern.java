package kala.foreign.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Extern {
    String name() default DEFAULT_NAME;

    String charset() default "UTF-8";

    CallingConvention callingConvention() default CallingConvention.Cdecl;

    String DEFAULT_NAME = "<default name>";

    enum CallingConvention {
        Cdecl, StdCall
    }
}
