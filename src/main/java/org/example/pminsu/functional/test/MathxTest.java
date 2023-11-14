package org.example.pminsu.functional.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import org.example.pminsu.functional.Iterators;
import org.example.pminsu.functional.Mathx;
import org.junit.jupiter.api.Test;


public class MathxTest {
    @Test
    public void sumTest() {
        assertEquals(Mathx.sum(Arrays.asList()), IntStream.of().sum());
        assertEquals(Mathx.sum(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
                IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).sum());
        assertEquals(Mathx.sum(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).iterator()),
                Mathx.sum(IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).iterator()));
    }

    @Test
    public void productTest() {
        assertEquals(Mathx.product(Arrays.asList()), IntStream.of().reduce(1, (x, y) -> x * y));
        assertEquals(Mathx.product(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
                IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).reduce(1, (x, y) -> x * y));
        assertEquals(Mathx.product(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).iterator()),
                Mathx.product(IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).iterator()));

    }

    public static void randDoublesDemo() {
        assertTrue(Iterators.generate(Mathx::randDouble) instanceof Iterator);
        assertTrue(Iterators.generate(Mathx::randInt) instanceof Iterator);
        assertTrue(Mathx.randDoubles() instanceof Iterator);
        assertTrue(Mathx.randInts() instanceof Iterator);

        Iterators.println(Iterators.limit(Iterators.generate(Mathx::randDouble), 20));
        Iterators.println(Iterators.limit(Mathx.randDoubles(), 20));
        Iterators.println(new Random().doubles(20).iterator());
        Iterators.println(ThreadLocalRandom.current().doubles(20).iterator());
        Iterators.println(DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(20)
                .iterator());
    }

    @Test
    public void distributionTest() {
        assertTrue(Mathx.binaryDistribution(0.5) instanceof Iterator);
        assertTrue(Mathx.discreteUniformDistribution(1, 4) instanceof Iterator);
        assertTrue(Mathx.normalDistribution(90, 10) instanceof Iterator);
    }

    public static void main(String[] args) {
        randDoublesDemo();
    }
}
