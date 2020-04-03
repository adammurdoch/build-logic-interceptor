package org.gradle.sample.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) throws IOException, UnmodifiableClassException, ClassNotFoundException {
        System.out.println(String.format("* Starting agent using args '%s'", agentArgs));
        System.out.println("* Can redefine classes: " + inst.isRedefineClassesSupported());
        System.out.println("* Can modify System: " + inst.isModifiableClass(System.class));
        System.out.println("* Instrumenting System");
        ClassReader reader;
        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(System.class.getName().replace('.', '/') + ".class")) {
            reader = new ClassReader(inputStream);
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = new InstrumentingVisitor(classWriter);
        reader.accept(visitor, ClassReader.EXPAND_FRAMES);
        inst.redefineClasses(new ClassDefinition(System.class, classWriter.toByteArray()));
        System.out.println("* System is redefined");

        System.getProperty("java.version");
    }
}
