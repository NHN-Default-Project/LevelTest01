package org.example.pminsu.functional;

import static org.example.pminsu.functional.base.Precondition.*;

import java.util.*;
import java.util.function.*;

public class Iterators {

    public static <E, R> R reduce(Iterable<E> es, BiFunction<R, E, R> biFunction, R init) {
        checkNotNull(es, "reduce parameter Iterable value Null");
        checkNotNull(biFunction, "reduce parameter BiFunction value Null ");
        checkNotNull(init);
        R result = init;

        for (E e : es) // next
            result = biFunction.apply(result, e);

        return result;
    }

    /**
     * 해당 메서드에도 null check를 한 이유는 Iterator에 null이 들어와도 reduce(Iterable, BiFunction, R) 메서드로 넘어가면 Iterable null check에서 검사가 안됨
     * 다른 parameter 또한 reduce로 넘어가서 하기 보다는 미리 잡고 싶어서 해당 메서드에도 check를 함
     */
    public static <E, R> R reduce(Iterator<E> es, BiFunction<R, E, R> biFunction, R init) {
        checkNotNull(es, "reduce parameter iterator Null ");
        checkNotNull(biFunction, "reduce parameter BiFunction biFunction value Null");
        checkNotNull(init, "reduce parameter R init value Null");
        return reduce(() -> es, biFunction, init);
    }

    /**
     * 두 개의 iterator 동일한 iterator인지 판별하는 메서드
     * InfiniteIterator
     */
    public static <T> boolean equals(Iterator<T> xs, Iterator<T> ys) { // TODO: reduce, zip을 써서
        checkNotNull(xs);
        checkNotNull(ys);
        checkInfiniteIterator(xs, "equals Iterator Parameter InfiniteIterator");
        checkInfiniteIterator(ys, "equals Iterator Parameter InfiniteIterator");

        return reduce(zip(Objects::equals, xs, ys), (total, element) -> total && element, true) && (xs.hasNext() == ys.hasNext());
    }

    public static <T> String toString(Iterator<T> es, String separator) { // TODO: reduce를 써서
        checkNotNull(es);
        checkNotNull(separator);

        return reduce(es, (x, y) -> {
            x.append(y);
            if (es.hasNext()) {
                x.append(separator);
            }
            return x;
        }, new StringBuilder()).toString();
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

        // TODO: Bug를 찾을 수 있는 test code를 IteratorTest.filterTest에 쓰고, Bug 고치기
        // findFirst를 써서 풀기
        checkNotNull(iterator);
        checkNotNull(predicate);
        return new Iterator<E>() {
            private E current = findFirst(iterator, predicate);

            public boolean hasNext() {
                return !Objects.isNull(current);
            }

            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("filter");
                }
                E item = current;
                current = findFirst(iterator, predicate);

                return item;
            }
        };


    }

    public static <E> E findFirst(Iterator<E> iterator, Predicate<E> predicate) {
        checkNotNull(iterator, "findFirst iterator NullPointerException");
        checkNotNull(predicate, "findFirst predicate NullPointerException");

        while (iterator.hasNext()) { // 무한 루프
            E first = iterator.next();
            if (predicate.test(first))
                return first;
        }
        return null;
    }

    /**
     * 값이 무한대로 들어오는 Iterator
     * seed의 값은 null이 들어와도 Functional Interface의 식에 따라서 null이 안나올수도 있으므로
     * 예시로 iterate(null, x-> 1)를 하면 1의 값이 나옴
     * seed 값에 대한 Null check는 하지 않음
     *
     * @param seed 처음 초기값
     * @param f    Functional Interface
     */
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

    /**
     * 해당 메서드는 maxSize의 값만큼 iterator가 원소를 반환함
     * <p>
     * parameter로 들어오는 Iterator가 remove를 지원할 경우에 반환되는 Iterator도 remove를 지원
     *
     * @param iterator limit Iterator
     * @param maxSize  limit Iterator의 maxValue
     * @throws IllegalArgumentException maxSize의 값이 음수인 경우
     */
    public static <T> Iterator<T> limit(Iterator<T> iterator, long maxSize) { // TODO
        checkNotNull(iterator);
        checkArgument(maxSize >= 0, "Limit NegativeNum");
        return new Iterator<T>() {
            long remainInt = 0;

            @Override
            public boolean hasNext() {
                return remainInt < maxSize && iterator.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("limit");

                }
                remainInt = Math.addExact(remainInt, 1);
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    public static <T> InfiniteIterator<T> generate(Supplier<T> supplier) { // TODO:
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
                if (!hasNext())
                    throw new NoSuchElementException("zip");
                return biFunction.apply(xIterator.next(), yIterator.next());
            }
        };
    }

    public static <E> long count(Iterator<E> iterator) {
        checkNotNull(iterator);
        return reduce(iterator, (x, y) -> Math.addExact(x, 1), 0);
    }

    public static <T> T get(Iterator<T> iterator, long index) {
        checkNotNull(iterator, "get Method Iterator Null");
        if (index < 0)
            throw new IndexOutOfBoundsException("index < " + index);
        return getLast(limit(iterator, index + 1));
    }

    private static <T> T getLast(Iterator<T> iterator) {
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext())
                return current;
        }
    }

    public static <T> List<T> toList(Iterator<T> iterator) {
        checkNotNull(iterator);
        List<T> list;
        list = new ArrayList<T>();
        while (iterator.hasNext()) {
            T value = iterator.next();
            list.add(value);
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


