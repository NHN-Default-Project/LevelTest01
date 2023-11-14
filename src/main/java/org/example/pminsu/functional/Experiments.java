package org.example.pminsu.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Experiments<T extends Number> implements Iterator<T> {
    // TODO: 채우기
    String herb;
    String distribution;
    Iterator<T> iterator;
    T data;
    List<T> iteratorList;

    public Experiments(Iterator<T> iterator, String herbQualities, String discreteUniformDistribition) {
        this.herb = herbQualities;
        this.distribution = discreteUniformDistribition;
        this.iterator = iterator;
        iteratorList = new ArrayList<T>();
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Experiments");
        }

        data = iterator.next();
        this.iteratorList.add(data);
        return data;
    }

    public void report() {
        System.out.println(Mathx.sum(iteratorList.iterator()) / this.iteratorList.size());
    }
//    public void report() {
//        System.out.println(Mathx.sum(iteratorList.iterator()) / this.iteratorList.size());
//    }


}
