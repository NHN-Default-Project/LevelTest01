package org.example.jminsu.functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.example.jminsu.Mathx;

public class Experiments<T extends Number> implements Iterator<T> {
    private Iterator<T> iterator;

    private String herbAvailabilities;
    private String binomialDistribution;

    private List<T> result;


    public Experiments(Iterator<T> iterator, String herbAvailabilities,
                       String binomialDistribution) {
        this.iterator = iterator;
        this.herbAvailabilities = herbAvailabilities;
        this.binomialDistribution = binomialDistribution;
        this.result = new LinkedList<>();
    }


    // TODO: 채우기
    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public T next() {
        T temp = this.iterator.next();
        result.add(temp);
        return temp;
    }

    public void report() {
        System.out.println("허브 유효도 : " + this.herbAvailabilities);
        System.out.println("분산 : " + this.binomialDistribution);
        System.out.println(Mathx.sum(result.iterator()) / result.size());
    }
}
