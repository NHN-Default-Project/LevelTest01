package org.example.main.functional.test;

import static org.example.main.Mathx.fibonacci;
import static org.example.main.functional.Iterators.iterate;
import static org.example.main.functional.Iterators.limit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.example.main.Mathx;
import org.example.main.functional.InfiniteIterator;
import org.example.main.functional.Iterators;
import org.junit.jupiter.api.Test;

public class IteratorsTest {

        @Test
        public void iterateTest() {
                assertTrue(iterate(1, x -> x + 1) instanceof InfiniteIterator);
                assertTrue(!(limit(iterate(1, x -> x + 1), 10) instanceof InfiniteIterator));
                assertTrue(!Iterators.equals(limit(iterate(1, x -> x + 1), 10),
                                Stream.iterate(1, x -> x + 1).limit(5).iterator()));
                assertTrue(Iterators.equals(limit(iterate(1, x -> x + 1), 10),
                                Stream.iterate(1, x -> x + 1).limit(10).iterator()));
                assertEquals(Iterators.toString(limit(iterate(1, x -> x + 1), 10), " "),
                                Stream.iterate(1, x -> x + 1).limit(10).map(String::valueOf)
                                                .reduce((x, y) -> x + " " + y).orElse(""));
                assertEquals(Iterators.toString(limit(iterate(1, x -> x + 1), 10), ","),
                                Stream.iterate(1, x -> x + 1).limit(10).map(String::valueOf)
                                                .collect(Collectors.joining(",")));

        }

        @Test
        void filterTest() {
                assertTrue(fibonacci() instanceof InfiniteIterator);
                Iterable<Integer> fib = Mathx::fibonacci;
                assertTrue(Iterators.equals(limit(fibonacci(), 10), StreamSupport
                                .stream(fib.spliterator(), false).limit(10).iterator()));

        }
}