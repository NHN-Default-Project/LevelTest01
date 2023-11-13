package org.example.jminsu.functional.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.example.jminsu.functional.Iterators;
import org.example.jminsu.functional.Range;
import org.junit.jupiter.api.Test;

public class RangeTest {
    @Test
    public void rangeTest() {
        // TODO: 모든 기능을 고르게 테스트 할 수 있는 코드 적어보기
        Range range = new Range(10);
        assertEquals(range.min(), 1);
        assertEquals(range.max(), 9);
        assertEquals(range.size(), 9);
        assertEquals(range.size(), 9);

        List<Long> longList = new ArrayList<>();
        for (long i = 1; i < 10; i++) {
            longList.add(i);
        }

        Iterators.equals(range.iterator(), longList.iterator());
    }

    @Test
    public void maxValueTest() {
        Range range = new Range(Long.MAX_VALUE, Long.MAX_VALUE+1);

    }
}
