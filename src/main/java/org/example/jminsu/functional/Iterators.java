package org.example.jminsu.functional;

import static org.example.main.Precondition.checkArgument;
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

    /**
     *
     * @param es : 피연산자들이 모인 Iterable 객체
     * @param biFunction : 파라미터 두개를 받아 하나의 결과를 내놓는 함수
     * @param init : 초기값. biFunction의 결과값 타입을 결정한다
     * @return : es에서 값을 받아와 Bifuction을 거친 결과값
     * @param <E> : biFunction의 피연산자 타입
     * @param <R> : biFunction의 결과 타입
     */
    public static <E, R> R reduce(Iterable<E> es, BiFunction<R, E, R> biFunction, R init) {
        checkNotNull(es, "reduce parameter Iterable value Null");
        checkNotNull(biFunction, "reduce parameter BiFunction value Null ");
        checkNotNull(init);

        R result = init;
        for (E e : es) {
            if (!Objects.isNull(e)) {
                result = biFunction.apply(result, e);
            }
        }
        return result;
    }

    /**
     *  Iterator를 위한 Method Overriding
     *  Iterator가 갖고 있는 타입이 Iterable이므로 람다식을 활용해 추출함
     *
     * @param es : 피연산자를 갖고 있는 Iterator
     * @param biFunction : 파라미터 두개를 받아 하나의 결과를 내놓는 함수
     * @param init : 초기값 biFunction의 결과값 타입을 결정한다
     * @return : Iterable Method Overriding reduce 재호출
     * @param <E> : biFunction의 피연산자 타입
     * @param <R> : biFunction의 결과 타입
     */
    public static <E, R> R reduce(Iterator<E> es, BiFunction<R, E, R> biFunction, R init) {
        checkNotNull(es, "reduce parameter iterator Null ");
        checkNotNull(biFunction, "reduce parameter BiFunction biFunction value Null");
        checkNotNull(init, "reduce parameter R init value Null");
        return reduce(() -> es, biFunction, init);
    }

    // TODO: reduce를 써서 check1

    /**
     *
     * @param xs 비교할 Iterator 인수1
     * @param ys 비교할 Iterator 인수2
     * @return Iterator 인수를 소모하면서 비교하여 두 Iterator가 같은지 출력
     * @param <T>
     *
     * xs와 ys의 주소값만을 비교하여 먼저 둘이 같은지를 판단한다.
     * 1. Equals나 hashcode는 객체에서 오버라이드하고 구현하여 의미가 달라질 수도 있다.
     * 2. Objects.equals 역시 객체의 Equal 메서드를 사용하므로 제외한다.
     * 주소값이 같다면 해당 equals의 의도에 맞게 Iterator를 소모한다.
     *
     */
    public static <T> boolean equals(Iterator<T> xs, Iterator<T> ys) {
        checkNotNull(xs);
        checkNotNull(ys);
        //둘의 주소값이 같다면 값을 소모시키고 true 출력
        if (xs == ys) {
            return reduce(xs, (total, element) -> true, true);

        } else {
            return (reduce(zip(Objects::equals, xs, ys), (total, element) -> total && element, true) &&
                    xs.hasNext() == ys.hasNext());
        }
    }

    // TODO: redude를 써서

    /**
     * iterator의 값을 separator로 구분하여 String을 리턴하는 메서드
     * @param es : toString을 적용할 값을 갖고 있는 Iterator
     * @param separator : 구분자
     * @return : separator로 Iterator의 값들을 구분한 String
     * @param <T> : Iterator의 타입
     *
     * StringBuilder를 활용하여 String 객체가 Iterator의 수만큼 늘어나는 것을 방지함
     */
    public static <T> String toString(Iterator<T> es, String separator) {
        checkNotNull(es);
        checkNotNull(separator);

        StringBuilder sb = new StringBuilder();

        if (es.hasNext()) {
            sb.append(es.next());
        }

        return reduce(es, (result, add) -> result.append(separator).append(add), sb).toString();
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
     *
     * @param iterator : 필터링을 적용할 Iterator
     * @param predicate : 필터링 방법을 의미하는 수식
     * @return : 필터링이 적용된 Iterator
     * @param <E>
     *
     * 연산 방식
     * - findFirst를 호출하여 미리 current의 값을 초기화 한다
     * - hasNext는 current가 null인지만 확인한다
     * - next는 현재 current를 리턴하고, findFirst로 해당 predicate를 만족하는 값이 있는 지를 체크한다
     */
    public static <E> Iterator<E> filter(Iterator<E> iterator, Predicate<E> predicate) {
        // TODO: Bug를 찾을 수 있는 test code를 IteratorTest.filterTest에 쓰고, Bug 고치기
        // findFirst를 써서 풀기
        checkNotNull(iterator);
        checkNotNull(predicate);
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

                E result = this.current;
                this.current = findFirst(iterator, predicate);

                return result;
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
        checkNotNull(seed, "iterate UnaryOperator NullPointException");
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
        checkNotNull(iterator);
        checkArgument(maxSize >= 0, "Limit NegativeNum");

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

    // TODO:

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

        // TODO: reduce를 써서
        return reduce(iterator, (x, y) -> Math.addExact(x, 1), 0);
    }

    public static <T> T get(Iterator<T> iterator, long index) {
        checkNotNull(iterator, "get Method Iterator Null");

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
        checkNotNull(iterator);
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <E> void print(Iterator<E> iterator, String separator,
                                 java.io.PrintStream printStream) {

        checkNotNull(iterator);
        checkNotNull(separator);
        checkNotNull(printStream);
        printStream.print(toString(iterator, separator));
    }

    public static <E> void print(Iterator<E> iterator, String separator) {
        checkNotNull(iterator);
        checkNotNull(separator);
        print(iterator, separator, System.out);
    }

    public static <E> void println(Iterator<E> iterator, String separator,
                                   java.io.PrintStream printStream) {
        checkNotNull(iterator);
        checkNotNull(separator);
        checkNotNull(printStream);

        print(iterator, separator, printStream);
        printStream.println();
    }

    public static <E> void println(Iterator<E> iterator, String separator) {
        checkNotNull(iterator);
        checkNotNull(separator);
        println(iterator, separator, System.out);
    }

    public static <E> void println(Iterator<E> iterator) {
        checkNotNull(iterator);
        println(iterator, ", ");
    }

    private Iterators() {
    }

}


