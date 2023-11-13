package org.example.jminsu.functional.test;

import static java.util.Arrays.asList;
import static org.example.jminsu.Mathx.fibonacci;
import static org.example.jminsu.functional.Iterators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.example.jminsu.Mathx;
import org.example.jminsu.functional.InfiniteIterator;
import org.example.jminsu.functional.Iterators;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IteratorsTest {

    @Test
    @DisplayName("사이즈 0 테스트")
    void testSize0() {
        Iterator<Integer> iterator = Collections.emptyIterator();
        assertEquals(0, Iterators.count(iterator));
    }

    @Test
    @DisplayName("사이즈 1 테스트")
    void testSize1() {
        Iterator<Integer> iterator = Collections.singleton(0).iterator();
        assertEquals(1, Iterators.count(iterator));
    }

    @Test
    @DisplayName("부분 테스트")
    void testSize_partiallyConsumed() {
        Iterator<Integer> iterator = asList(1, 2, 3, 4, 5).iterator();
        iterator.next();
        iterator.next();
        assertEquals(3, Iterators.count(iterator));
    }

    @Test
    @DisplayName("non null contain 테스트(true)")
    void test_contains_nonnull_yes() {
        Iterator<String> set = asList("a", null, "b").iterator();
        assertTrue(Iterators.toList(set).contains("b"));
    }

    @Test
    @DisplayName("non null contain 테스트(false)")
    void test_contains_nonnull_no() {
        Iterator<String> set = asList("a", null, "b").iterator();
        assertFalse(Iterators.toList(set).contains("c"));
    }

    @Test
    @DisplayName("null contain 테스트(true)")
    void test_contains_null_yes() {
        Iterator<String> set = asList("a", null, "b").iterator();
        assertTrue(Iterators.toList(set).contains(null));
    }

    @Test
    @DisplayName("null contain 테스트(false)")
    void test_contains_null_no() {
        Iterator<String> set = asList("a", "b").iterator();
        assertFalse(Iterators.toList(set).contains(null));
    }


    @Test
    @DisplayName("Iterable Reduce 테스트")
    void iterableReduceTest() {
        assertEquals(Iterators.reduce(Arrays.asList(1, 2, 3, 4, 5), (x, y) -> x + y, 0D), 15);
    }

    @Test
    @DisplayName("Iterator Reduce 테스트")
    void iteratorReduceTest() {
        assertEquals(Iterators.reduce(Arrays.asList(1, 2, 3, 4, 5).iterator(), (x, y) -> x + y, 0D), 15);
    }

    @Test
    @DisplayName("Iterable empty Reduce 테스트")
    void iterableEmptyReduceTest() {
        assertEquals(Iterators.reduce(Arrays.asList(1, null, 5), (x, y) -> x + y, 0D), 6);
    }

    @Test
    @DisplayName("Iterator empty Reduce 테스트")
    void iteratorEmptyReduceTest() {
        assertEquals(Iterators.reduce(Arrays.asList(1, null, 2).iterator(), (x, y) -> x + y, 0D), 3);
    }

    @Test
    @DisplayName("equals Test")
    void equalsTest() {
        Iterator<Integer> iter1 = asList(1, 2, 3, 4, 5).iterator();
        Iterator<Integer> iter2 = asList(1, 2, 3, 4, 5).iterator();

        assertTrue(Iterators.equals(iter1, iter2));
    }

    @Test
    @DisplayName("get Test")
    void getTest() {
        Iterator<Integer> iter1 = asList(1, 2, 3, 4, 5).iterator();
        Iterator<Integer> iter2 = asList(1, 2, 3, 4, 5, 6).iterator();

        Iterators.zip(Object::equals, iter1, iter2);
    }

    @Test
    @DisplayName("equals null Test")
    void equalsNullTest() {
        Iterator<Object> iter1 = asList().iterator();
        Iterator<Object> iter2 = null;
        assertThrows(NullPointerException.class, () -> Iterators.equals(iter1, iter2));

    }

    @Test
    @DisplayName("toString Test")
    void toStringTest() {
        String expectedValue = "1,2,3,4,5";
        assertTrue(Iterators.toString(limit(iterate(1, x -> x + 1), 5), ",").equals(expectedValue));
    }

    @Test
    @DisplayName("toString empty Test")
    void toStringEmptyTest() {
        Iterator<Object> iter = asList().iterator();
        String expectedValue = "";

        assertTrue(Iterators.toString(iter, ",").equals(expectedValue));
    }

    @Test
    @DisplayName("map Test")
    void mapTest() {
        assertTrue(
                Iterators.equals(
                        limit(map(iterate(1, x -> x + 1), x -> x * 2
                        ), 5)

                        , Stream.iterate(1, x -> x + 1)
                                .map(x -> x * 2)
                                .limit(5)
                                .iterator()));
    }

    @Test
    @DisplayName("limit, count 테스트")
    void limitCountTest() {
        assertEquals(5L, Iterators.count(limit(iterate(1, x -> x + 1), 5)));
    }

    @Test
    @DisplayName("generate 테스트")
    void generateTest() {
        Iterators.println(limit(Iterators.generate(Mathx::randInt), 5));
    }

    @Test
    @DisplayName("findFirst 테스트")
    void findFirstTest() {
        assertEquals(Iterators.findFirst(limit(iterate(1, x -> x + 1), 5), x -> x % 2 == 0), 2);
    }

    @Test
    @DisplayName("toList 테스트")
    void toListTest() {
        assertTrue(Iterators.toList(limit(iterate(1, x -> x + 1), 5))
                .equals(
                        Stream.iterate(1, x -> x + 1)
                                .limit(5)
                                .collect(Collectors.toList()))
        );
    }

    @Test
    @DisplayName("fibonacciTest")
    void fibonacciTest() {
        assertTrue(fibonacci() instanceof InfiniteIterator);


        Iterable<Integer> fib = Mathx::fibonacci;

        assertTrue(Iterators.equals(limit(fibonacci(), 10)
                , StreamSupport
                        .stream(fib.spliterator(), false)
                        .limit(10)
                        .iterator()));

    }

    @Test
    void filterTest() {


        assertTrue(
                Iterators.equals(
                        limit(filter(iterate(1, x -> x + 1), (x -> x % 2 == 0)), 10)
                        , Stream.iterate(1, x -> x + 1)
                                .filter(x -> x % 2 == 0)
                                .limit(10)
                                .iterator()));
        assertTrue(
                Iterators.equals(
                        limit(filter(iterate(0, x -> x), (x -> x % 2 == 0)), 11)
                        , Stream.iterate(0, x -> x)
                                .filter(x -> x % 2 == 0)
                                .limit(11)
                                .iterator()));
    }

    @Test
    @DisplayName("통합 테스트")
    public void iterateTest() {
        assertTrue(!Iterators.equals(
                limit(iterate(1, x -> x + 1), 10),

                Stream.iterate(1, x -> x + 1)
                        .limit(5)
                        .iterator()));

        assertTrue(
                Iterators.equals(
                        limit(iterate(1, x -> x + 1), 9),
                        Stream.iterate(1, x -> x + 1)
                                .limit(9)
                                .iterator()));

        assertEquals(
                Iterators
                        .toString(limit(iterate(1, x -> x + 1), 10)
                                , " "),


                Stream.iterate(1, x -> x + 1)
                        .limit(10)
                        .map(String::valueOf)
                        .reduce((x, y) -> x + " " + y).orElse(""));

        assertEquals(Iterators.toString(
                        limit(
                                iterate(1, x -> x + 1), 10), ","),
                Stream.iterate(1, x -> x + 1)
                        .limit(10)
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));

    }
    @Test
    public void equalTest() {
        Iterator iter1 = limit(iterate(1, x -> x + 1), 10);

        Iterator iter2 = limit(iterate(1, x -> x + 1), 10);

        assertEquals(Iterators.equals(iter1, iter1), true);
    }
//    @Test
//    public void testTest() {
//        Stream.iterate(1, x -> x + 1).forEach(System.out::println);
//
//    }


}
