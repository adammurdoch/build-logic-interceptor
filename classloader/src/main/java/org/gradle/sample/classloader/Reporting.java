package org.gradle.sample.classloader;

public class Reporting {
    public static void systemProperty(String name, String calledFrom) {
        System.out.println(String.format("[System property '%s' called from %s]", name, calledFrom));
    }
}
