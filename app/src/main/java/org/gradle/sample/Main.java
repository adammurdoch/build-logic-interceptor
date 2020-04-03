package org.gradle.sample;

import org.gradle.sample.groovylib.GroovyLib;
import org.gradle.sample.javalib.JavaLib;
import org.gradle.sample.kotlinlib.KotlinLib;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("-- Application --");
        System.out.println("java.version = " + System.getProperty("java.version"));
        System.out.println("some-prop = " + System.getProperty("some-prop"));
        File location = new File("../README.md");
        new JavaLib().something(location);
        new GroovyLib().something();
        new KotlinLib().something();
    }
}
