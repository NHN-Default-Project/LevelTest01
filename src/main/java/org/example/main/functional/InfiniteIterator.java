package org.example.main.functional;

import java.util.Iterator;

public interface InfiniteIterator<T> extends Iterator<T> {
    @Override
    default boolean hasNext() {
        return true;
    }
}