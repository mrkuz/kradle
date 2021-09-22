# Kradle

Swiss army knife for Kotlin development.

`kradle` is a plugin for Gradle, supporting Kotlin developers with their day-to-day activities.

- [Bootstrap project](#bootstrap-project)
- [Automatic restarts](#automatic-restarts)
- [Check for dependency updates](#check-for-dependency-updates)
- [Static code analysis](#static-code-analysis)
- [Scan for vulnerabilities in dependencies](#scan-for-vulnerabilities-in-dependencies)
- [Run JMH benchmarks](#run-jmh-benchmarks)
- [Testing](#testing)
- [Generate documentation](#generate-documentation)
- [Packaging](#packaging)
- [Create Docker image](#create-docker-image)
- [Quality of life improvements](#quality-of-life-improvements)

## (Very) Quick Start

```shell
mkdir demo && cd demo
curl https://raw.githubusercontent.com/mrkuz/kradle/main/examples/demo/settings.gradle.kts -o settings.gradle.kts
curl https://raw.githubusercontent.com/mrkuz/kradle/main/examples/demo/build.gradle.kts -o build.gradle.kts
gradle bootstrap
./gradlew run
```

## Quick Start

Add the `kradle` plugin to your build script.

- App projects: `net.bitsandbobs.kradle-app`
- Library projects: `net.bitsandbobs.kradle-lib`

_build.gradle.kts_

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("net.bitsandbobs.kradle-app") version "1.1.0"
}

group = "com.example"
version = "1.0.0"

kradle {
    mainClass("com.example.demo.App")
}
```

Make sure you apply the Kotlin plugin before `kradle`.

For new projects, you can run `gradle boostrap` to initialize Git, add Gradle wrapper and create essential directories
and files.

## Tasks

Many tasks are provided by 3rd party plugins. These plugins are applied and configured by `kradle`.

| Task | Description | Alias for | Plugin |
|---|---|---|---|
| [bootstrap](#bootstrap-project) | Bootstraps app/lib project | - | - |
| [dev](#automatic-restarts) | Runs the application and stops it when sources change (use with `-t`, kradle-app only) | - | - |
| [showDependencyUpdates](#check-for-dependency-updates) | Displays dependency updates | - | [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |
| [lint](#static-code-analysis) | Runs [ktlint](https://ktlint.github.io/) | ktlintCheck | [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint) |
| [analyzeCode](#static-code-analysis) | Runs [detekt](https://detekt.github.io/detekt/) code analysis | - | [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt) |
| [generateDetektConfig](#static-code-analysis) | Generates _detekt-config.yml_ | - | - |
| [analyzeDependencies](#scan-for-vulnerabilities-in-dependencies) | Analyzes dependencies for vulnerabilities | - | [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck) |
| [runBenchmarks](#run-jmh-benchmarks) | Runs all [JMH](https://github.com/openjdk/jmh) benchmarks | benchmark | [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark) |
| [integrationTest](#testing) | Runs the integration tests | - | - |
| [functionalTest](#testing) | Runs the functional tests | - | - |
| [generateDocumentation](#generate-documetnationo) | Generates [Dokka](https://kotlin.github.io/dokka/) HTML documentation | - | [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka) |
| [package](#packaging) | Creates JAR | jar | [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html) |
| [uberJar](#packaging) | Creates Uber-JAR (kradle-app only) | - | [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow) |
| [buildImage](#generate-docker-image) | Builds Docker image (kradle-app only) | - | [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib) |
| [install](#quality-of-life-improvements) | Installs JAR to local Maven repository (kradle-lib only) |  publishToMavenLocal | [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html) |
| [generateGitignore](#quality-of-life-improvements) | Generates _.gitignore_ | - | - |
| [generateBuildProperties](#quality-of-life-improvements) | Generates _build.properties_ | - | - |

## Configuration

This example configuration shows all available options. If __not__ commented, the values represent the defaults.

```kotlin
kradle {
    targetJvm("16")
    // mainClass("com.example.demo.App", jvmName = false)
    kotlinxCoroutinesVersion("1.5.2")
    ktlintVersion("0.42.1")
    detektVersion("1.18.1")
    jmhVersion("1.33")
    tests {
        junitJupiterVersion("5.8.0")
        jacocoVersion("0.8.7")
        // useKotest("4.6.3")
        // useMockk("1.12.0")
    }
    uberJar {
        minimize(false)
    }
    image {
        baseImage("bellsoft/liberica-openjdk-alpine:17")
        // ports.add(8080)
        // jvmOpts("-Xmx512M")
        // withAppSh()
        // withJvmKill("1.16.0")
    }

    // disable(BuildPropertiesBlueprint::class.java)
}
```

### Blueprints

Plugins used by `kradle` are configured by so-called _blueprints_. If you want to manually configure a plugin, you
should disable the blueprint,

```kotlin
kradle {
    disable(BuildPropertiesBlueprint::class.java)
}
```

## Bootstrap project

To help you to get started with new projects, `kradle` provides the task `bootstrap`.

- Initializes Git
- Adds Gradle wrapper
- Creates essentials directories and files

## Automatic restarts

_net.bitsandbobs.kradle-app only_

The task `dev` watches the directories `src/main/kotlin` and `src/main/resources`. If changes are detected, the
application is stopped. Should be used with continuous build flag `-t` to archive automatic rebuilds and restarts.

When launching the application with `dev`, the environment variable `DEV_MODE=true` is set. To speed up application
start, the JVM flag `-XX:TieredStopAtLevel=1` is used.

Plugins: [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

## Check for dependency updates

The task `showDependencyUpdates` shows all available dependency updates. It only considers stable versions, no release
candidates or milestone builds.

Plugins: [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)

## Static code analysis

The task `lint` runs [ktlint](https://ktlint.github.io/) on the project. It uses the standard rule set (including
experimental rules) with one exception: Wildcard imports are allowed.

The ktlint version is configurable.

```kotlin
kradle {
    ktlintVersion("0.42.1")
}
```

The task `analyzeCode` runs [detekt](https://detekt.github.io/detekt/) static code analysis. detekt can be configured
with the file _detekt-config.yml_ in the project root directory. `generateDetektConfig` can be used to generate a
configuration file with sane defaults.

The detekt version is configurable.

```kotlin
kradle {
    detektVersion("1.18.1")
}
```

`lint` and `analyzeCode` are executed when running `check`.

Plugins: [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)
, [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt)

## Scan for vulnerabilities in dependencies

The task `analyzeDependencies` scans all dependencies on the runtime and compile classpath for vulnerabilities.

Plugins: [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)

## Run JMH benchmarks

The `runBenchmarks` task runs all [JMH](https://github.com/openjdk/jmh) benchmarks found under `src/benchmark/kotlin`.

The JMH version is configurable.

```kotlin
kradle {
    jmhVersion("1.33")
}
```

Plugins: [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark)

## Testing

[JUnit Jupiter](https://junit.org/junit5/) is set up for running tests. The version is configurable.

```kotlin
kradle {
    tests {
        junitJupiterVersion("5.8.0")
    }
}
```

There are convenience methods to add [kotest](https://kotest.io/)
and [mockk](https://mockk.io/) dependencies.

```kotlin
kradle {
    tests {
        useKotest("4.6.3")
        useMockk("1.12.0")
    }
}
```

Test file names can end with `Test`, `Tests` or `IT`.

`kradle` adds `integrationTest` and `functionalTest`, which run tests under `src/integrationTest`
and `src/functionalTest`.

Both are executed when running `check`.

Running tests always generates [JaCoCo](https://www.jacoco.org/jacoco/) code coverage reports. They can be found
under `build/reports/jacoco/`.

The JaCoCo version is configurable.

```kotlin
kradle {
    tests {
        jacocoVersion("0.8.7")
    }
}
```

Plugins: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
, [JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html),
[Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)

## Generate documentation

The `generateDocumentation` task uses [Dokka](https://kotlin.github.io/dokka/) to generate a HTML documentation based on
KDoc comments. The documentation can be found under `build/docs`.

Package and module documentation can be placed in files _package.md_ or _module.md_ in the project or any source
directory.

Plugins: [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka)

## Packaging

_net.bitsandbobs.kradle-app only_

The `Main-Class` entry will be added to the manifest, so the JAR is runnable. If you use the `@JvmName` annotation, make
sure to set the according flag in the configuration.

```kotlin
kradle {
    mainClass("com.example.demo.CustomApp", jvmName = true)
}
```

The task `uberJar` creates an Uber-Jar. This is a JAR containing all dependencies.

The resulting JAR can be minimized, so only required classes are added.

```kotlin
kradle {
    uberJar {
        minimize(true)
    }
}
```

Plugins: [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

## Create Docker image

_net.bitsandbobs.kradle-app only_

The task `buildImage` creates a Docker image using [Jib](https://github.com/GoogleContainerTools/jib). The base image,
exposed ports and JVM command-line options are configurable.

```kotlin
kradle {
    image {
        baseImage("bellsoft/liberica-openjdk-alpine:17")
        // ports.add(8080)
        // jvmOpts("-Xmx512M")
    }
}
```

Files in `src/main/extra/` will be copied to the image directory `/app/extra/`.

There are two more options to further customize the image.

`withAppSh` will use a script as entrypoint for the container. You can provide your own script
in `src/main/extra/app.sh`. If you don't, `kradle` will create one for you.

`withJvmKill` adds [jvmkill](https://github.com/airlift/jvmkill) to the image. jvmkill terminates the JVM if it is
unable to allocate memory.

```kotlin
kradle {
    image {
        // withAppSh()
        // withJvmKill("1.16.0")
    }
}
```

Plugins: [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib)

## Quality of life improvements

`sourceCompatibility` and `targetCompatibility` are set based on one property.

```kotlin
kradle {
    targetJvm("16")
}
```

Kotlin Standard Library, Kotlin reflection library, kotlin.test library and coroutines dependencies are added. The
coroutines version is configurable.

```kotlin
kradle {
    kotlinxCoroutinesVersion("1.5.2")
}
```

[Opt-ins](https://kotlinlang.org/docs/opt-in-requirements.html) are enabled.

Report [JSR-305](https://jcp.org/en/jsr/detail?id=305) nullability mismatch as error (`"-Xjsr305=strict"`).

The task `generateBuildProperties` generates a file _build.properties_ containing the project name, group, version,
build timestamp and Git commit id. The task is also executed after `processResources`.

```properties
project.name=...
project.group=...
project.version=...
build.timestamp=...
git.commit-id=...
```

The task `generateGitignore` generates _.gitignore_ with sane defaults.

`gitCommit` is added to the project properties

`kradle` looks for a file called _project.properties_ in the project directory. If found, the entries are added to the
project properties.

The task `install` installs the library to your local Maven repository (kradle-lib only).

Plugins: [Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html)
, [kotlinx.serialization Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization)
, [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen)
, [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)

## Changelog

### Version 1.x

- The tasks `showDependencyUpdates`, `analyzeCode`, `analyzeDependencies`, `generateDocumentation`,
  `uberJar` and `buildImage` are no longer aliases. Instead, they are independent tasks.
- Support use of `@JvmName`
- Automatically add `kotlin-reflect` to project dependencies
- __Breaking change__: `run` no longer sets `DEV_MODE=true`
- JMH and detekt versions are now configurable
- Strict JSR-305 processing
- Fix package statement of main class generated by `bootstrap`
- __Breaking change__: The JAR created by `uberJar` is no longer minimized by default
- __Breaking change__: Content of _build.properties_ generated by `generateBuildProperties` changed

  Before

  ```properties
  version=...
  timestamp=...
  git.commit-id=...
  ```

  After

  ```properties
  project.name=...
  project.group=...
  project.version=...
  build.timestamp=...
  git.commit-id=...
  ```

### Version 1.1.0 (2021-09-09)

- New task `bootstrap`: Bootstraps new app/lib project
- New task `dev`: Runs the application and stops it when sources change (for automatic rebuilds and restarts)
- New task `generateGitignore`: Generates _.gitignore_
- Added source sets and tasks for integration and functional tests
- module.md and package.md for Dokka can also be placed in any source directory
- Syntactic sugar: Added methods for configuration

  Before

    ```kotlin
    kradle {
        targetJvm.set("16")
    }
    ```

  After

    ```kotlin
    kradle {
        targetJvm("16")
    }
    ```

- Added configuration for main class

  Before

    ```kotlin
    application {
        mainClass.set("com.example.demo.AppKt")
    }
    ```

  After

    ```kotlin
    kradle {
        mainClass("com.example.demo.App")
    }
    ```


- Moved JaCoCo version to `tests`

  Before

    ```kotlin
    kradle {
        jacocoVersion("0.8.7")
    }
    ```

  After

    ```kotlin
    kradle {
        tests {
            jacocoVersion("0.8.7")
        }
    }
    ```