package org.example.pminsu.functional.exception;

public class InfiniteIteratorException extends IllegalArgumentException {
    public InfiniteIteratorException(String message) {
        super(message);
    }

    public InfiniteIteratorException() {
    }
}
