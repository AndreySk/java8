package com.askr.java8;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StreamsOperationsTest {
    @Test
    public void testFilterCount() { // calculate number of aa occurrences
        long numAAs = Stream.of("aab", "aac", "abc").filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("aa");
            }
        }).count();
        assertEquals(2, numAAs);

        assertEquals(2, Stream.of("aab", "aac", "abc").filter(s -> s.contains("aa")).count());
    }

    @Test
    public void testMapReducePeek() { // convert each string into number and calculate the sum
        int result = Stream.of("1","2","3").map(s -> Integer.parseInt(s)).peek(v -> System.out.println(v)).reduce((a,b) -> a+b).get();
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
        AtomicInteger numOfEqualities = new AtomicInteger(0);
        String oneOne = Stream.of("-1","0","1","1","2","1","3").filter((v) -> {
            System.out.println(v + " takes place");
            if (v.equals("1")) {
                numOfEqualities.incrementAndGet();
                return true;
            }
            return false;
        }).map(str -> {
            System.out.println("mapping: " + str);
            return str.concat(str);
        }).findFirst().orElseGet(() -> "");
        assertEquals(1, numOfEqualities.get());
        System.out.println(oneOne);
        assertEquals("11", oneOne);
    }
//
//    @Test
//    public void testFlatMap() {
//        Stream.of(1,2,3,4).flatMapToInt(integer -> IntStream.rangeClosed(integer, integer)).
//    }

}
