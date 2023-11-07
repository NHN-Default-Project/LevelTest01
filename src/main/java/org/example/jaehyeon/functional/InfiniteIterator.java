package org.example.jaehyeon.functional;

import java.util.Iterator;

public interface InfiniteIterator<T> extends Iterator<T> {
    default boolean hasNext(){
        return true;
    }

}
