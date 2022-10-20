package kala.foreign.processor.type;

import java.util.List;

public record JavaMethodType(JavaType returnType, List<JavaType> parameters) {

}
