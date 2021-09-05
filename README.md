# Kradle

Swiss army knife for Kotlin development.

This project aims to provide a solution for common Kotlin development tasks using Gradle.

- [Bootstrap project](#bootstrap-project)
- [Check for dependency updates](#check-for-dependency-updates)
- [Static code analysis](#static-code-analysis)
- [Scan for vulnerabilities in dependencies](#scan-for-vulnerabilities-in-dependencies)
- [Run JMH benchmarks](#run-jmh-benchmarks)
- [Testing](#testing)
- [Generate documentation](#generate-documentation)
- [Packaging](#packaging)
- [Create Docker images](#create-docker-images)
- [Automatic restarts](#automatic-restarts)

It utilizes many other Gradle plugins. We will refer to them as vendor plugins. `kradle` takes care of applying and
configuring them.

So instead of fiddling with your build script, you just need to apply one plugin.

- For Kotlin apps: `net.bitsandbobs.kradle-app`
- For Kotlin libs: `net.bitsandbobs.kradle-lib`

## Quickstart

Just add the `kradle` plugin to your build script.

_build.gradle.kts_

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("net.bitsandbobs.kradle-app") version "1.0.0"
}

group = "com.example"
version = "1.0.0"

kradle {
    mainClass("com.example.App")
}
```

You can now use `gradle boostrap` to set up the project.

## Tasks

These are the tasks added by `kradle`.

| Task | Description | Alias for |
|---|---|---
| [bootstrap](#bootstrap-plugin) | Boostrap app/lib project | - |
| [showDependencyUpdates](#dependencyupdatesblueprint) | Displays dependency updates | dependencyUpdates |
| [lint](#ktlintblueprint) | Runs [ktlint](https://ktlint.github.io/) | ktlintCheck |
| [analyzeCode](#detektblueprint) | Runs [detekt](https://detekt.github.io/detekt/) code analysis | detekt |
| [generateDetektConfig](#detektblueprint) | Generates _detekt-config.yml_ | - |
| [analyzeDependencies](#dependencycheckblueprint) | Analyzes dependencies for vulnerabilities | dependencyCheckAnalyze |
| [runBenchmarks](#benchmarksblueprint) | Runs all [JMH](https://github.com/openjdk/jmh) benchmarks | benchmark |
| [generateDocumentation](#dokkablueprint) | Generates [Dokka](https://kotlin.github.io/dokka/) HTML documentation | dokkaHtml |
| [package](#javablueprint) | Creates JAR | jar |
| [buildImage](#jibblueprint) | Builds Docker image (kradle-app only) | jibDockerBuild |
| [uberJar](#shadowblueprint) | Creates Uber-JAR (kradle-app only) | shadowJar |
| [install](#mavenpublishblueprint) | Installs JAR to local Maven repository (kradle-lib only) | publishToMavenLocal |
| [generateGitignore](#git-plugin) | Generates _.gitignore_ | - |
| [generateBuildProperties](#buildpropertiesblueprint) | Generates _build.properties_ | - |
| [dev](#applicationblueprint) | Runs the application and stops it when sources change (use with `-t`, kradle-app only) | - |

## Configuration

`kradle` provides an extension for configuration. These are all available options. The lines __not__ commented out
represent the defaults.

```kotlin
kradle {
    targetJvm("16")
    // mainClass("com.example.App")
    kotlinxCoroutinesVersion("1.5.1")
    jacocoVersion("0.8.7")
    ktlintVersion("0.42.1")
    tests {
        junitJupiterVersion("5.7.2")
        // useKotest()
        // useMockk()
    }
    image {
        baseImage("bellsoft/liberica-openjdk-alpine:16")
        // ports.add(8080)
        // jvmOpts("-Xmx512M")
        // withAppSh()
        // withJvmKill("1.16.0")
    }

    // disable(BuildPropertiesBlueprint::class.java)
}
```

## Blueprints

Configuration of vendor plugins is handled by classes called _blueprints_.

If you want to manually configure a plugin, you should disable the blueprint to avoid conflicts.

Example:

```kotlin
kradle {
    disable(JibBlueprint::class.java)
}
```

## Bootstrap project

### Bootstrap Plugin

Plugin: internal

Adds the task `bootstrap`, which

- Initializes Git
- Adds Gradle wrapper
- Creates essentials directories and files

## Check for dependency updates

### DependencyUpdatesBlueprint

Plugin: [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)

> Gradle plugin that provides tasks for discovering dependency updates.

Adds the task `showDependencyUpdates`, which shows all available dependency updates. It only considers stable versions,
no release candidates or milestone builds.

## Static code analysis

### KtlintBlueprint

Plugin: [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)

> Provides a convenient wrapper plugin over the ktlint project.

Adds the `lint` task, which runs [ktlint](https://ktlint.github.io/) on the project. Uses the standard rule set with one
exception: Wildcard imports are allowed. Experimental rules are also enabled.

The `lint` task is also executed when running `check`.

The `kradle` extension provides a property to configure the ktlint version.

```kotlin
kradle {
    ktlintVersion("0.42.1")
}
```

### DetektBlueprint

Plugin: [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt)

> Static code analysis for Kotlin.

Adds the `analyzeCode` task, which runs [detekt](https://detekt.github.io/detekt/) on the project. This task is also
executed when running `check`.

detekt can be configured with a file named _detekt-config.yml_ in the project root directory.

The task `generateDetektConfig` generates a configuration file with sane defaults.

## Scan for vulnerabilities in dependencies

### DependencyCheckBlueprint

Plugin: [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)

> OWASP dependency-check-gradle plugin.

Adds the `analyzeDependencies` tasks, which scans all dependencies on the runtime and compile classpath for
vulnerabilities.

## Run JMH benchmarks

### BenchmarksBlueprint

Plugin: [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark)

> Toolkit for running benchmarks for multiplatform Kotlin code.

Adds the `runBenchmarks` tasks, which runs all [JMH](https://github.com/openjdk/jmh) benchmarks found
under `src/benchmark/kotlin`.

## Testing

### TestBlueprint

Plugin: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

> The Java plugin adds Java compilation along with testing and bundling capabilities to a project.

Sets up [JUnit Jupiter](https://junit.org/junit5/) for running tests. The `kradle` extension provides a property to
configure the version. There are also convenience methods to add [kotest](https://kotest.io/)
and [mockk](https://mockk.io/) dependencies.

```kotlin
kradle {
    tests {
        junitJupiterVersion("5.7.2")
        useKotest("4.6.1")
        useMockk("1.12.0")
    }
}
```

Test code must reside under `src/test/` and the files must end with `Test` `Tests` or `IT`.

### JacocoBlueprint

Plugin: [JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)

> The JaCoCo plugin provides code coverage metrics for Java code via integration with JaCoCo.

Always generates [JaCoco](https://www.jacoco.org/jacoco/) code coverage reports when running tests. The report can be
found under `build/reports/jacoco/`.

The `kradle` extension provides a property to configure the JaCoCo version.

```kotlin
kradle {
    jacocoVersion("0.8.7")
}
```

## Generate documentation

### DokkaBlueprint

Plugin: [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka)

> Dokka, the documentation engine for Kotlin.

Adds the `generateDocumentation` task, which uses [Dokka](https://kotlin.github.io/dokka/) to generates a HTML
documentation based on KDoc comments. The documentation can be found under `build/docs`.

Package and module documentation can be placed in a file _package.md_ or _module.md_ in the project directory.

## Packaging

### ShadowBlueprint

_net.bitsandbobs.kradle-app only_

Plugin: [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

> A Gradle plugin for collapsing all dependencies and project code into a single Jar file.

Adds the task `uberJar`, which creates an Uber-Jar. This is a JAR containing all dependencies. The resulting JAR is
minimized, so only required classes are added.

## Automatic restarts

### ApplicationBlueprint

_net.bitsandbobs.kradle-app only_

Plugin: [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

> The Application plugin facilitates creating an executable JVM application.

Adds the task `dev`. Basically the same as `run`, but watches for changes in `src/main/kotlin`
and `src/main/resources`. If changes are detected, the application will be stopped. Should be used with continuous build
flag `-t` to archive automatic restarts.

Sets the environment variable `DEV_MODE=true` when executing `gradle run` or `gradle dev`. So the application can easily
figure out, if it is run in development environment. Speeds up application start by using `-XX:TieredStopAtLevel=1`.

Adds the `Main-Class` entry to the manifest, so the JAR is runnable.

## Create Docker images

### JibBlueprint

_net.bitsandbobs.kradle-app only_

Plugin: [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib)

> Containerize your Java application.

Adds the task `buildImage`, which creates a Docker image using [Jib](https://github.com/GoogleContainerTools/jib).

The base image, exposed ports and JVM command-line options can be configured by the `kradle` extension.

```kotlin
kradle {
    image {
        baseImage("bellsoft/liberica-openjdk-alpine:16")
        // ports.add(8080)
        // jvmOpts("-Xmx512M")
        // withAppSh()
        // withJvmKill("1.16.0")
    }
}
```

`withAppSh` will use a script as entrypoint for the container. You can provide your own script
in `src/main/extra/app.sh`. If you don't, the plugin will create one for you.

`withJvmKill` adds [jvmkill](https://github.com/airlift/jvmkill) to the image, which terminates the JVM when it is
unable to allocate memory.

## Miscellaneous

### JavaBlueprint

Plugin: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

> The Java plugin adds Java compilation along with testing and bundling capabilities to a project.

Adds the task `package`, which creates a JAR file. It also sets the `sourceCompatibility` and `targetCompatibility`
based on the extension property.

```kotlin
kradle {
    targetJvm("16")
}
```

### KotlinBlueprint

Plugin: [Kotlin Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm)

> Kotlin plugins for Gradle.

Adds Kotlin Standard Library, kotlin.test library and coroutines dependencies. The coroutines version can be configured
with the `kradle` extension.

```kotlin
kradle {
    kotlinxCoroutinesVersion("1.5.1")
}
```

You still have to apply the Kotlin plugin in your project. This also defines the Kotlin version used.

_build.gradle.kts_

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
}
```

The plugin also enables [opt-ins](https://kotlinlang.org/docs/opt-in-requirements.html).

### MavenPublishBlueprint

_net.bitsandbobs.kradle-lib only_

Plugin: [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)

> The Maven Publish Plugin provides the ability to publish build artifacts to an Apache Maven repository.

Adds the task `install`, which installs the library to your local Maven repository.

### BuildPropertiesBlueprint

Plugin: internal

Adds the task `generateBuildProperties`, which generates a file _build.properties_ containing the build timestamp,
project version and Git commit id.

The task is also executed as after `processResources`.

## Other plugins

These plugins are applied, but there are no blueprints for them.

### Java Library Plugin

_net.bitsandbobs.kradle-lib only_

Plugin: [Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html)

> The Java Library plugin expands the capabilities of the Java plugin by providing specific knowledge about Java libraries

### Serialization Plugin

Plugin: [kotlinx.serialization Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization)

> Kotlin compiler plugin for kotlinx.serialization library.

Required for kotlinx.serialization.

### All-open Plugin

Plugin: [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen)

> Kotlin plugins for Gradle.

Can be used to make specific classes `open`.

### Test Logger Plugin

Plugin: [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)

> A Gradle plugin for printing beautiful logs on the console while running tests.

### Git Plugin

Plugin: internal

Adds the task `generateGitignore`, which generates _.gitignore_ with sane defaults.

Also adds `gitCommit` to the project properties.

### Project Properties Plugin

Plugin: internal

Looks for a property file called _project.properties_ in the project directory. If found, adds the entries to the
project properties.
