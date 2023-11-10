package org.example.jminsu.functional;

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
        R result = init;
        for (E e : es) {
            result = biFunction.apply(result, e);
        }
        return result;
    }

    public static <E, R> R reduce(Iterator<E> es, BiFunction<R, E, R> biFunction, R init) {
        return reduce(() -> es, biFunction, init);
    }

    // TODO: reduce를 써서 check1
    public static <T> boolean equals(Iterator<T> xs, Iterator<T> ys) {
        if (xs == null || ys == null) {
            throw new NoSuchElementException("equals");
        }

        return (reduce(zip(Objects::equals, xs, ys), (total, element) -> total && element, true) &&
                xs.hasNext() == ys.hasNext());
    }

    // TODO: redude를 써서
    public static <T> String toString(Iterator<T> es, String separator) {
        if (Objects.isNull(es) || Objects.isNull(separator)) {
            throw new NoSuchElementException("toString");
        }

        StringBuilder sb = new StringBuilder();

        if (es.hasNext()) {
            sb.append(es.next());
        }

        return reduce(es, (result, add) -> result.append(separator).append(add), sb).toString();
    }

    public static <E, R> Iterator<R> map(Iterator<E> es, Function<E, R> function) {

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
        // TODO: Bug를 찾을 수 있는 test code를 IteratorTest.filterTest에 쓰고, Bug 고치기
        // findFirst를 써서 풀기

        if (Objects.isNull(iterator) || Objects.isNull(predicate)) {
            throw new IllegalArgumentException("filter!");
        }
        return new Iterator<>() {
            private E current = findFirst(iterator, predicate);

            public boolean hasNext() {
                return !Objects.isNull(this.current);
            }

            @Override
            public E next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException("filter");
                }

                E tempData = this.current;
                this.current = findFirst(iterator, predicate);

                return tempData;
            }
        };
    }

    public static <E> E findFirst(Iterator<E> iterator, Predicate<E> predicate) {
        if (predicate == null || iterator == null) {
            throw new NullPointerException("findFirst!");
        }
        while (iterator.hasNext()) {
            E first = iterator.next();
            if (predicate.test(first)) {
                return first;
            }
        }
        return null;
    }

    public static <T> InfiniteIterator<T> iterate(T seed, UnaryOperator<T> f) {
        if (f == null) {
            throw new NullPointerException("iterate!");
        }
        return new InfiniteIterator<T>() {
            T current = seed;

            //TODO

            @Override
            public T next() {
                T old = current;
                current = f.apply(current);
                return old;
            }
        };
    }

    // TODO
    public static <T> Iterator<T> limit(Iterator<T> iterator, long maxSize) {
        if (maxSize < 0 || iterator == null) {
            throw new IllegalArgumentException();
        }

        return new Iterator<T>() {
            long count = 0;

            @Override
            public boolean hasNext() {
                return maxSize > count && iterator.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                count = Math.addExact(this.count, 1);

                return iterator.next();

            }
        };
    }

    public static <T> InfiniteIterator<T> generate(Supplier<T> supplier) { // TODO:
        if (supplier == null) {
            throw new NullPointerException("generate!");
        }

        return supplier::get;
    }

    public static <X, Y, Z> Iterator<Z> zip(BiFunction<X, Y, Z> biFunction, Iterator<X> xIterator,
                                            Iterator<Y> yIterator) {
        if (biFunction == null || xIterator == null || yIterator == null) {
            throw new NullPointerException("zip!");
        }
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
        if (Objects.isNull(iterator)) {
            throw new IllegalArgumentException("count");
        }
        // TODO: reduce를 써서
        return reduce(iterator, (x, y) -> Math.addExact(x, 1), 0);
    }

    public static <T> T get(Iterator<T> iterator, long index) {
        if (iterator == null) {
            throw new NullPointerException("iterator is null!");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("index < " + index);
        }
        return getLast(limit(iterator, Math.addExact(index, 1)));
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


