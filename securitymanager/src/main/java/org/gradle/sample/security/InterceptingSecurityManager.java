package org.gradle.sample.security;

import org.gradle.sample.reporting.Reporting;

import java.io.File;
import java.security.Permission;
import java.util.concurrent.atomic.AtomicBoolean;

public class InterceptingSecurityManager extends SecurityManager {
    private final AtomicBoolean init = new AtomicBoolean();
    private final ThreadLocal<Boolean> reporting = ThreadLocal.withInitial(() -> false);

    public InterceptingSecurityManager() {
        System.out.println("[intercepting security manager]");
    }

    @Override
    public void checkPermission(Permission perm) {
        if (!init.get()) {
            Class<?>[] classContext = getClassContext();
            for (Class<?> aClass : classContext) {
                if (aClass.getName().startsWith("java.") || aClass.getName().startsWith("jdk.") || aClass.getName().startsWith("sun.")) {
                    continue;
                }
                if (aClass.equals(InterceptingSecurityManager.class)) {
                    continue;
                }
                init.set(true);
                return;
            }
        }
    }

    @Override
    public void checkRead(String file) {
        super.checkRead(file);
        if (init.get()) {
            if (!reporting.get()) {
                reporting.set(true);
                try {
                    Reporting.readFile(new File(file), null);
                } finally {
                    reporting.set(false);
                }
            }
        }
    }

    @Override
    public void checkPropertyAccess(String key) {
        super.checkPropertyAccess(key);
        if (init.get()) {
            if (!reporting.get()) {
                reporting.set(true);
                try {
                    Reporting.systemProperty(key, null);
                } finally {
                    reporting.set(false);
                }
            }
        }
    }
}
