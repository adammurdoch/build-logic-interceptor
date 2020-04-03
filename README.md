This repository contains some experiments in approaches that can be used in Gradle to intercept build logic access to 
external resources such as system properties, files and processes. This is intended to be used to detect undeclared
build inputs when instant execution is used, but may also be applied to tasks. 

There are 3 experiments:

1. Use an instrumenting Java agent to rewrite the implementation of `System.getProperty()` to notify a reporting class when `getProperty()` is called.
This intercepts all calls, including those made by the JDK and Gradle core.
2. Use an instrumenting `ClassLoader` to rewrite the bytecode of plugin classes to notify a reporting class when `System.getProperty()` and `new FileInputStream()` is called.
Also uses a Groovy metaclass to intercept calls from dynamic Groovy code.
3. Use a `SecurityManager` to intercept system property and file access. This intercepts all calls, including those made by the JDK and Gradle core.

Each experiment is applied to the application in the `app` directory. The app is made up of Java, Kotlin and dynamic and statically compiled Groovy source.

Java Agent
-----
The agent implementation is in the `agent` directory.

Pros
- Nothing really, compared to the other options

Cons
- Need to filter out property and file access from the JDK and Gradle core classes. There's no obvious reliable strategy
for this other than walking the stack on each access when build logic is running. Another option might be to whitelist 
certain properties and files and assume that only the JDK or Gradle uses these. 
- Very poor error reporting when something goes wrong with the instrumentation (produces a JVM crash report with no stack trace).

ClassLoader
----
The implementation is in the `classloader` directory.

Pros
- Captures direct usages from build logic but not the JDK or Gradle core.
- Can reuse this infrastructure to apply decoration to model types, to fix `@Nested final` properties, mix in legacy APIs and so on. The result can be cached on disk rather than generated at runtime, and possibly even generated at build time.
- Is also applied automatically when the build does not run in a daemon, except when running unit tests. This is not the case for the other options.
- We already use this approach to mix in some legacy types and methods. 

Cons 
- Need to intercept dynamic Groovy differently (but this is included in the experiment).
- Does not intercept reflective invocation. It's unlikely that we care about this case, but could be combined with one of the other approaches if we do.

Security Manager
----
The implementation is in the `securitymanager` directory.

Pros
- Most likely to intercept all file access through the various APIs.

Cons
- Need to filter out property and file access from the JDK and Gradle core classes.
- Very easy to break the JDK startup.
- Only works for APIs provided and instrumented by the JDK.
- Does not allow patching of `System.getenv()` or `System.console()` in the daemon. On the other hand, it's unlikely that we want to solve those problems by doing this.
