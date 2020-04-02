plugins {
    id("java-library")
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.ow2.asm:asm:8.0")
    implementation("org.codehaus.groovy:groovy:2.5.8")
    runtimeOnly("org.jetbrains.kotlin:kotlin-stdlib:1.3.71")
}
