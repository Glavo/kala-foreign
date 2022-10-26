package kala.foreign.processor;

import kala.foreign.annotations.Extern;

public record ExternChain(Extern extern, ExternChain parent) {
    public ExternChain(Extern extern) {
        this(extern, null);
    }

    public Extern getClassAnnotation() {
        ExternChain chain = this;
        while (chain.parent != null)
            chain = chain.parent;
        return chain.extern;
    }
}
