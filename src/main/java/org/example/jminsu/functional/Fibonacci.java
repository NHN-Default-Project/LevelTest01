package org.example.jminsu.functional;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci implements InfiniteIterator<Integer> {

    Map<Integer, Integer> memorize = new HashMap<>();

    private int count = 0;
    private int fib1 = 0;
    private int fib2 = 1;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        if (memorize.get(count) == null) {
            int current = fib1;

            fib1 = fib2;
            fib2 = fib1 + current;
            memorize.put(count, current);
            count++;
            return current;
        } else {
            return memorize.get(count);
        }


    }
    // TODO: 채우기
}
