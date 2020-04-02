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
    val agentClassPath = configurations.get("agent")
    classpath = sourceSets.main.get().runtimeClasspath + agentClassPath
    dependsOn(agentClassPath)
    jvmArgs("-javaagent:${agentClassPath.files.first()}=")
}

tasks.register("runWithClassLoader", JavaExec::class.java) {
    main = application.mainClassName
    val appClasspath = sourceSets.main.get().runtimeClasspath
    dependsOn(appClasspath)
    // Move Groovy and Kotlin to the system classloader, to closer match the Gradle runtime
    classpath = configurations.get("classloader") + appClasspath.filter { it.name.matches(Regex("groovy-.+\\.jar")) || it.name.matches(Regex("kotlin-.+\\.jar")) }
    jvmArgs("-Djava.system.class.loader=org.gradle.sample.classloader.InstrumentingClassLoader", "-Dapp.classpath=${appClasspath.asPath}")
}