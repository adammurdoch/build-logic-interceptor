package org.gradle.sample.agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class InstrumentingVisitor extends ClassVisitor {
    public InstrumentingVisitor(ClassVisitor visitor) {
        super(Opcodes.ASM8, visitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        Type methodType = Type.getMethodType(descriptor);
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals("getProperty") && methodType.getArgumentTypes().length == 1) {
            return new InstrumentingMethodVisitor(methodVisitor);
        }
        return methodVisitor;
    }

    private static class InstrumentingMethodVisitor extends MethodVisitor {
        public InstrumentingMethodVisitor(MethodVisitor visitor) {
            super(Opcodes.ASM8, visitor);
        }

        @Override
        public void visitCode() {
            System.out.println("-> visiting System.getProperty()");
            super.visitCode();
//            visitFieldInsn(Opcodes.GETSTATIC, Type.getType(System.class).getInternalName(), "out", Type.getType(PrintStream.class).getDescriptor());
//            visitVarInsn(Opcodes.ALOAD, 0);
//            visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(PrintStream.class).getInternalName(), "println", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class)), false);
//            visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(Reporting.class).getInternalName(), "thing", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class)), false);
        }
    }
}
