package org.example.pminsu.functional;

public interface InfiniteIterator<T> extends java.util.Iterator<T> {
    // TODO: 채우기

    @Override
    default boolean hasNext() {
        return true;
    }


}