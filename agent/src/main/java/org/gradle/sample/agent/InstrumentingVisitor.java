package org.gradle.sample.agent;

import org.gradle.sample.reporting.Reporting;
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
        private static final Type STRING_TYPE = Type.getType(String.class);

        public InstrumentingMethodVisitor(MethodVisitor visitor) {
            super(Opcodes.ASM8, visitor);
        }

        @Override
        public void visitCode() {
            System.out.println("-> instrumenting System.getProperty()");
            super.visitCode();
            visitVarInsn(Opcodes.ALOAD, 0);
            visitInsn(Opcodes.ACONST_NULL);
            visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(Reporting.class).getInternalName(), "systemProperty", Type.getMethodDescriptor(Type.VOID_TYPE, STRING_TYPE, STRING_TYPE), false);
        }
    }
}
