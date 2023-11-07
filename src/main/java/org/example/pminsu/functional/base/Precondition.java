package org.example.pminsu.functional.base;


import java.util.Iterator;
import java.util.Objects;
import org.example.pminsu.functional.InfiniteIterator;
import org.example.pminsu.functional.exception.InfiniteIteratorException;

public class Precondition {


    /**
     * 제네릭 타입의 value의 값이 null인지 아닌지 체크하는 메서드
     * value의 값이 null인 경우 NullPointException을 던져줌
     *
     * @param value an Object value
     * @throws NullPointerException if {@code reference} is true
     */
    public static <T> void checkNotNull(T value) {
        if (Objects.isNull(value)) {
            throw new NullPointerException();
        }
    }

    /**
     * 제네릭 타입의 value의 값이 null인지 아닌지 체크하는 메서드
     * value의 값이 null인 경우 NullPointException을 던져줌
     *
     * @param value        an Object value
     * @param errorMessage the exception message to use if  the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws NullPointerException if {@code reference} is true
     */
    public static <T> void checkNotNull(T value, Object errorMessage) {
        if (Objects.isNull(value)) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
    }


    /**
     * 제네릭 타입의 value의 값이 null인지 아닌지 체크하는 메서드
     * value의 값이 null인 경우 NullPointException을 던져줌
     *
     * @param expression boolean expression
     * @throws IllegalArgumentException if {@code reference} is false
     */
    public static <T> void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 제네릭 타입의 value의 값이 null인지 아닌지 체크하는 메서드
     * value의 값이 null인 경우 NullPointException을 던져줌
     *
     * @param expression   boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code reference} is false
     */
    public static <T> void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static <T> void checkInfiniteIterator(Iterator<T> iterator) {
        if (iterator instanceof InfiniteIterator) {
            throw new InfiniteIteratorException();
        }
    }

    public static <T> void checkInfiniteIterator(Iterator<T> iterator, Object object) {
        if (iterator instanceof InfiniteIterator) {
            throw new InfiniteIteratorException(String.valueOf(object));
        }
    }
}
