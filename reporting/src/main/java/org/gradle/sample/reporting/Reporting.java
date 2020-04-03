package org.gradle.sample.reporting;

import java.io.File;

public class Reporting {
    public static void systemProperty(String name, String calledFrom) {
        System.out.println(String.format("[System property '%s' called from %s]", name, calledFrom));
    }

    public static void readFile(File file, String calledFrom) {
        System.out.println(String.format("[File '%s' read from %s]", file, calledFrom));
    }
}
