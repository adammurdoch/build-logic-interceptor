package org.gradle.sample.groovylib

class GroovyLib {
    void something() {
        println "Groovy library"
        println "java.version = ${System.getProperty("java.version")}"
        with {
            println "some-prop = ${System.class.getProperty("some-prop")}"
        }
    }
}