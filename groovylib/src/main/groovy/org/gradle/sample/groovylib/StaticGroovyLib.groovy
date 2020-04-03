package org.gradle.sample.groovylib

import groovy.transform.CompileStatic

@CompileStatic
class StaticGroovyLib {
    void something() {
        println "static Groovy library"
        println "java.version = ${System.getProperty("java.version")}"
        with {
            println "some-prop = ${System.class.getProperty("some-prop")}"
        }
    }
}
