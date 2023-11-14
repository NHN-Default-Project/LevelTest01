package org.example.pminsu.functional;

import static org.example.pminsu.functional.base.Precondition.checkArgument;
import static org.example.pminsu.functional.base.Precondition.checkNotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class Iterators {
    /**
     * Iterable가 제공하는 항등 값과 관된 누적 함수를 사용하여 축소 작업을 수행하고 축소된 값을 반환합니다.
     *
     * @param <E> Iterable의 타입
     * @param <R> init과 biFunction의 반환 값 타입
     * @param es 비교할 요소를 포함하는 iterable
     * @param biFunction
     * @throws NullPointerException {@code es} null 인 경우
     * @throws NullPointerException {@code bifunction} null 인 경우
     * @return 축소된 값
     * */
    public static <E, R> R reduce(Iterable<E> es, BiFunction<R, E, R> biFunction, R init) {
        checkNotNull(es, "reduce parameter Iterable value Null");
        checkNotNull(biFunction, "reduce parameter BiFunction value Null ");
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
        return reduce(() -> es, biFunction, init);
    }

    /**
     * Iterator 안에있는 요소들의 값을 비교하여 값이 동일하면 true, 다르면 false를 던짐
     * Iteraotr는 소진된 상태로 유지가 됨.
     *
     * @param <T> Iterator xs,ys 타입
     * @param xs generic type Iterator
     * @param ys generic type Iterator
     */
    public static <T> boolean elementEquals(Iterator<T> xs, Iterator<T> ys) { // TODO: reduce, zip을 써서
        checkNotNull(xs);
        checkNotNull(ys);

        return reduce(zip(Objects::equals, xs, ys), (total, element) -> total && element, true) && (xs.hasNext() == ys.hasNext());

    }

    /**
     * Iterator의 요소의 값 뒤에 separator 기호가 더해진 형식, {@code seperator = ","}이면
     * [e1, e2, ..., en] 형식으로 {@code Iterator}의 문자열 표현을 반환합니다.
     * <br>{@code Iteraotr} 소진된 상태로 유지.
     * <br>hasNext() 메서드는 false를 반환.
     * @param <T> Iterator es 타입
     * @param es 문자열을 만들 요소를 들고 있는 Iterator
     * @param separator Iterator 요소를 분리해주는 문자 기호
     * @throws NullPointerException {@code Iterato} Null인 경우
     * @throws NullPointerException {@code separator} Null인 경우
     */
    public static <T> String toString(Iterator<T> es, String separator) { // TODO: reduce를 써서
        checkNotNull(es);
        checkNotNull(separator);

        return reduce(es, (x, y) -> {
            x.append(y).append(separator);
            return x;
        }, new StringBuilder().append(es.next())).toString();
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

    /**
     * {@code predicate}의 조건에 맞는 {@code iterator}의 요소들만 반환
     *
     * @throws NullPointerException iterator가 null인 경우
     * @throws NullPointerException predicate가 null인 경우
     */
    public static <E> Iterator<E> filter(Iterator<E> iterator, Predicate<E> predicate) {

        // TODO: Bug를 찾을 수 있는 test code를 IteratorTest.filterTest에 쓰고, Bug 고치기
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

    /**
     * {@code predicate}의 조건에 맞는 {@code iterator}의 첫 번째 요소의 값을 반환
     *
     * @throws NullPointerException iterator가 null 인 경우
     * @throws NullPointerException predicate가 null 인 경우
     */
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
     * 초기값이 seed
     * 무한한 순열을 내뱉는다. limit
     * seed의 값은 null이 들어와도 Functional Interface의 식에 따라서 null이 안나올수도 있으므로
     * 예시로 {iterate(null, x-> 1)}를 하면 1의 값이 나옴
     * seed 값에 대한 Null check는 하지 않음
     * limit() 사용하지않으면 hashNext가 true이므로 무한인피니티가 됨
     * 무한으로 순차적으로 한다
     *
     * @param seed 처음 초기값
     * @param f    Functional Interface
     */
    public static <T> Iterator<T> iterate(T seed, UnaryOperator<T> f) {
        checkNotNull(f, "iterate UnaryOperator NullPointException");
        return new Iterator<T>() {
            T current = seed;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                T old = current;
                current = f.apply(current);
                return old;
            }

        };
    }


    /**
     *
     */
    public static <T> Iterator<T> iterate(T seed, Predicate<T> predicate, UnaryOperator<T> f) {
        checkNotNull(f, "iterate UnaryOperator NullPointException");
        return new Iterator<T>() {
            T current = seed;

            @Override
            public boolean hasNext() {
                return predicate.test(current);
            }

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
     * </p>
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

    /**
     * 사용시 limit 사용 해주세요, 안쓰면
     */
    public static <T> Iterator<T> generate(Supplier<T> supplier) { // TODO:
        checkNotNull(supplier);
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

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


