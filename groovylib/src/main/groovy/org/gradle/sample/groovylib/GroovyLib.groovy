package org.gradle.sample.groovylib

class GroovyLib {
    void something() {
        println "Groovy library"
        println "java.version = ${System.getProperty("java.version")}"
    }
}