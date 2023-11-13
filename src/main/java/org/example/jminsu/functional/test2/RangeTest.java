package org.example.jminsu.functional.test2;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.example.jminsu.functional.Iterators;
import org.example.jminsu.functional.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RangeTest {
    @Test
    void rangeClassInvariantExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> new Range(10, 5));
        assertThrows(ArithmeticException.class, () -> new Range(10, Long.MAX_VALUE + 1));
        assertThrows(IllegalArgumentException.class, () -> new Range(0, Integer.MAX_VALUE + 1));
    }

    @Test
    void maxTest() {
        int start = 10;
        int max = 50;
        Range range = new Range(start, max);
        Assertions.assertEquals(range.max(), Math.subtractExact(max, 1));
    }

    @Test
    void minTest() {
        int start = 10;
        int end = 50;
        Range range = new Range(start, end);
        Assertions.assertEquals(range.min(), start);
    }

    @Test
    void endTest() {
        int start = 10;
        int end = 50;
        Range range = new Range(start, end);
        assertEquals(range.end(), end);
    }

    @Test
    void sizeTest() {
        int start = 10;
        int end = 40;
        Range range = new Range(start, end);
        assertEquals(range.size(), end - start);
    }

    @Test
    void iteratorTest() {
        int min = 10;
        int max = 20;
        Range range = new Range(min, max);

        Assertions.assertTrue(Iterators.equals(range.iterator(), Stream.iterate(10, x -> x + 1)
                .limit(max - min).map(Integer::longValue)
                .iterator()));
    }

    @Test
    @DisplayName("Test the next method after iterator is called")
    void iteratorCallNextMethodTest() {
        Range range = new Range(1, 3);
        Iterator<Long> iter = range.iterator();
        while (iter.hasNext()) {
            iter.next();
        }
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    @DisplayName("Test the hashNext method after iterator is called")
    void iteratorCallHashNextMethodTest() {
        Range range = new Range(1, 4);
        Iterator<Long> iterators = range.iterator();
        while (iterators.hasNext()) {
            iterators.next();
        }
        assertFalse(iterators.hasNext());
    }
}