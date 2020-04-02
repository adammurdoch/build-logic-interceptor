plugins {
    id("java-library")
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.ow2.asm:asm:8.0")
}

val reporting = tasks.register("reportingJar", Jar::class.java) {
    from(sourceSets.main.map { it.output.asFileTree.matching { include("**/Reporting.*") } })
    archiveFileName.set("reporting.jar")
}

tasks.jar.configure {
    dependsOn(reporting)
    manifest.attributes("Premain-Class" to "org.gradle.sample.agent.AgentMain")
    manifest.attributes("Can-Redefine-Classes" to "true")
    manifest.attributes("Boot-Class-Path" to "reporting.jar")
}
