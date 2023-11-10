package org.example.jminsu.functional.test;

import com.tip.functional.Iterators;
import com.tip.functional.Range;
import java.util.ArrayList;
import java.util.List;
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
}
