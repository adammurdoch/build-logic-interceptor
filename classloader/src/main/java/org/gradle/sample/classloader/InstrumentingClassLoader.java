package org.gradle.sample.classloader;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaClassRegistry;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.StaticMetaClassSite;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
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
        System.out.println("[Starting using system classloader]");
        MetaClassRegistry classRegistry = MetaClassRegistryImpl.getInstance(0);
        MetaClassImpl metaClass = new MetaClassImpl(classRegistry, System.class) {
            @Override
            public CallSite createStaticSite(CallSite site, Object[] args) {
                return new StaticMetaClassSite(site, this);
            }

            @Override
            public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
                if (object.equals(System.class) && methodName.equals("getProperty") && arguments.length == 1) {
                    Reporting.systemProperty((String) arguments[0], null);
                }
                return super.invokeStaticMethod(object, methodName, arguments);
            }
        };
        metaClass.initialize();
        classRegistry.setMetaClass(System.class, metaClass);
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
