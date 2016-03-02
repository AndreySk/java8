package com.askr.java8;


import org.junit.Test;

import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionalInterfaceTest {

    @FunctionalInterface
    private interface ActionI {
        void action();
    }
    private class MyAction {

        public MyAction(int a) {
        }

        public MyAction() {
            System.out.println("Constructor");
        }

        private void myTestAction() {
            System.out.println("Method");
        }
    }

    @Test
    public void functionalInterface() {

        ActionI act = new ActionI() {
            @Override
            public void action() {

            }
        };
        ActionI actionMethodRef = new MyAction(0)::myTestAction; // method reference
        ActionI actionConstructorRef = MyAction::new; // constructor reference
        ActionI actionLambda = () -> System.out.println("lambda");

        ActionI[] actions = new ActionI[] {actionMethodRef, actionConstructorRef, actionLambda};

        for (ActionI action: actions) {
            action.action();
        }

    }

    //@FunctionalInterface
    interface DefaultImplI {
        default void defaultAction() {
            System.out.println("default action");
        }
        void action1();

        //void action2(int a);
    }

    @Test
    public void functionalInterfaceDefault() {
        DefaultImplI i = () -> System.out.println("action1");
        i.action1();
    }

    @Test
    public void predicateTest() {
        Predicate<Integer> isEvenJava7Style = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isEvenJava8Style = (n) -> n%2 == 0;

        assertTrue(isEvenJava7Style.test(2));
        assertTrue(isEvenJava8Style.test(2));
        assertFalse(isEvenJava7Style.test(3));
        assertFalse(isEvenJava8Style.test(3));

        Predicate<Integer> notEven = isEvenJava8Style.negate();
        assertTrue(notEven.test(3));
        assertFalse(notEven.test(2));

        assertTrue(isEvenJava8Style.or(notEven).test(1));
        assertTrue(isEvenJava8Style.or(notEven).test(2));
    }

    @Test
    public void functionTest() {
        Function<Integer, Boolean> isPositiveFunc = (a) -> a>0;
        Predicate<Integer> isPositivePredicate = isPositiveFunc::apply;
        assertTrue(isPositivePredicate.test(12));
        assertTrue(isPositiveFunc.apply(12));
    }

}
