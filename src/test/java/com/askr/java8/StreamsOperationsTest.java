package com.askr.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StreamsOperationsTest {
    @Test
    public void testFilterCount() {
    // calculate number of aa occurrences
        long numAAs = Stream.of("aab", "aac", "abc").filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("aa");
            }
        }).count();
        assertEquals(2, numAAs);

        assertEquals(2,
                Stream.of("aab", "aac", "abc").filter(s -> s.contains("aa")).count());
    }

    @Test
    public void testIntStream() {
        // calculate the sum of x*x for any x > 2; x is an element in the sequence
        int result = IntStream.of(1, 2, 3, 4)
                .filter(e -> e > 2)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(e -> e * e)
                .peek((int e) -> System.out.println("Mapped value: " + e))
                .sum();
        assertEquals(3*3+4*4, result);
    }

    @Test
    public void testMapReducePeek() {
    // convert each string into number and calculate the sum
        int result = Stream.of("1","2","3").
                map(s -> Integer.parseInt(s)).
                peek(v -> System.out.println(v)).
                reduce((a, b) -> a + b).
                get();
        assertEquals(6, result);
    }

    @Test
    public void testDistinctToArray() { // get array of distinct integers
        Integer[] result = Stream.of(1,2,3,1,2,3,1,1,4).distinct().toArray(Integer[]::new);
        assertEquals(4, result.length);
        assertArrayEquals(new Integer[]{1,2,3,4}, result);
    }

    @Test
    public void testFilterMapFindFirst() {
        // get first "1" and concatenate to itself;
        // check that processing ends once first "1" is found
        AtomicInteger numOfEqualities = new AtomicInteger(0);
        String oneOne = Stream.of("-1", "0", "1", "1", "2", "1", "3").
                filter(v -> {
                    System.out.println(v + " takes place");
                    if (v.equals("1")) {
                        numOfEqualities.incrementAndGet();
                        return true;
                    }
                    return false;
                }).
                map(str -> {
                    System.out.println("mapping: " + str);
                    return str.concat(str);
                }).
                findFirst().
                orElseGet(() -> "");
        assertEquals(1, numOfEqualities.get());
        System.out.println(oneOne);
        assertEquals("11", oneOne);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    private long arithProgressionSum(long a1, long an, long n) {
        return n*(a1+an)/2;
    }

    @Test
    public void testParallelStream() {
        final long MAX_VAL = 50_000_000;
        List<Long> list = new ArrayList<>();
        LongStream.range(1, MAX_VAL + 1).forEach(i -> list.add(i));
        assertEquals(MAX_VAL, list.size());
        long numEven = list.parallelStream().filter(v -> v % 2 == 0).peek(v -> {
                //System.out.println(Thread.currentThread().getName() + "; value: " + v);
        }).reduce(0l, (a, b) -> a + b);
        assertEquals(arithProgressionSum(2, MAX_VAL, MAX_VAL/2), numEven);
    }

    @Test
    public void testSequentialStream() {
        final long MAX_VAL = 50_000_000;
        List<Long> list = new ArrayList<>();
        LongStream.range(1, MAX_VAL + 1).forEach(i -> list.add(i));
        assertEquals(MAX_VAL, list.size());
        long numEven = list.stream().filter(v -> v%2 == 0).peek(v -> {
            //System.out.println(Thread.currentThread().getName() + "; value: " + v);
        }).reduce(0l, (a,b) -> a+b);
        assertEquals(arithProgressionSum(2, MAX_VAL, MAX_VAL/2), numEven);
    }

    @Test
    public void testSequentialStreamNoCollection() {
        final long MAX_VAL = 2_500_000_000L;
        long numEven = LongStream.range(1, MAX_VAL + 1).sequential().filter(v -> v % 2 == 0).peek(v -> {
            //System.out.println(Thread.currentThread().getName() + "; value: " + v);
        }).reduce(0l, (a, b) -> a + b);
        assertEquals(arithProgressionSum(2, MAX_VAL, MAX_VAL/2), numEven);
    }

    @Test
    public void testParallelStreamNoCollection() {
        final long MAX_VAL = 2_500_000_000L;
        long numEven = LongStream.range(1, MAX_VAL + 1).parallel().filter(v -> v % 2 == 0).peek(v -> {
            //System.out.println(Thread.currentThread().getName() + "; value: " + v);
        }).reduce(0l, (a, b) -> a + b);
        assertEquals(arithProgressionSum(2, MAX_VAL, MAX_VAL/2), numEven);
    }

}
