package kala.foreign.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

public final class LibraryProxy implements InvocationHandler {

    private final Map<Method, Function<Object[], Object>> handles;

    public LibraryProxy(Map<Method, Function<Object[], Object>> handles) {
        this.handles = handles;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Function<Object[], Object> impl = handles.get(method);
        if (impl == null) {
            throw new NoSuchMethodError(method.toString());
        }
        return impl.apply(args);
    }
}
