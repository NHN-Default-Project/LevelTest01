package org.example.jminsu.functional.test2;

import static org.example.jminsu.Mathx.fibonacci;
import static org.example.jminsu.functional.Iterators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.example.jminsu.Mathx;
import org.example.jminsu.functional.InfiniteIterator;
import org.example.jminsu.functional.Iterators;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class IteratorsTest {

    @Test
    @DisplayName("reduce parameter iterator and iterable Precondition Test")
    void reducePreconditionTest() {
        //precondition
        assertThrows(NullPointerException.class, () -> reduce((Iterable) null, (x, y) -> x, 1));
        assertThrows(NullPointerException.class, () -> reduce((Iterator) null, (x, y) -> y.toString(), ""));

        assertThrows(NullPointerException.class, () -> reduce(Collections.emptyIterator(), null, ""));
        assertThrows(NullPointerException.class, () -> reduce(Collections.emptyIterator(), (x, y) -> x, null));

        Iterable<Integer> iterable = Collections.emptyList();

        assertThrows(NullPointerException.class, () -> reduce(iterable, null, ""));
        assertThrows(NullPointerException.class, () -> reduce(iterable, (x, y) -> x, null));
    }

    @Test
    @DisplayName("reduce Iterator Value1 Test")
    void reduceIteratorValue1Test() {
        Iterator<Integer> iterator = Collections.singletonList(1).iterator();
        assertEquals(reduce(iterator, Integer::sum, 0),
                Stream.of(1).reduce(0, Integer::sum));
    }

    @Test
    @DisplayName("reduce Iterator Many Value Test")
    void reduceIteratorManyValueTest() {
        Iterator<Integer> iterator = Stream.of(1, 2, 3).iterator();
        assertEquals(reduce(iterator, Math::addExact, 0),
                Stream.of(1, 2, 3).reduce(0, Math::addExact));
    }

    @Test
    @DisplayName("reduce Iterable Many Value")
    void reduceIterableManyValue() {
        Iterable<Integer> iterable = Stream.of(1, 2, 3).collect(Collectors.toList());
        assertEquals(reduce(iterable, Math::addExact, 0),
                Stream.of(1, 2, 3).reduce(0, Math::addExact));
    }

    @Test
    @DisplayName("equals precondtion")
    void eqaulsPreconditionTest() {
        assertThrows(NullPointerException.class, () -> Iterators.equals(null,
                Stream.iterate(1, x -> x + 1).limit(10).iterator()));
        assertThrows(NullPointerException.class,
                () -> Iterators.equals(Stream.iterate(1, x -> x + 1).limit(10).iterator(),
                        null));
    }

    @Test
    public void testEqualsNotMatchLength() {
        Iterator<Integer> firstIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> secondIterator = Stream.iterate(1, x -> x + 1).limit(4).iterator();
        assertFalse(Iterators.equals(firstIterator, secondIterator));
    }

    @Test
    public void testEqualsNotMatchElement() {
        Iterator<Integer> firstIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> secondIterator = Stream.iterate(4, x -> x + 1).limit(3).iterator();
        assertFalse(Iterators.equals(firstIterator, secondIterator));
    }

    @Test
    public void testEqualsNotMatchOrder() {
        Iterator<Integer> firstIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> secondIterator = Stream.iterate(3, x -> x - 1).limit(3).iterator();
        assertFalse(Iterators.equals(firstIterator, secondIterator));
    }

    @Test
    @DisplayName("equals Value 1 True test")
    void equalsValue1TrueTest() {
        assertTrue(Iterators.equals(List.of(1).iterator(), Stream.of(1).iterator()));
    }

    @Test
    @DisplayName("equals Many Value True test")
    void equalsManyValueTrueTest() {
        assertTrue(Iterators.equals(List.of(1, 2, 3).iterator(),
                Stream.of(1, 2, 3).iterator()));
    }

    @Test
    @DisplayName("equals Empty Iterator Test")
    void eqaulsEmptyIteratorTest() {
        List<Object> list = new ArrayList();
        list.add(null);
        Iterator<Object> nullIterator = list.iterator();
        assertTrue(Iterators.equals(Collections.emptyIterator(),
                Collections.emptyIterator()));
        assertFalse(Iterators.equals(Collections.emptyIterator(), nullIterator));
    }

    @Test
    public void toStringTest() {
        List<String> list = List.of("jaehyeon", "is", "best");

        Iterator<String> iterator = list.iterator();

        assertTrue(iterator.toString() instanceof String);
        assertThrows(NullPointerException.class, () -> Iterators.toString(null, " "));
        assertThrows(NullPointerException.class,
                () -> Iterators.toString(iterator, null));

        String result = String.join(" ", list);
        assertEquals(result, Iterators.toString(iterator, " "));
    }

    @Test
    public void testToStringSimple() {
        Iterator<String> firstTargetIterator = List.of("jae", "is", "hyeon").iterator();
        String firstExpected = "jae is hyeon";
        String firstActual = Iterators.toString(firstTargetIterator, " ");

        Iterator<String> secondTargetIterator = List.of("jae", "is", "hyeon").iterator();
        String secondExpected = "jae,is,hyeon";
        String secondActual = Iterators.toString(secondTargetIterator, ",");

        assertEquals(firstExpected, firstActual);
        assertEquals(secondExpected, secondActual);
    }

    @Test
    public void testToStringWithNull() {
        List<String> targetList = new ArrayList<>();
        targetList.add("jae");
        targetList.add(null);
        targetList.add("hyeon");

        Iterator<String> targetIterator = targetList.iterator();

        String expected = "jae|null|hyeon";
        String actual = Iterators.toString(targetIterator, "|");

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringEmpty() {
        Iterator<String> targetIterator = Collections.<String>emptyList().iterator();
        String expected = "";
        String actual = Iterators.toString(targetIterator, "|");

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringNotMatch() {
        Iterator<String> firstTargetIterator = List.of("jae", "is", "hyeon").iterator();
        String firstExpected = "jae.is hyeon";
        String firstActual = Iterators.toString(firstTargetIterator, " ");

        assertNotEquals(firstExpected, firstActual);
    }

    @Test
    public void mapTest() {
        Iterator<Integer> integerIterator = List.of(1, 2, 3).iterator();
        assertTrue(map(integerIterator, (x) -> x) instanceof Iterator);
        assertThrows(NullPointerException.class, () -> map(null, (x) -> x));
        assertThrows(NullPointerException.class, () -> map(integerIterator, null));
    }

    @Test
    @DisplayName("map After calling iterator, call next Method Test")
    void mapAfterCallIteratorCallNextMethodTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        map(iterator, x -> 1.0).next();
        assertThrows(NoSuchElementException.class, () -> map(iterator, x -> 1.0).next());
    }

    @Test
    public void testMapSimple() {
        Iterator<Integer> beforeMap = List.of(1, 2, 3).iterator();
        List<Integer> expected = List.of(2, 3, 4);

        assertEquals(toList(map(beforeMap, (x) -> x + 1)), expected);
    }

    @Test
    public void testMapTypeCasting() {
        Iterator<String> beforeMap = List.of("1", "2", "3").iterator();
        List<Integer> expected = List.of(1, 2, 3);

        assertEquals(toList(map(beforeMap, ((x) -> Integer.parseInt(x)))), expected);
    }

    @Test
    public void filterTest() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertTrue(filter(list.iterator(), x -> x % 2 == 0) instanceof Iterator);
        assertThrows(NullPointerException.class, () -> filter(null, Objects::isNull));

        assertThrows(NullPointerException.class, () -> filter(list.iterator(), null));


        Iterator<Integer> iterator = filter(list.iterator(), x -> x % 2 == 1);
        Iterator<Integer> result = Stream.iterate(1, x -> x + 1).limit(10).filter(x -> x % 2 == 1).iterator();
        assertTrue(Iterators.equals(iterator, result));


        List<Integer> emptyList = new ArrayList<>();
        Iterator<Integer> emptyIterator = emptyList.iterator();
        assertTrue(Iterators.equals(filter(iterator, x -> x > 10), emptyIterator));


        assertTrue(filter(Iterators.limit(
                        Iterators.iterate(1, x -> x + 1), 10),
                x -> x % 2 == 1) instanceof Iterator);
        assertTrue(Iterators.equals(filter(
                       Iterators.limit(
                                Iterators.iterate(1, x -> x + 1), 100), x -> x % 2 == 1),
                filter(Stream.iterate(1, x -> x + 1).limit(100).iterator(), x -> x % 2 == 1)));
    }

    @Test
    @DisplayName("filter After calling iterator, call next Test")
    void filterAfterCallIteratorCallNextMethodTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        filter(iterator, x -> x % 2 == 1).next();
        assertThrows(NoSuchElementException.class, () -> filter(iterator, x -> x % 2 == 1).next());
    }

    @Test
    public void testFilterSimple() {
        Iterator<String> beforeFilter = List.of("jae", "hyeon").iterator();
        Iterator<String> afterFilter = filter(beforeFilter, Predicate.isEqual("hyeon"));
        List<String> expected = Collections.singletonList("hyeon");
        List<String> actual = toList(afterFilter);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterNoMatch() {
        Iterator<Integer> beforeFilter = List.of(1, 2, 3, 4, 5).iterator();
        Iterator<Integer> afterFilter = filter(beforeFilter, Predicate.isEqual(0));
        List<Integer> expected = Collections.emptyList();
        List<Integer> actual = toList(afterFilter);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterAllMatch() {
        Iterator<Double> beforeFilter = List.of(1.1, 2.3, 5.2, 0.1).iterator();
        Iterator<Double> afterFilter = filter(beforeFilter, x -> x > 0);
        List<Double> expected = List.of(1.1, 2.3, 5.2, 0.1);
        List<Double> actual = toList(afterFilter);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterEmpty() {
        Iterator<Integer> beforeFilter = new ArrayList<Integer>().iterator();
        Iterator<Integer> afterFilter = filter(
                beforeFilter,
                new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        throw new AssertionFailedError("Should never be evaluated");
                    }
                });
        List<Integer> expected = Collections.emptyList();
        List<Integer> actual = toList(afterFilter);
        assertEquals(expected, actual);
    }

    @Test
    public void findFirstTest() {
        Iterator<Integer> iterator = Collections.singletonList(1).iterator();
        assertThrows(NullPointerException.class, () -> findFirst(null, Objects::isNull));
        assertThrows(NullPointerException.class, () -> findFirst(iterator, null));
    }

    @Test
    public void testFindFirstSimple() {
        Iterator<Integer> iterator = List.of(1, 2, 3, 4, 5).iterator();
        Integer expected = 2;
        Integer actual = findFirst(iterator, x -> x % 2 == 0);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindFirstNotFound() {
        Iterator<Integer> iterator = List.of(1, 2, 3, 4, 5).iterator();
        assertEquals(null, findFirst(iterator, x -> x > 6));
    }

    @Test
    public void testFindFirstEmpty() {
        Iterator<Integer> iterator = Collections.emptyIterator();
        Integer actual = findFirst(
                iterator,
                new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        throw new AssertionFailedError("Should never be evaluated");
                    }
                }
        );
        assertEquals(null, actual);
    }

    @Test
    public void testIterate() {
        assertTrue(Iterators.iterate(1,
                x -> x + 1) instanceof InfiniteIterator);
        assertThrows(NullPointerException.class, () -> iterate(1, null));
    }

    @Test
    public void iterateTest() {
        assertTrue(iterate(1,
                x -> x + 1) instanceof InfiniteIterator);
        assertTrue((Iterators.limit(
                iterate(1, x -> x + 1),
                10) instanceof InfiniteIterator));
        assertTrue(!Iterators.equals(limit(
                        iterate(1, x -> x + 1), 10),
                Stream.iterate(1, x -> x + 1).limit(5).iterator()));
        assertTrue(Iterators.equals(limit(
                        iterate(1, x -> x + 1), 10),
                Stream.iterate(1, x -> x + 1).limit(10).iterator()));
        assertEquals(Iterators.toString(limit(
                        iterate(1, x -> x + 1), 10), " "),
                Stream.iterate(1, x -> x + 1).limit(10).map(String::valueOf)
                        .reduce((x, y) -> x + " " + y).orElse(""));
        assertEquals(Iterators.toString(limit(
                        iterate(1, x -> x + 1), 10), ","),
                Stream.iterate(1, x -> x + 1).limit(10).map(String::valueOf)
                        .collect(Collectors.joining(",")));
    }

    @Test
    public void limitTest() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        long maxSize = 3;

        assertTrue(limit(list.iterator(), maxSize) instanceof Iterator);
        assertThrows(IllegalArgumentException.class,
                () -> limit(null, maxSize));
        assertThrows(IllegalArgumentException.class,
                () -> limit(list.iterator(), -1));


        Iterator<Integer> limitIterator = limit(list.iterator(), maxSize);
        int count = 0;
        while (limitIterator.hasNext()) {
            count++;
            limitIterator.next();
        }

        assertEquals(count, maxSize);

        Iterator<Integer> listIterator = list.iterator();
        List<Integer> result = List.of(1, 2, 3);
        Iterator<Integer> resultIterator = result.iterator();

        assertTrue(
                Iterators.equals(resultIterator,
                        limit(listIterator, maxSize)));
    }


    @Test
    public void testLimitSimple() {
        Iterator<Integer> beforeLimitIterator = List.of(1, 2, 3, 4, 5).iterator();
        Iterator<Integer> afterLimitIterator = limit(beforeLimitIterator, 3);
        List<Integer> expected = List.of(1, 2, 3);
        List<Integer> actual = toList(afterLimitIterator);

        assertEquals(expected, actual);
    }

    //maxSize가 iterator의 사이즈 보다 클때
    @Test
    public void testLimitResultEmpty() {
        Iterator<Integer> beforeLimitIterator = List.of(1, 2, 3, 4, 5).iterator();
        Iterator<Integer> afterLimitIterator = limit(beforeLimitIterator, 0);
        List<Integer> expected = Collections.emptyList();
        List<Integer> actual = toList(afterLimitIterator);

        assertEquals(expected, actual);
    }

    @Test
    public void generateTest() {
        assertTrue(generate(() -> Integer.MAX_VALUE) instanceof InfiniteIterator);
        assertThrows(NullPointerException.class, () -> generate(null));
    }

    @Test
    public void testGenerateSimple() {
        Object expected = null;
        Object actual = limit(generate(() -> null), 1).next();
        assertEquals(expected, actual);
    }

    @Test
    public void zipTest() {
        Iterator<Integer> iterator1 = List.of(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = List.of(4, 2, 3).iterator();

        assertTrue(zip(Integer::equals, iterator1, iterator2) instanceof Iterator);
        assertThrows(NullPointerException.class, () -> zip(null, iterator1, iterator2));
        assertThrows(NullPointerException.class, () -> zip(Integer::equals, null, iterator2));
        assertThrows(NullPointerException.class, () -> zip(Integer::equals, iterator1, null));
    }

    @Test
    @DisplayName("zip next Method Over Call Test")
    void zipNextMethodOverCallTest() {
        Iterator<Integer> xIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> yIterator = List.of(1, 2, 3, 4).iterator();

        while (zip(Math::addExact, xIterator, yIterator).hasNext()) {
            zip(Math::addExact, xIterator, yIterator).next();
        }
        assertThrows(NoSuchElementException.class, () -> zip(Math::addExact, xIterator, yIterator).next());
    }

    @Test
    public void testZipSimple() {
        Iterator<Integer> firstIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> secondIterator = List.of(4, 2, 3).iterator();

        Iterator<Boolean> expected = List.of(false, true, true).iterator();
        Iterator<Boolean> actual = zip(Integer::equals, firstIterator, secondIterator);

        assertTrue(Iterators.equals(expected, actual));
    }

    @Test
    @DisplayName("zip different Length Iterator Test")
    void zipDifferentLengthIteratorTest() {
        Iterator<Integer> xIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> yIterator = List.of(1, 2, 3, 4).iterator();
        Iterator<Integer> resultIterator = List.of(2, 4, 6).iterator();

        while (zip(Math::addExact, xIterator, yIterator).hasNext()) {
            assertEquals(zip(Math::addExact, xIterator, yIterator).next(), resultIterator.next());
        }
    }

    @Test
    public void testZipEmpty() {
        Iterator<Integer> firstIterator = Collections.emptyIterator();
        Iterator<Integer> secondIterator = Collections.emptyIterator();
        Iterator<Boolean> expected = Collections.emptyIterator();
        Iterator<Boolean> actual = zip(Integer::equals, firstIterator, secondIterator);

        assertTrue(Iterators.equals(expected, actual));
    }

    @Test
    public void countTest() {
        assertThrows(NullPointerException.class, () -> count(null));
        assertThrows(ArithmeticException.class, () -> count(Stream.iterate(1, x -> x + 1).iterator()));
    }

    @Test
    public void testCountSimple() {
        assertEquals(count(Stream.iterate(1, x -> x + 1).limit(10).iterator()), 10L);
    }

    @Test
    public void testCountEmpty() {
        Iterator<Integer> targetIterator = Collections.emptyIterator();
        long expected = 0;
        long actual = count(targetIterator);

        assertEquals(expected, actual);
    }

    @Test
    public void testCountNextAfter() {
        Iterator<Integer> targetIterator = List.of(1, 2).iterator();
        targetIterator.next();

        long expected = 1;
        long actual = count(targetIterator);

        assertEquals(expected, actual);
    }

    @Test
    public void getTest() {
        Iterator<Integer> integerIterator = List.of(1).iterator();
        assertTrue(get(integerIterator, 1L) instanceof Integer);
        Iterator<String> stringIterator = List.of("a").iterator();
        assertTrue(get(stringIterator, 1L) instanceof String);

        assertThrows(IndexOutOfBoundsException.class, () -> get(integerIterator, -1L));
        assertThrows(NullPointerException.class, () -> get(null, 0L));
    }

    @Test
    public void testGetSimple() {
        Iterator<Integer> integerIterator = List.of(1, 2, 3).iterator();
        Integer expected = 1;
        Integer actual = get(integerIterator, 0L);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEmpty() {
        Iterator<Integer> integerIterator = Collections.emptyIterator();
        assertThrows(NoSuchElementException.class, () -> get(integerIterator, 0L));
    }

    @Test
    @DisplayName("get index is larger than iterator size Test")
    void getIndexLagerThanIteratorSizeTest() {
        assertEquals(get(List.of(1, 2, 3).iterator(), 10), get(List.of(1, 2, 3).iterator(), 2));
    }

    @Test
    public void toListTest() {
        Iterator<Integer> integerIterator = List.of(1).iterator();
        assertTrue(toList(integerIterator) instanceof List);
        assertThrows(NullPointerException.class, () -> toList(null));
    }

    @Test
    public void testToListSimple() {
        Iterator<Integer> integerIterator = List.of(1, 2).iterator();
        List<Integer> expected = List.of(1, 2);
        List<Integer> actual = toList(integerIterator);

        assertEquals(expected, actual);
    }

    @Test
    public void testToListEmpty() {
        Iterator<Integer> integerIterator = Collections.emptyIterator();
        List<Integer> expected = Collections.emptyList();
        List<Integer> actual = toList(integerIterator);

        assertEquals(expected, actual);
    }

    @Test
    public void fibonacciTest() {
        assertTrue(fibonacci() instanceof InfiniteIterator);
        Iterable<Integer> fib = Mathx::fibonacci;
        assertTrue(Iterators.equals(
                limit(fibonacci(), 10),
                StreamSupport.stream(fib.spliterator(), false).limit(10).iterator()));
    }

    @Test
    @DisplayName("toList iterator Null Value Test")
    void toListIteratorNullValueTest() {
        Iterator<Object> iterator = Collections.singletonList(null).iterator();
        assertEquals(toList(iterator).toString(), "[null]");
    }

    @Test
    @DisplayName("toList Empty Iterator test")
    void toListEmptyIteratorTest() {
        assertEquals(toList(Collections.emptyIterator()).toString(), "[]");
    }

}