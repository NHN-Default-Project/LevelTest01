package org.example.pminsu.functional;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Range implements Iterable<Long> {
    private final long startInclusive;
    private final long endExclusive;

    public Range(long startInclusive, long endExclusive) {// min < max
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        classInvariant();
    }

    public Range(long endExclusive) {
        this(1, endExclusive);
    }

    public static Range closed(long startInclusive, long endInclusive) {
        return new Range(startInclusive, endInclusive + 1);
    }

    private void classInvariant() {
        if (max() < min())
            throw new IllegalArgumentException("Range: " + this.min() + " > " + this.max());
    }

    public long max() {
        return Math.subtractExact(endExclusive, 1);
    } // Maxvalue

    public long min() {
        return this.startInclusive;
    }

    public long end() {
        return this.endExclusive;
    }

    public long size() {
        return Math.subtractExact(this.end(), this.min());
    }

    public Iterator<Long> iterator() {
        return new Iterator<Long>() {

            private long current = min();

            public boolean hasNext() {
                return current < end();
            }

            public Long next() {
                if (!hasNext())
                    throw new NoSuchElementException("Range.iterator()");
                long value = current;
                current = Math.addExact(current, 1);
                return value;
            }
        };
    }
}
