module kala.foreign.core {
    exports kala.foreign.annotations;

    opens kala.foreign.internal;

    requires static kala.base;
}