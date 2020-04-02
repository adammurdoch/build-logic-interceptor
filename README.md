
Agent
-----
- Very poor error reporting when something goes wrong (produces a JVM crash report)

ClassLoader
----
- Need to intercept dynamic Groovy differently
- Does not intercept reflective invocation. However, if you really want to invoke `System.getProperty()` reflectively, go for it.
