package kala.foreign.processor.type;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record MethodType(JavaType returnType, List<JavaType> parameterTypes) {
    public MethodType(JavaType returnType) {
        this(returnType, Collections.emptyList());
    }

    @Override
    public String toString() {
        return parameterTypes
                .stream()
                .map(JavaType::toString)
                .collect(Collectors.joining(", ", returnType.toString() + "(", ")"));
    }
}
