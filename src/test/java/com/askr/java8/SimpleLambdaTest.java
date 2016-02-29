package com.askr.java8;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class SimpleLambdaTest {
    private List<Integer> list;

    private static final int NUM_ELEMENTS = 10000;
    static int[] array;

    @BeforeClass
    public static void initArray() {
        array = new int[NUM_ELEMENTS];
        Random rand = new Random(0);
        for (int i = 0; i < NUM_ELEMENTS; i++) {
            array[i] = rand.nextInt(1000);
        }
    }

    @Before
    public void init() {
        list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
    }

    private boolean checkSorting(List<Integer> reversiblySortedList) {
        for (int i = 0; i < reversiblySortedList.size() - 1; i++) {
            if (reversiblySortedList.get(i) < reversiblySortedList.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSorting(Integer[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] < array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void java7sorting1() {
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return -o1.compareTo(o2);
            }
        });
        assertTrue(checkSorting(list));
    }

    @Test
    public void java7sorting2() {
        class MyComparator implements Comparator<Integer> {
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }
        Collections.sort(list, new MyComparator());
        assertTrue(checkSorting(list));
    }

    @Test
    public void java8sorting1() { // full lambda
        Collections.sort(list, (Integer o1, Integer o2) -> {
                    return -o1.compareTo(o2);
                }
        );
        assertTrue(checkSorting(list));
    }

    @Test
    public void java8sorting2() { // lambda without type specification
        Collections.sort(list, (o1, o2) -> {
                    return -o1.compareTo(o2);
                }
        );
        assertTrue(checkSorting(list));

    }

    @Test
    public void java8sorting3() { // lambda without return key word
        Collections.sort(list, (o1, o2) -> -o1.compareTo(o2));
        assertTrue(checkSorting(list));
    }

    @Test
    public void java8sorting4() {
        class MyComparator { // does not implement Comparator
            int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }
        Collections.sort(list, new MyComparator()::compare);
        assertTrue(checkSorting(list));
    }

    @Test
    public void java8ArraysParallelSorting() {

        Integer[] arr = new Integer[array.length];
        for (int i=0; i<array.length; i++) {
            arr[i] = array[i];
        }
        //Arrays.sort(arr);
        Arrays.parallelSort(arr, (o1, o2) -> -o1 + o2);
        assertTrue(checkSorting(arr));
    }

    @Test
    public void createThreadJava7() throws InterruptedException {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                IntStream.range(0, 10).forEach(new IntConsumer() {
                    @Override
                    public void accept(int value) {
                        System.out.println(value);
                    }
                });
            }
        });
        th.start();
        th.join();
    }

    @Test
    public void createThreadJava8_1() throws InterruptedException {
        Runnable r = () -> {
            IntStream.range(0, 10).forEach(new IntConsumer() {
                @Override
                public void accept(int value) {
                    System.out.println(value);
                }
            });
        };
        Thread th = new Thread(r);
        th.start();
        th.join();
    }

    @Test
    public void createThreadJava8_2() throws InterruptedException {
        Thread th = new Thread(() -> IntStream.range(0, 10).forEach((v) ->  System.out.println(v)));
        th.start();
        th.join();
    }



}
