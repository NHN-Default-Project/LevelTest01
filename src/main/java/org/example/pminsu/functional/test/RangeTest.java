package org.example.pminsu.functional.test;

import static org.junit.jupiter.api.Assertions.*;


import java.awt.font.NumericShaper;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.example.pminsu.functional.Iterators;
import org.example.pminsu.functional.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RangeTest {
    @Test
    void rangeClassInvariantExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> new Range(10, 5));
        assertThrows(IllegalArgumentException.class, () -> new Range(10, Long.MAX_VALUE + 1)); //
        assertThrows(IllegalArgumentException.class, () -> new Range(0, Integer.MAX_VALUE + 1));
    }

    @Test
    void maxTest() {
        int min = 10;
        int max = 50;
        Assertions.assertEquals(Range.closed(min, max).max(), max);
    }

    @Test
    void minTest() {
        int start = 10;
        int end = 50;
        Assertions.assertEquals(Range.closed(start, end).min(), start);
    }

    @Test
    void sizeTest() {
        int start = 10;
        int end = 40;
        Range range = new Range(start, end);
        assertEquals(range.size(), Math.subtractExact(end, start));
    }

    @Test
    void iteratorTest() {
        int min = 10;
        int max = 20;
        Range range = new Range(min, max);

        Assertions.assertTrue(Iterators.elementEquals(range.iterator(), Stream.iterate(10, x -> x + 1)
                .limit(max - min).map(Integer::longValue)
                .iterator()));
    }

    @Test
    @DisplayName("Test the next method after iterator is called")
    void iteratorCallNextMethodTest() {
        Range range = new Range(1, 3);
        while (range.iterator().hasNext()) {
            range.iterator().next();
        }
        assertThrows(NoSuchElementException.class, () -> range.iterator().next());
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