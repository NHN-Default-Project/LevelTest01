package org.example.jaehyeon.functional;

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

    public static <T> boolean equals(Iterator<T> xs, Iterator<T> ys) {
        if (Objects.isNull(xs) || Objects.isNull(ys)) {
            throw new NullPointerException("equals() null? you throw");
        }
        return reduce(zip(Objects::equals, xs, ys), (x, y) -> x && y, true) && (xs.hasNext() == ys.hasNext());
        // xs.hasNext() && ys.hasNext(); 이걸 해주는 이유 : zip은 길이가 다르면 더 짧은 쪽의 요소만을 고려한다
        //zip은 Iterator 중 하나가 다른 하나보다 더 길다면 나머지 요소를 무시하게 된다.
        // 이러한 이유로 이 구문을 추가하여 동일한 길이인 경우에만 true를 반환하도록 해야한다.
    }

    public static <T> String toString(Iterator<T> es, String separator) {
        if (Objects.isNull(es) || Objects.isNull(separator)) {
            throw new NullPointerException("toString() null? you throw");
        }
        StringBuilder sb = new StringBuilder();
        if (es.hasNext()) {
            sb.append(es.next());
        }

        return reduce(es, (o, t) -> o.append(separator).append(t), sb).toString();
    }


    public static <E, R> Iterator<R> map(Iterator<E> es, Function<E, R> function) {
        if (Objects.isNull(es) || Objects.isNull(function)) {
            throw new NullPointerException("map() null? you throw");
        }
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
        if (Objects.isNull(iterator) || Objects.isNull(predicate)) {
            throw new NullPointerException("null? you throw");
        }

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
        if (Objects.isNull(iterator) || Objects.isNull(predicate)) {
            throw new NullPointerException("findFirst() null? you throw");
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
        if (Objects.isNull(seed) || Objects.isNull(f)) {
            throw new NullPointerException("iterate() null? you throw");
        }
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
        if (Objects.isNull(iterator) || maxSize < 0) {
            throw new IllegalArgumentException("iterator() null? you throw, maxSize check plz");
        }

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
        if (Objects.isNull(supplier)) {
            throw new NoSuchElementException("");
        }
        return new InfiniteIterator<T>() {
            @Override
            public T next() {
                return supplier.get();
            }
        };
    }

    public static <X, Y, Z> Iterator<Z> zip(BiFunction<X, Y, Z> biFunction, Iterator<X> xIterator,
                                            Iterator<Y> yIterator) {
        if (Objects.isNull(biFunction) || Objects.isNull(xIterator) || Objects.isNull(yIterator)) {
            throw new NullPointerException("zip() null? you throw");
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
            throw new IllegalArgumentException("count() null? you throw");
        }
        return reduce(iterator, (o, e) -> Math.addExact(o, 1), 0);
    }

    public static <T> T get(Iterator<T> iterator, long index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index < " + index);
        } else if (Objects.isNull(iterator)) {
            throw new NullPointerException("get() null? you throw");
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
        if (Objects.isNull(iterator)) {
            throw new NullPointerException("toList() null? you throw");
        }
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


