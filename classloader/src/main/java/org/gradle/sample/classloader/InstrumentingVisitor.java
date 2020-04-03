package org.gradle.sample.classloader;

import org.gradle.sample.reporting.Reporting;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileInputStream;

public class InstrumentingVisitor extends ClassVisitor {
    private static final Type STRING_TYPE = Type.getType(String.class);

    private final String className;

    public InstrumentingVisitor(String className, ClassVisitor visitor) {
        super(Opcodes.ASM8, visitor);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodVisitor(Opcodes.ASM8, methodVisitor) {
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                if (opcode == Opcodes.INVOKESTATIC && owner.equals(Type.getType(System.class).getInternalName()) && name.equals("getProperty")) {
                    if (Type.getMethodType(descriptor).getArgumentTypes().length == 1) {
                        visitInsn(Opcodes.DUP);
                        visitLdcInsn(className);
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(Reporting.class).getInternalName(), "systemProperty", Type.getMethodDescriptor(Type.VOID_TYPE, STRING_TYPE, STRING_TYPE), false);
                    }
                }
                if (opcode == Opcodes.INVOKESPECIAL && owner.equals(Type.getType(FileInputStream.class).getInternalName()) && name.equals("<init>")) {
                    if (Type.getMethodType(descriptor).getArgumentTypes().length == 1) {
                        visitInsn(Opcodes.DUP);
                        visitLdcInsn(className);
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(Reporting.class).getInternalName(), "readFile", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(File.class), STRING_TYPE), false);
                    }
                }
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        };
    }
}
