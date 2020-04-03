package org.gradle.sample.javalib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaLib {
    public void something(File location) throws IOException {
        System.out.println("Java library");
        System.out.println("java.version = " + System.getProperty("java.version"));
        Runnable runnable = () -> {
            System.out.println("some-prop = " + System.getProperty("some-prop"));
        };
        runnable.run();

        try (InputStream inputStream = new FileInputStream(location)) {
            inputStream.read(new byte[1024]);
        }
    }
}
