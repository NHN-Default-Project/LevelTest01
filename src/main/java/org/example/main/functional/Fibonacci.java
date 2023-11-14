package org.example.main.functional;

public class Fibonacci implements InfiniteIterator<Integer> {

    private int n;
    private int prev;
    private int cur;
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        int temp = prev;
        prev = cur;
        return null;
    }
    // TODO: 채우기
}