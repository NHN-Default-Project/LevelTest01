package org.example.pminsu.functional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Fibonacci implements Iterator<Long> {
    // TODO: 채우기
    /**
     * 피보나치 시작 값은 0부터 시작이므로 num의 값을 0으로 시작
     * InfiniteIterator
     */
    private  Long num = 0L;
    private final Map<BigInteger, BigInteger> fibonacciMap;
    private final Map<Long, Long> fibonacci;

    public Fibonacci() {
        fibonacciMap = new HashMap<>();
        fibonacci = new HashMap<>();
    }



    public Long fibonnaci(Long num) {
        if (num.equals(0L)) {
            fibonacci.put(0L, 1L);
        } else if (num.equals(1L)) {
            fibonacci.put(1L, 1L);
        }
        if (fibonacci.get(num) == null) {
            fibonacci.put(num, fibonnaci(num - 1) + fibonnaci(num - 2));
        }
        return fibonacci.get(num);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Long next() {
        return fibonnaci(num++);
    }


}
