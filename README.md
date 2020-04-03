
Agent
-----
- Need to filter out property and file access from the JDK and Gradle core classes. 
- Very poor error reporting when something goes wrong (produces a JVM crash report)

ClassLoader
----
- Captures usages only from plugins
- Need to intercept dynamic Groovy differently
- Does not intercept reflective invocation. However, if you really want to invoke `System.getProperty()` reflectively, go for it.
- Can reuse to apply decoration to types, and to fix `@Nested final` properties.

Security Manager
----
- Need to filter out property and file access from the JDK and Gradle core classes. 
