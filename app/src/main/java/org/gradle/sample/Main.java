package org.gradle.sample;

import org.gradle.sample.groovylib.GroovyLib;
import org.gradle.sample.javalib.JavaLib;
import org.gradle.sample.kotlinlib.KotlinLib;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("-- Application --");
        System.out.println("java.version = " + System.getProperty("java.version"));
        System.out.println("some-prop = " + System.getProperty("some-prop"));
        new JavaLib().something();
        new GroovyLib().something();
        new KotlinLib().something();
    }
}
