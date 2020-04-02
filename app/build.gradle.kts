plugins {
    id("application")
}

repositories {
    jcenter()
}

configurations {
    create("agent")
    create("classloader")
}

dependencies {
    implementation(project(":javalib"))
    implementation(project(":groovylib"))
    implementation(project(":kotlinlib"))
    "agent"(project(":agent"))
    "classloader"(project(":classloader"))
}

application {
    mainClassName = "org.gradle.sample.Main"
}

tasks.register("runWithAgent", JavaExec::class.java) {
    main = application.mainClassName
    classpath = sourceSets.main.get().runtimeClasspath
    val agentClassPath = configurations.get("agent")
    dependsOn(agentClassPath)
    jvmArgs("-javaagent:${agentClassPath.singleFile}=")
}

tasks.register("runWithClassLoader", JavaExec::class.java) {
    main = application.mainClassName
    classpath = configurations.get("classloader")
    val appClasspath = sourceSets.main.get().runtimeClasspath
    dependsOn(appClasspath)
    jvmArgs("-Djava.system.class.loader=org.gradle.sample.classloader.InstrumentingClassLoader", "-Dapp.classpath=${appClasspath.asPath}")
}