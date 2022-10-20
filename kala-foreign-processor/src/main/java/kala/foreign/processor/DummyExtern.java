package kala.foreign.processor;

import kala.foreign.annotations.Extern;

@Extern
final class DummyExtern {
    static final Extern INSTANCE = DummyExtern.class.getAnnotation(Extern.class);
}
