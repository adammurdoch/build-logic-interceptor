plugins {
    id("application")
}

repositories {
    jcenter()
}

dependencies {
    implementation(project(":javalib"))
    implementation(project(":groovylib"))
    implementation(project(":kotlinlib"))
}

application {
    mainClassName = "org.gradle.sample.Main"
}
