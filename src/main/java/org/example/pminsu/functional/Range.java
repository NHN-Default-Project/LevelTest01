package org.example.pminsu.functional;

import java.util.Iterator;
import java.util.NoSuchElementException;

//TODO 제네릭 타입으로 하면은 괜찮을듯?

public final class Range implements Iterable<Long> { // long 타입인것도 그런거같아
    private final long startInclusive;
    private final long endInclusive;

    public Range(long startInclusive, long endInclusive) { // min < max
        classInvariant(startInclusive, endInclusive); //
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive; //
    }

    // Long.Maxvalue, Integer.MaxValue
    public Range(long endExclusive) { // range의 기준을 1로 해도 되는건가..?
        this(1, endExclusive);
    }

    //
    public static Range closed(long startInclusive, long endInclusive) {
        return new Range(startInclusive, endInclusive);
    }

    private void classInvariant(long startInclusive, long endExclusive) {
        if (endExclusive <= startInclusive)
            throw new IllegalArgumentException("Range: " + startInclusive + " > " + endExclusive);
    }

    public long min() {
        return this.startInclusive;
    }

    public long max() {
        return this.endInclusive;
    }

    public long size() {
        return Math.subtractExact(this.max(), this.min());
    }

    public Iterator<Long> iterator() {

        return new Iterator<Long>() {

            private long current = min();

            public boolean hasNext() {
                return current <= max();
            }

            public Long next() {
                if (!hasNext())
                    throw new NoSuchElementException("Range.iterator()");
                current = Math.addExact(current, 1);
                return current;
            }
        };
    }
}
