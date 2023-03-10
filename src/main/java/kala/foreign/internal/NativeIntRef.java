package kala.foreign.internal;

import kala.value.primitive.MutableIntValue;

import java.lang.foreign.Addressable;
import java.lang.foreign.ValueLayout;

public final class NativeIntRef implements MutableIntValue {
    private final Addressable address;

    public NativeIntRef(Addressable address) {
        this.address = address;
    }

    @Override
    public int get() {
        return address.address().get(ValueLayout.JAVA_INT, 0);
    }

    @Override
    public void set(int value) {
        address.address().set(ValueLayout.JAVA_INT, 0, value);
    }
}
