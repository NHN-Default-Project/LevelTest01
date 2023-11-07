package org.example.pminsu.functional.test;


import static org.example.pminsu.functional.Iterators.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.pminsu.functional.InfiniteIterator;
import org.example.pminsu.functional.Iterators;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IteratorsTest {
    //TODO reduceTest 작성하기
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

    //
    @Test
    @DisplayName("reduce Iterable Value1 Test")
    void reduceIterableValue1Test() {
        Iterable<Integer> iterable = Collections.singletonList(1);
        List<Object> list = new ArrayList();
        list.add(1);
        list.add(null);
        list.add(2);

    }

    @Test
    @DisplayName("reduce Iterable Many Value")
    void reduceIterableManyValue() {
        Iterable<Integer> iterable = Stream.of(1, 2, 3).collect(Collectors.toList());
        assertEquals(reduce(iterable, Math::addExact, 0),
                Stream.of(1, 2, 3).reduce(0, Math::addExact));
    }

    //TODO equals test code 작성
    @Test
    @DisplayName("equals precondtion")
    void eqaulsPreconditionTest() {
        assertThrows(NullPointerException.class, () -> Iterators.equals(null, Stream.iterate(1, x -> x + 1).limit(10).iterator()));
        assertThrows(NullPointerException.class, () -> Iterators.equals(Stream.iterate(1, x -> x + 1).limit(10).iterator(), null));
    }

    @Test
    @DisplayName("equals Empty Iterator Test")
    void eqaulsEmptyIteratorTest() {
        List<Object> list = new ArrayList();
        list.add(null);
        Iterator<Object> nullIterator = list.iterator();
        assertTrue(Iterators.equals(Collections.emptyIterator(), Collections.emptyIterator()));
        assertFalse(Iterators.equals(Collections.emptyIterator(), nullIterator));
    }

    @Test
    @DisplayName("equals Value 1 True test")
    void equalsValue1TrueTest() {
        assertTrue(Iterators.equals(List.of(1).iterator(), Stream.of(1).iterator()));
    }

    @Test
    @DisplayName("equals Many Value True test")
    void equalsManyValueTrueTest() {
        assertTrue(Iterators.equals(List.of(1, 2, 3).iterator(), Stream.of(1, 2, 3).iterator()));
    }

    @Test
    @DisplayName("equals false Test")
    void equalsFalseTest() {
        assertFalse(Iterators.equals(List.of(1, 2, 3).iterator(), Stream.of(1, 2).iterator()));
        assertFalse(Iterators.equals(List.of(1, 2, 3).iterator(), Stream.of(1, 2, 4).iterator()));
        assertFalse(Iterators.equals(List.of(1, 2, 3).iterator(), Stream.of(2, 2, 4).iterator()));
        Iterators.equals(Collections.emptyIterator(), Collections.emptyIterator());
    }

    //TODO toString Test
    @Test
    @DisplayName("toString Null Value Test")
    void toStringNullValueTest() {
        assertThrows(NullPointerException.class, () -> Iterators.toString(null, " "));
        assertThrows(NullPointerException.class, () -> Iterators.toString(Collections.emptyIterator(), null));
    }

    @Test
    @DisplayName("toString Test")
    void toStringTest() {
        assertEquals(Iterators.toString(List.of(1, 2, 3).iterator(), " "), "1 2 3");
    }

    @Test
    @DisplayName("toString Empty Iterator Test")
    void toStringEmptyIteratorTest() {

        assertEquals(Iterators.toString(Collections.emptyIterator(), ""), "");
    }

    @Test
    @DisplayName("map test")
    void mapTest() {
        Iterator<Integer> iterator = List.of(1, 2, 3, 4, 5, 6).iterator();
        while (iterator.hasNext()) {
            assertEquals(map(iterator, x -> "1.0").next(), "1.0");
        }
    }

    @Test
    @DisplayName("map Null Value test")
    void mapNullValueTest() {
        assertThrows(NullPointerException.class, () -> map(null, (x) -> x));
        assertThrows(NullPointerException.class, () -> map(Collections.emptyIterator(), null));
    }

    @Test
    @DisplayName("map Iterator Value Null Test")
    void mapIteratorValueNullTest() {
        assertThrows(NoSuchElementException.class, () -> map(Collections.emptyIterator(), x -> x).next());
    }

    @Test
    @DisplayName("map After calling iterator, call next Method Test")
    void mapAfterCallIteratorCallNextMethodTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        map(iterator, x -> 1.0).next();
        assertThrows(NoSuchElementException.class, () -> map(iterator, x -> 1.0).next());
    }

    @Test
    @DisplayName("map After calling iterator, call hashNext Method Test")
    void mapAfterCallIteratorCallHashNextMethodTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        map(iterator, x -> 1.0).next();
        assertFalse(map(iterator, x -> 1.0).hasNext());
    }

    @Test
    @DisplayName("filter Null Value Test")
    void filterNullValueTest() {
        assertThrows(NullPointerException.class, () -> filter(null, x -> x.equals("")));
        assertThrows(NullPointerException.class, () -> filter(Collections.singleton(1).iterator(), null));
    }

    @Test
    @DisplayName("filter Iterator Null Value Test")
    void filterIteratorNullValueTest() {
        Iterator<Integer> iterator = Collections.emptyIterator();
        assertTrue(filter(iterator, x -> x % 2 == 0) instanceof Iterator); // null value가 들어가도 에러는 안남
        assertThrows(NoSuchElementException.class, () -> filter(iterator, x -> x % 2 == 0).next()); // next를 했을 때 에러
    }

    @Test
    @DisplayName("filter Predicate True Condition Test")
    void filterPredicateTrueConditionTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        assertTrue(filter(iterator, x -> x % 2 == 1).hasNext());
    }

    @Test
    @DisplayName("filter Predicate False Condition Test")
    void filterPredicateFalseConditionTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        assertThrows(NoSuchElementException.class, () -> filter(iterator, x -> x % 2 == 0).next());
    }

    @Test
    @DisplayName("filter After calling iterator, call next Test")
    void filterAfterCallIteratorCallNextMethodTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        filter(iterator, x -> x % 2 == 1).next();
        assertThrows(NoSuchElementException.class, () -> filter(iterator, x -> x % 2 == 1).next());
    }

    @Test
    @DisplayName("filter After calling iterator, call hashNext Test")
    void filterAfterCallIteratorCallHashNextMethodTest() {
        Iterator<Integer> iterator = Collections.singleton(1).iterator();
        filter(iterator, x -> x % 2 == 1).next();
        assertFalse(filter(iterator, x -> x % 2 == 1).hasNext());
    }

    @Test
    @DisplayName("findFirst Predicate Condition Value True test")
    void findFirstPredicateConditionValueTrueTest() {
        Iterator<Integer> iterator = Collections.singletonList(1).iterator();
        assertEquals(findFirst(iterator, x -> true), 1);
    }

    @Test
    @DisplayName("findFirst Predicate Condition Value False Test")
    void findFirstPredicateConditionValueFalseTest() {
        Iterator<Integer> iterator = Collections.singletonList(1).iterator();
        assertEquals(findFirst(iterator, x -> false), null);
    }

    @Test
    @DisplayName("findFirst Predicate Condition Value null Test")
    void findFirstPredicateConditionValueNullTest() {
        assertThrows(NullPointerException.class, () -> findFirst(Collections.singletonList(1).iterator(), null));
    }

    @Test
    @DisplayName("findFirst Predicate Condition Match Test")
    void findFirstPredicateConditionMatchTest() {
        Iterator<Integer> iterator = List.of(1, 2, 3).iterator();
        assertEquals(findFirst(iterator, x -> x % 2 == 1), 1);
    }

    @Test
    @DisplayName("findFirst Predicate Condition Not Match Test")
    void findFirstPredicateConditionNotMatchTest() {
        Iterator<Integer> iterator = List.of(1, 3, 5).iterator();
        assertEquals(findFirst(iterator, x -> x % 2 == 0), null);
    }

    @Test
    @DisplayName("iterate Test")
    public void iterateTest() {
        assertTrue(iterate(1, x -> x + 1) instanceof InfiniteIterator);
    }

    @Test
    @DisplayName("iterate Parameter UnaryOperator Null Test")
    void iterateUnaryOperatorNullTest() {
        assertThrows(NullPointerException.class, () -> iterate(1, null));
    }

    @Test
    @DisplayName("iterate Parameter Generic Type Value Null Test")
    void iterateGenericTypeValueNullTest() {
        assertTrue(iterate(null, x -> x) instanceof InfiniteIterator);
    }

    @Test
    @DisplayName("limit maxSize Value 1 Test")
    void limitMaxSizeValue1Test() {
        Iterator<Integer> iterator = List.of(1, 2, 3, 4, 5).iterator();
        assertEquals(limit(iterator, 1).next(), Collections.singleton(1).iterator().next());
    }

    @Test
    @DisplayName("limit maxSize Value NegativeNum Test")
    void limitMaxSizeValueNegativeNumTest() {
        Iterator<Integer> iterator = Collections.emptyIterator();
        assertThrows(IllegalArgumentException.class, () -> limit(iterator, -1));
    }

    @Test
    @DisplayName("limit iterator value null Test")
    void limitIteratorValueNullTest() {
        assertThrows(NullPointerException.class, () -> limit(null, 10));
    }

    @Test
    @DisplayName("limit calling the next method to exceed the maximum size Test")
    void limitCallNextMethodToExceedTheMaxSizeTest() {
        Iterator<Integer> iterator = limit(List.of(1, 2, 3).iterator(), 2);
        iterator.next();
        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    @DisplayName("limit hashNext Method True Test")
    void limitHashNextTrueTest() {
        Iterator<Integer> iterator = limit(Collections.singleton(1).iterator(), 10);
        assertTrue(iterator.hasNext());
    }

    @Test
    @DisplayName("limit hashNext Method False Test")
    void limitHashNextFalseTest() {
        Iterator<Integer> maxSize0Iterator = limit(Collections.singleton(1).iterator(), 0);
        Iterator<Integer> emptyIterator = limit(Collections.emptyIterator(), 10);
        assertFalse(maxSize0Iterator.hasNext());
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    @DisplayName("generate Test")
    void generateTest() {
        assertTrue(generate(() -> true) instanceof InfiniteIterator);
        assertTrue(limit(generate(() -> true), 1).next());

    }

    @Test
    @DisplayName("generate Supplier Null Value Test")
    void generateSupplierNullValueTest() {
        assertThrows(NullPointerException.class, () -> generate(null));
    }

    @Test
    @DisplayName("generate Supplier get Method Return Value Null Test")
    void generateSupplierGetMethodReturnValueNullTest() {
        assertEquals(limit(generate(() -> null), 1).next(), null);
    }

    @Test
    @DisplayName("zip Same Length Iterator Test")
    void zipSameLengthTest() {
        Iterator<Integer> xIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> yIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> resultIterator = List.of(2, 4, 6).iterator();

        while (xIterator.hasNext()) {
            assertEquals(zip(Math::addExact, xIterator, yIterator).next(), resultIterator.next());
        }
    }

    @Test
    @DisplayName("zip null Value Test")
    void zipNullValueTest() {
        assertThrows(NullPointerException.class, () -> zip(null, Collections.emptyIterator(), Collections.emptyIterator()));
        assertThrows(NullPointerException.class, () -> zip(Object::equals, null, Collections.emptyIterator()));
        assertThrows(NullPointerException.class, () -> zip(Object::equals, Collections.emptyIterator(), null));
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
    @DisplayName("zip next Method Over Call Test")
    void zipNextMethodOverCallTest() {
        Iterator<Integer> xIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> yIterator = List.of(1, 2, 3, 4).iterator();

        while (zip(Math::addExact, xIterator, yIterator).hasNext()) {
            zip(Math::addExact, xIterator, yIterator).next();
        }
        assertThrows(NoSuchElementException.class, () -> zip(Math::addExact, xIterator, yIterator).next());
    }

    // 빈값의 iterator가 들어갔을 떄

    @Test
    @DisplayName("zip hashNext Method Over Call Test")
    void zipHashNextMethodOverCallTest() {
        Iterator<Integer> xIterator = List.of(1, 2, 3).iterator();
        Iterator<Integer> yIterator = List.of(1, 2, 3, 4).iterator();

        while (zip(Math::addExact, xIterator, yIterator).hasNext()) {
            zip(Math::addExact, xIterator, yIterator).next();
        }
        assertFalse(zip(Math::addExact, xIterator, yIterator).hasNext());
    }

    @Test
    @DisplayName("count Success Test")
    void countSuccessTest() {
        //정상, null, MaxValue 넘길때
        Iterator<Integer> iterator = List.of(1, 2, 3).iterator();
        assertEquals(count(iterator), List.of(2, 4, 6).size());
    }

    @Test
    @DisplayName("count Null Value Test")
    void countNullValueTest() {
        assertThrows(NullPointerException.class, () -> count(null));
    }

    // iterator가 비어있을 때
    @Test
    @DisplayName("count Long Over MaxValue Test")
    void countLongOverMaxValueTest() {
        assertThrows(ArithmeticException.class, () -> count(iterate(1, x -> x + 1)));
    }

    @Test
    @DisplayName("get Test")
    void getTest() {
        Iterator<Integer> iterator = List.of(1, 2, 3).iterator();
        assertEquals(get(iterator, 2), List.of(1, 2, 3).get(2));
    }

    @Test
    @DisplayName("get index NegativeNum Value Test")
    void getIndexNegativeNumValueTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> get(Collections.emptyIterator(), -1));
    }

    @Test
    @DisplayName("get Empty Iterator Test")
    void getEmptyIteratorTest() {
        assertThrows(NoSuchElementException.class, () -> get(Collections.emptyIterator(), 10));
    }

    @Test
    @DisplayName("get Iterator value Null Test")
    void getIteratorValueNullTest() {

        assertThrows(NullPointerException.class, () -> get(null, 10));

    }

    @Test
    @DisplayName("get index is larger than iterator size Test")
    void getIndexLagerThanIteratorSizeTest() {
        assertEquals(get(List.of(1, 2, 3).iterator(), 10), get(List.of(1, 2, 3).iterator(), 2));
    }

    @Test
    @DisplayName("toList Test")
    void toListTest() {
        Iterator<Integer> iterator = List.of(1, 2, 3).iterator();
        assertTrue(toList(iterator).equals(List.of(1, 2, 3)));

    }

    @Test
    @DisplayName("toList parameter Null Value Test")
    void toListNullValueTest() {
        assertThrows(NullPointerException.class, () -> toList(null));
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