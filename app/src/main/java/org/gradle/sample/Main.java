package org.gradle.sample;

import org.gradle.sample.groovylib.GroovyLib;
import org.gradle.sample.javalib.JavaLib;
import org.gradle.sample.kotlinlib.KotlinLib;

public class Main {
    public static void main(String[] args) {
        System.out.println("Started");
        new JavaLib().something();
        new GroovyLib().something();
        new KotlinLib().something();
    }
}
