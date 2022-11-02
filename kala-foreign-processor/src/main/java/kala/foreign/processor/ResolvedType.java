package kala.foreign.processor;

import javassist.bytecode.SignatureAttribute;

import java.lang.reflect.Type;

record ResolvedType(Type nativeType, SignatureAttribute.Type javaType) {

}
