package org.example.main.functional;

import static org.example.main.Precondition.checkArgument;
import static org.example.main.Precondition.checkInfiniteIterator;
import static org.example.main.Precondition.checkNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Iterators {

    public static <E, R> R reduce(Iterable<E> es, BiFunction<R, E, R> biFunction, R init) {
        checkNotNull(es, "reduce parameter Iterable value Null");
        checkNotNull(biFunction, "reduce parameter BiFunction value Null ");
        checkNotNull(init);
        R result = init;
        for (E e : es) {
            result = biFunction.apply(result, e);
        }
        return result;
    }

    public static <E, R> R reduce(Iterator<E> es, BiFunction<R, E, R> biFunction, R init) {
        checkNotNull(es, "reduce parameter iterator Null ");
        checkNotNull(biFunction, "reduce parameter BiFunction biFunction value Null");
        checkNotNull(init, "reduce parameter R init value Null");
        return reduce(() -> es, biFunction, init);
    }

    public static <T> boolean equals(Iterator<T> xs, Iterator<T> ys) {
        checkNotNull(xs);
        checkNotNull(ys);
        checkInfiniteIterator(xs, "equals Iterator Parameter InfiniteIterator");
        checkInfiniteIterator(ys, "equals Iterator Parameter InfiniteIterator");
        return reduce(zip(Objects::equals, xs, ys), (x, y) -> x && y, true) && (xs.hasNext() == ys.hasNext());
    }

    public static <T> String toString(Iterator<T> es, String separator) {
        checkNotNull(es);
        checkNotNull(separator);
        StringBuilder sb = new StringBuilder();
        if (es.hasNext()) {
            sb.append(es.next());
        }

        return reduce(es, (o, t) -> o.append(separator).append(t), sb).toString();
    }


    public static <E, R> Iterator<R> map(Iterator<E> es, Function<E, R> function) {
        checkNotNull(es);
        checkNotNull(function);
        return new Iterator<R>() {
            public boolean hasNext() {
                return es.hasNext();
            }

            public R next() {
                return function.apply(es.next());
            }
        };
    }

    public static <E> Iterator<E> filter(Iterator<E> iterator, Predicate<E> predicate) {
        checkNotNull(iterator);
        checkNotNull(predicate);
        return new Iterator<E>() {
            private E nextCurrent = findFirst(iterator, predicate);

            public boolean hasNext() {
                return !Objects.isNull(nextCurrent);
            }

            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("filter");
                }
                E nowCurrent = nextCurrent;
                nextCurrent = findFirst(iterator, predicate);
                return nowCurrent;
            }
        };
    }

    public static <E> E findFirst(Iterator<E> iterator, Predicate<E> predicate) {
        checkNotNull(iterator, "findFirst iterator NullPointerException");
        checkNotNull(predicate, "findFirst predicate NullPointerException");
        while (iterator.hasNext()) {
            E first = iterator.next();
            if (predicate.test(first)) {
                return first;
            }
        }
        return null;
    }

    public static <T> InfiniteIterator<T> iterate(T seed, UnaryOperator<T> f) {
        checkNotNull(f, "iterate UnaryOperator NullPointException");
        return new InfiniteIterator<T>() {
            T current = seed;

            @Override
            public T next() {
                T old = current;
                current = f.apply(current);
                return old;
            }
        };
    }

    public static <T> Iterator<T> limit(Iterator<T> iterator, long maxSize) {
        checkNotNull(iterator);
        checkArgument(maxSize >= 0, "Limit NegativeNum");

        return new InfiniteIterator<T>() {

            long currentCount = 0;

            @Override
            public boolean hasNext() {
                return currentCount < maxSize && iterator.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("no such");
                }
                currentCount = Math.addExact(currentCount, 1);
                return iterator.next();
            }
        };
    }

    public static <T> InfiniteIterator<T> generate(Supplier<T> supplier) {
        checkNotNull(supplier);
        return new InfiniteIterator<T>() {
            @Override
            public T next() {
                return supplier.get();
            }
        };
    }

    public static <X, Y, Z> Iterator<Z> zip(BiFunction<X, Y, Z> biFunction, Iterator<X> xIterator,
                                            Iterator<Y> yIterator) {
        checkNotNull(biFunction);
        checkNotNull(xIterator);
        checkNotNull(yIterator);
        return new Iterator<Z>() {
            public boolean hasNext() {
                return xIterator.hasNext() && yIterator.hasNext();
            }

            public Z next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("zip");
                }
                return biFunction.apply(xIterator.next(), yIterator.next());
            }
        };
    }

    public static <E> long count(Iterator<E> iterator) {
        checkNotNull(iterator);

        return reduce(iterator, (o, e) -> Math.addExact(o, 1), 0);
    }

    public static <T> T get(Iterator<T> iterator, long index) {
        checkNotNull(iterator, "get Method Iterator Null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("index < " + index);
        }
        return getLast(limit(iterator, index + 1));
    }

    private static <T> T getLast(Iterator<T> iterator) {
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return current;
            }
        }
    }

    public static <T> List<T> toList(Iterator<T> iterator) {
        checkNotNull(iterator);
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <E> void print(Iterator<E> iterator, String separator,
                                 java.io.PrintStream printStream) {
        printStream.print(toString(iterator, separator));
    }

    public static <E> void print(Iterator<E> iterator, String separator) {
        print(iterator, separator, System.out);
    }

    public static <E> void println(Iterator<E> iterator, String separator,
                                   java.io.PrintStream printStream) {
        print(iterator, separator, printStream);
        printStream.println();
    }

    public static <E> void println(Iterator<E> iterator, String separator) {
        println(iterator, separator, System.out);
    }

    public static <E> void println(Iterator<E> iterator) {
        println(iterator, ", ");
    }

    private Iterators() {
    }

}

