package org.gradle.sample.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class InstrumentingClassLoader extends URLClassLoader {
    public InstrumentingClassLoader(ClassLoader parent) throws MalformedURLException {
        super(classpath(), parent);
        System.out.println("Starting using system classloader");
    }

    private static URL[] classpath() throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        for (String path : System.getProperty("app.classpath").split(File.pathSeparator)) {
            urls.add(new File(path).toURI().toURL());
        }
        return urls.toArray(new URL[urls.size()]);
    }
}
