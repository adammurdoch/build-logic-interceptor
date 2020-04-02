package org.gradle.sample.classloader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            InputStream inputStream = getResourceAsStream(name.replace('.', '/') + ".class");
            try {
                if (inputStream == null) {
                    throw new ClassNotFoundException(name);
                }
                ClassReader reader = new ClassReader(inputStream);
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES) {
                    @Override
                    protected ClassLoader getClassLoader() {
                        return InstrumentingClassLoader.this;
                    }
                };
                reader.accept(new InstrumentingVisitor(name, classWriter), ClassReader.EXPAND_FRAMES);
                byte[] bytes = classWriter.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
