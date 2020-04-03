plugins {
    id("application")
}

repositories {
    jcenter()
}

configurations {
    create("agent")
    create("classloader")
    create("securityManager")
}

dependencies {
    implementation(project(":javalib"))
    implementation(project(":groovylib"))
    implementation(project(":kotlinlib"))
    "agent"(project(":agent"))
    "classloader"(project(":classloader"))
    "securityManager"(project(":securitymanager"))
}

application {
    mainClassName = "org.gradle.sample.Main"
}

tasks.register("runWithSecManager", JavaExec::class.java) {
    main = application.mainClassName
    val securityManagerClasspath = configurations.get("securityManager")
    classpath = sourceSets.main.get().runtimeClasspath + securityManagerClasspath
    jvmArgs("-Djava.security.manager=org.gradle.sample.security.InterceptingSecurityManager")
}

tasks.register("runWithAgent", JavaExec::class.java) {
    main = application.mainClassName
    val agentClassPath = configurations.get("agent")
    classpath = sourceSets.main.get().runtimeClasspath
    dependsOn(agentClassPath)
    jvmArgs("-javaagent:${agentClassPath.files.first()}=")
}

tasks.register("runWithClassLoader", JavaExec::class.java) {
    main = application.mainClassName
    val appClasspath = sourceSets.main.get().runtimeClasspath
    dependsOn(appClasspath)
    classpath = configurations.get("classloader")
    jvmArgs("-Djava.system.class.loader=org.gradle.sample.classloader.InstrumentingClassLoader", "-Dapp.classpath=${appClasspath.asPath}")
}