package com.gonzobeans;

import java.util.NoSuchElementException;

public class ResolverException extends NoSuchElementException {
    public ResolverException() {
    }

    public ResolverException(String s) {
        super(s);
    }
}
