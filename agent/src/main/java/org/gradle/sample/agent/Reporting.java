package org.gradle.sample.agent;

public class Reporting {
    // Called from generated code
    public static void accessed(String propertyName) {
        System.out.println("## SYSTEM PROPERTY USED: " + propertyName);
    }
}
