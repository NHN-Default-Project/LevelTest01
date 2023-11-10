package org.example.jminsu.functional;

public interface InfiniteIterator<T> extends java.util.Iterator<T> {
    // TODO: 채우기
    default boolean hasNext() {
        return true;
    }

}
