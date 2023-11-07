package org.example.jaehyeon.functional.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.stream.Stream;
import org.example.jaehyeon.functional.Iterators;
import org.example.jaehyeon.functional.Range;
import org.junit.jupiter.api.Test;

public class RangeTest {

    @Test
    public void constructorTwoParameterTest() {
        long errorStart = 1;
        long errorEnd = 1;

        assertThrows(IllegalArgumentException.class, () -> new Range(errorStart, errorEnd));
        assertThrows(ArithmeticException.class, () -> new Range(0, Long.MAX_VALUE + 1));

        long start = 1;
        long end = 2;
        Range range = new Range(start, end);
        assertEquals(range.getStartInclusive(), start);
        assertEquals(range.getEndExclusive(), end);
    }

    @Test
    public void constructorOneParameterTest() {
        long errorEnd = 1;
        assertThrows(IllegalArgumentException.class, () -> new Range(errorEnd));

        long end = 2;
        Range range = new Range(end);
        assertEquals(range.getStartInclusive(), 1);
        assertEquals(range.getEndExclusive(), end);
    }

    @Test
    public void closedTest() {
        assertTrue(Range.closed(1, 3) instanceof Range);
        long errorStart = 2;
        long errorEnd = 1;
        assertThrows(IllegalArgumentException.class, () -> Range.closed(errorStart, errorEnd));

        long start = 1;
        long end = 2;
        Range range = Range.closed(1, 2);
        assertEquals(range.getStartInclusive(), start);
        assertEquals(range.getEndExclusive(), end + 1);
    }

    @Test
    public void maxTest() {
        long start = 1;
        long end = 4;
        Range range = new Range(start, end);
        assertEquals(range.max(), Math.subtractExact(end, 1));
    }

    @Test
    public void minTest() {
        long start = 1;
        long end = 4;
        Range range = new Range(start, end);
        assertEquals(range.min(), start);
    }

    @Test
    public void endTest() {
        long start = 1;
        long end = 4;
        Range range = new Range(start, end);
        assertEquals(range.end(), end);
    }

    @Test
    public void sizeTest() {
        long start = 1;
        long end = 4;
        Range range = new Range(start, end);
        assertEquals(range.size(), Math.subtractExact(end, start));
    }

    @Test
    public void iteratorTest() {
        long start = 1;
        long end = 10;
        Range range = new Range(start, end);
        assertTrue(range.iterator() instanceof Iterator);
        assertTrue(range.iterator().next() instanceof Long);

        Iterator<Long> iterator = range.iterator();
        Iterator<Long> result = Stream.iterate(1L, x -> x + 1).limit(end - 1).iterator();
        assertTrue(Iterators.equals(iterator, result));
    }


}
