package org.example.jaehyeon.functional;

public class Fibonacci implements InfiniteIterator<Integer> {

    private int prevCurrent = 0;
    private int current = 1;


    @Override
    public Integer next() {
        int tmp = prevCurrent;
        prevCurrent = current;
        current = tmp + current;
        return tmp;
    }
}
