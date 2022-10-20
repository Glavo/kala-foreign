package kala.foreign.processor.type;

import javassist.CtClass;

import java.util.List;
import java.util.stream.Collectors;

public sealed interface JavaType {

    record PrimitiveType(CtClass ctClass) implements JavaType {
        @Override
        public String toString() {
            return ctClass.getName();
        }
    }

    record ClassType(CtClass ctClass) implements JavaType {
        @Override
        public String toString() {
            return ctClass.getName();
        }
    }

    record GenericType(CtClass ctClass, List<JavaType> typeArguments) implements JavaType {
        public GenericType {
            assert !typeArguments.isEmpty();
        }

        @Override
        public String toString() {
            return typeArguments.stream()
                    .map(JavaType::toString)
                    .collect(Collectors.joining(", ", ctClass.getName() + "<", ">"));
        }
    }

    record TypeVariable(String name, JavaType bounds, GenericDeclaration genericDeclaration) /*implements JavaType*/ {
    }
}
