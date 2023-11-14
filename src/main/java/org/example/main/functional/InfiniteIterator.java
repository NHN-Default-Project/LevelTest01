package org.example.main.functional;

import java.util.Iterator;
import org.junit.Test;
// a = b
// b= a

public interface InfiniteIterator<T> {
    boolean hasNext();
    T next();
}