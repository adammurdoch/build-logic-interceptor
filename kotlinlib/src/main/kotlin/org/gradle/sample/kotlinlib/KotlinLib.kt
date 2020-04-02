package org.gradle.sample.kotlinlib

class KotlinLib {
    fun something() {
        println("Kotlin library")
        println("java.version = ${System.getProperty("java.version")}")
        run {
            println("some-prop = ${System.getProperty("some-prop")}")
        }
    }
}
