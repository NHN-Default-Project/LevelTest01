package org.example.pminsu.functional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Fibonacci implements InfiniteIterator<BigInteger> {
    // TODO: 채우기
    /**
     * 피보나치 시작 값은 0부터 시작이므로 num의 값을 0으로 시작
     * '
     */
    private final BigInteger num = new BigInteger("0");
    private final Map<BigInteger, BigInteger> fibonacciMap;

    public Fibonacci() {
        fibonacciMap = new HashMap<>();
    }

    public BigInteger fibonnaci(BigInteger num) {
        if (num.equals(BigInteger.ZERO)) {
            fibonacciMap.put(BigInteger.ZERO, BigInteger.ONE);
            return BigInteger.ZERO;
        } else if (num.equals(BigInteger.ONE)) {
            fibonacciMap.put(BigInteger.ONE, BigInteger.ONE);
            return BigInteger.ONE;
        }
        if (fibonacciMap.get(num) == null) {
            fibonacciMap.put(num, fibonnaci(num.subtract(BigInteger.ONE)).add(fibonnaci(num.subtract(BigInteger.TWO))));
        }
        return fibonacciMap.get(num);
    }

    @Override
    public BigInteger next() {

        return fibonnaci(num.add(BigInteger.ONE));
    }


}
