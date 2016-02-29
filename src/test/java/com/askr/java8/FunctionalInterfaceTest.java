package com.askr.java8;


import org.junit.Test;

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

        public void myTestAction() {
            System.out.println("Method");
        }
    }

    @Test
    public void createFunctionalInterfaceInstance() {

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
    interface DefaultImplI2 extends DefaultImplI {
        @Override
        default void defaultAction() {};
    }

    class DefaultC implements DefaultImplI2 {
        @Override
        public void action1() {

        }
    }

    @Test
    public void functionalInterfaceDefault() {

       DefaultImplI i = () -> System.out.println("action1");
        DefaultImplI i2 = new DefaultC();
        i2.defaultAction();
    }
}
