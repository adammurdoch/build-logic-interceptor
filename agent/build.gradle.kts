plugins {
    id("java-library")
}

tasks.jar.configure {
    manifest.attributes("Premain-Class" to "org.gradle.sample.agent.AgentMain")
}
