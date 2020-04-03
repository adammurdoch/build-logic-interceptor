plugins {
    id("java-library")
}

repositories {
    jcenter()
}

dependencies {
    implementation(project(":reporting"))
    implementation("org.ow2.asm:asm:8.0")
}

tasks.jar.configure {
    manifest.attributes("Premain-Class" to "org.gradle.sample.agent.AgentMain")
    manifest.attributes("Can-Redefine-Classes" to "true")
    manifest.attributes("Boot-Class-Path" to configurations.runtimeClasspath.get().joinToString(" "))
}
