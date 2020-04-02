package org.gradle.sample.javalib;

public class JavaLib {
    public void something() {
        System.out.println("Java library");
        System.out.println("java.version = " + System.getProperty("java.version"));
        Runnable runnable = () -> {
            System.out.println("some-prop = " + System.getProperty("some-prop"));
        };
        runnable.run();
    }
}
