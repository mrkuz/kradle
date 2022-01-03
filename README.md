# Kradle

Swiss army knife for Kotlin/JVM development.

`kradle` is a Gradle plugin, which sets up your Kotlin/JVM (or Java) project in no time.

With a few lines of configuration, you will be able to:

- [Bootstrap new projects](#bootstrapping)
- [Check for dependency updates](#dependency-updates)
- [Run vulnerability scans](#vulnerability-scans)
- [Run static code analysis](#code-analysis)
- [Add automatic restart on code change](#development-mode)
- [Add support for integration and functional testing](#test-improvements)
- [Run JMH benchmarks](#benchmarks)
- [Create Uber-Jars](#packaging)
- [Create Docker images](#docker)
- [Generate documentation](#documentation)

Most of the functionality is provided by other well-known plugins. `kradle` just takes care of the setup and provides a unified configuration DSL.

## What's new?

See [CHANGELOG](CHANGELOG.md).

## (Very) Quick Start

```shell
mkdir demo && cd demo
curl -O https://raw.githubusercontent.com/mrkuz/kradle/main/examples/app/settings.gradle.kts
curl -O https://raw.githubusercontent.com/mrkuz/kradle/main/examples/app/build.gradle.kts
gradle bootstrap
```

Run application:

`./gradlew run`

Package application and run JAR:

`./gradlew uberJar && java -jar build/libs/demo-1.0.0-uber.jar`

Build Docker image and run container:

`./gradlew buildImage && docker run --rm demo`


## Quick Start

Add the `kradle` plugin to your build script: `net.bitsandbobs.kradle`.

_build.gradle.kts_

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {
    kotlinJvmApplication {
        jvm {
            application {
                mainClass("com.example.demo.AppKt")
            }
        }
    }
}
```

Make sure you apply the Kotlin plugin before `kradle`. For applications, you also have to provide the `mainClass`.

If you are starting from scratch, you can run `gradle boostrap` to initialize Git, add Gradle wrapper and create essential directories
and files.

With `kradle` applied, many new [tasks](#tasks) become available.

The example above uses the Kotlin/JVM application [preset](#presets).

For full configuration reference see [Appendix A](#appendix-a-configuration-reference).

## Tasks

| Task | Description | Alias for | Plugins used |
|---|---|---|---|
| [bootstrap](#bootstrapping) | Bootstraps app/lib project | - | - |
| [showDependencyUpdates](#dependency-updates) | Displays dependency updates | - | [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |
| [lint](#linting) | Runs [ktlint](https://ktlint.github.io/) (Kotlin) and [checkstyle](https://checkstyle.sourceforge.io/) (Java) | - | [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint), [Checkstyle Plugin](https://docs.gradle.org/current/userguide/checkstyle_plugin.html) |
| [generateCheckstyleConfig](#code-analysis) | Generates _checkstyle.xml_ | - | - |
| [analyzeCode](#code-analysis) | Runs [detekt](https://detekt.github.io/detekt/) (Kotlin), [PMD](https://pmd.github.io/) (Java) and [SpotBugs](https://spotbugs.github.io/) (Java) code analysis | - | [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt), [PMD Plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html), [SpotBugs Plugin](https://plugins.gradle.org/plugin/com.github.spotbugs) |
| [generateDetektConfig](#code-analysis) | Generates _detekt-config.yml_ | - | - |
| [analyzeDependencies](#vulnerability-scans) | Analyzes dependencies for vulnerabilities | - | [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck) |
| [dev](#development-mode) | Runs the application and stops it when sources change (use with `-t`, applications only) | - | - |
| [runBenchmarks](#benchmarks) | Runs [JMH](https://github.com/openjdk/jmh) benchmarks | benchmark | [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark) |
| [integrationTest](#test-improvements) | Runs integration tests | - | - |
| [functionalTest](#test-improvements) | Runs functional tests | - | - |
| [generateDocumentation](#documentation) | Generates [Dokka](https://kotlin.github.io/dokka/) HTML documentation | - | [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka) |
| [package](#packaging) | Creates JAR | jar | [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html) |
| [uberJar](#packaging) | Creates Uber-JAR (applications only) | - | [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow) |
| [buildImage](#docker) | Builds Docker image (applications only) | - | [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib) |
| [install](#library-development) | Installs JAR to local Maven repository (libraries only) |  publishToMavenLocal | [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html) |
| [generateGitignore](#git-integration) | Generates _.gitignore_ | - | - |
| [generateBuildProperties](#build-properties) | Generates _build.properties_ | - | - |

---

## Features

Features provided by `kradle` must be enabled explicitly. For example to enable benchmarks:

```kotlin
kradle {
    jvm {
        benchmarks.enable()
    }
}
```

If the feature has options, you can pass in a configuration code block. This configures and enables the feature.

```kotlin
kradle {
    jvm {
        benchmark {
            jmhVersion("1.34")
        }
    }
}
```

### Bootstrapping

```kotlin
kradle {
    general {
        bootstrap.enable()
    }
}
```

Adds the task `bootstrap`, which

- Initializes Git
- Adds Gradle wrapper
- Creates essentials directories and files

### Git integration

```kotlin
kradle {
    general {
        git.enable()
    }
}
```

Adds the task `generateGitignore`, which generates _.gitignore_ with sane defaults.

`gitCommit` is added to the project properties.

### Project properties

```kotlin
kradle {
    general {
        projectProperties.enable()
    }
}
```

Looks for a file called _project.properties_ in the project directory. If found, the entries are added to the
project properties.

### Build properties

```kotlin
kradle {
    general {
        buildProperties.enable()
    }
}
```

Adds the task `generateBuildProperties`, which generates a file _build.properties_ containing the project name, group, version and the
build timestamp.

If [Git integration](#git-integration) is enabled, the Git commit id is also added.

The task is executed after `processResources`.

```properties
project.name=...
project.group=...
project.version=...
build.timestamp=...
git.commit-id=...
```

### JVM

```kotlin
kradle {
    jvm {
        ...
    }
}
```

Groups JVM related features.

#### Options

```kotlin
kradle {
    jvm {
        targetJvm("17")
    }
}
```

- `targetJvm`: Sets target release (`"--release"`)

### Kotlin development

```kotlin
kradle {
    jvm {
        kotlin.enable()
    }
}
```

Adds Kotlin Standard Library, Kotlin reflection library, and kotlin.test library dependencies.

Enables [Opt-ins](https://kotlinlang.org/docs/opt-in-requirements.html).

[JSR-305](https://jcp.org/en/jsr/detail?id=305) nullability mismatches are reported as error (`"-Xjsr305=strict"`).

Plugins used: [kotlinx.serialization Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization)
, [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen),
 [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

#### Options

```kotlin
kradle {
    jvm {
        kotlin {
            useCoroutines(/* "1.6.0" */)
            lint {
                ktlint {
                    version("0.43.2")
                    rules {
                        disable(...)
                    }
                }
            }
            codeAnalysis {
                detekt {
                    version("1.19.0")
                    configFile("detekt-config.yml")
                }
            }
            test {
                useKotest(/* "5.0.3" */)
                useMockk(/* "1.12.2" */)
            }
        }
    }
}
```

- `useCoroutines`: Adds Kotlin coroutines dependency
- `ktlint.version`: [ktlint](https://ktlint.github.io/) version used for [linting](#linting) (if enabled)
- `ktlint.disable`: Disable [ktlint](https://ktlint.github.io/) rule. Can be called multiple times
- `detekt.version`: [detekt](https://detekt.github.io/detekt/) version used for [static code analysis](#code-analysis) (if enabled)
- `detekt.configFile`: [detekt](https://detekt.github.io/detekt/) configuration file used
- `useKoTest`: Adds [kotest](https://kotest.io/) test dependencies (if [test improvements](#test-improvements) are enabled)
- `useMockk`: Adds [mockk](https://mockk.io/) test dependency (if [test improvements](#test-improvements) are enabled)

### Java development

```kotlin
kradle {
    jvm {
        java.enable()
    }
}
```

Plugins used: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

#### Options

```kotlin
kradle {
    jvm {
        java {
            withPreviewFeatures(true)
            lint {
                checkstyle {
                    version("9.2.1")
                    configFile("checkstyle.xml")
                }
            }
            codeAnalysis {
                pmd {
                    version("6.41.0")
                    ruleSets {
                        bestPractices(false)
                        codeStyle(false)
                        design(false)
                        documentation(false)
                        errorProne(true)
                        multithreading(true)
                        performance(true)
                        security(true)
                    }
                }
                spotBugs {
                    version("4.5.2")
                    useFbContrib(/* 7.4.7 */)
                    useFindSecBugs(/* 1.11.0 */)
                }
            }
        }
    }
}
```

- `withPreviewFeatures`: Enable preview features
- `checkstyle.version`: [checkstyle](https://checkstyle.sourceforge.io/) version used for [linting](#linting) (if enabled)
- `checkstyle.configFile`: [checkstyle](https://checkstyle.sourceforge.io/) configuration file used
- `pmd.version`: [PMD](https://pmd.github.io/) version used for [code analysis](#code-analysis) (if enabled)
- `pmd.ruleSets.*`: Enable/disable [PMD](https://pmd.github.io/) rule sets
- `spotBugs.version`: [SpotBugs](https://spotbugs.github.io/) version used for [code analysis](#code-analysis) (if enabled)
- `spotBugs.useFbContrib`: Use [fb-contrib](http://fb-contrib.sourceforge.net/) plugin
- `spotBugs.useFbContrib`: Use [Find Security Bugs](https://find-sec-bugs.github.io/) plugin

### Application development

```kotlin
kradle {
    jvm {
        application.enable()
    }
}
```

Conflicts with [library development](#library-development).

Plugins used: [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

#### Options

```kotlin
kradle {
    jvm {
        application {
            mainClass("...")
        }
    }
}
```

- `mainClass`: Sets the main class (required)

### Library development

```kotlin
kradle {
    jvm {
        library.enable()
    }
}
```

Adds the task `install`, which installs the library to your local Maven repository.

Conflicts with [application development](#application-development).

Plugins used: [Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html), [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)

### Dependency updates

```kotlin
kradle {
    jvm {
        dependencyUpdates.enable()
    }
}
```

Adds the task `showDependencyUpdates`, which shows all available dependency updates. It only considers stable versions; no release candidates or milestone builds.

Plugins used: [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)

### Vulnerability scans

```kotlin
kradle {
    jvm {
        vulnerabilityScan.enable()
    }
}
```

Adds the task `analyzeDependencies`, which scans all dependencies on the runtime and compile classpath for vulnerabilities.

Plugins used: [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)

### Linting

```kotlin
kradle {
    jvm {
        lint.enable()
    }
}
```

Adds the task `lint`, which runs:

- [ktlint](https://ktlint.github.io/) on Kotlin source files. It uses all standard and experimental rules per default.

- [checkstyle](https://checkstyle.sourceforge.io/) on Java source files. Looks for the configuration file _checkstyle.xml_ in the project root. If not found, `kradle` generates one.

`lint` is executed when running `check`.

Plugins used: [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)

### Code analysis

```kotlin
kradle {
    jvm {
        codeAnalysis.enable()
    }
}
```

Adds the task `analyzeCode`, which runs:

- [detekt](https://detekt.github.io/detekt/) static code analysis for Kotlin

- [PMD](https://pmd.github.io/) code analysis for Java. Enabled rule sets: `errorprone`, `multithreading`, `performance` and `security`

- [SpotBugs](https://spotbugs.github.io/) code analysis for Java

Adds the tasks `generateDetektConfig`, which generates a configuration file with sane defaults.

`analyzeCode` is executed when running `check`.

Plugins used: [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt), [PMD Plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html), [SpotBugs Plugin](https://plugins.gradle.org/plugin/com.github.spotbugs)

### Development mode

```kotlin
kradle {
    jvm {
        developmentMode.enable()
    }
}
```

Adds the task `dev`, which watches the directories _src/main/kotlin_ and _src/main/resource_. If changes are detected, the
application is stopped. Should be used with continuous build flag `-t` to archive automatic rebuilds and restarts.

When launching the application with `dev`, the environment variable `DEV_MODE=true` is set.

To speed up application start, the JVM flag `-XX:TieredStopAtLevel=1` is used.

Requires [application development](#application-development).

### Test improvements

```kotlin
kradle {
    test {
        test.enable()
    }
}
```

Test file names can end with `Test`, `Tests` or `IT`.

Plugins used: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
, [JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html),
[Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)

#### Options

```kotlin
kradle {
    jvm {
        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter(/* "5.8.2" */)
            withJacoco(/* "0.8.7" */)
        }
    }
}
```

- `prettyPrint`: Prettifies test output with [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)
- `withIntegrationTests`: Adds task `integrationTest`, which runs tests under _src/integrationTest_. The task is executed when running `check`.
- `withFunctionalTests`: Adds task `functionalTest`, which runs tests under _src/functionalTest_. The task is executed when running `check`.
- `withJunitJupiter`: Sets up [JUnit Jupiter](https://junit.org/junit5/) for running tests
- `withJacoco`: Generates [JaCoCo](https://www.jacoco.org/jacoco/) code coverage reports after tests. They can be found under _build/reports/jacoco/_.

### Benchmarks

```kotlin
kradle {
    jvm {
        benchmarks.enable()
    }
}
```

Adds the task `runBenchmarks`, which runs [JMH](https://github.com/openjdk/jmh) benchmarks found under _src/benchmark/kotlin_.

Plugins used: [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark), [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen)

#### Options

```kotlin
kradle {
    jvm {
        benchmark {
            jmhVersion("1.34")
        }
    }
}
```

- `jmhVersion`: [JMH](https://github.com/openjdk/jmh) version used

### Packaging

```kotlin
kradle {
    jvm {
        packaging.enable()
    }
}
```

Adds the task `uberJar`, which creates an Uber-Jar. This is a JAR containing all dependencies.

Adds the task `package`, which is an alias for `jar`.

Adds `Main-Class` the manifest, so the JAR is runnable (application only).

Plugins used: [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

#### Options

```kotlin
kradle {
    jvm {
        packaging {
            uberJar {
                minimize(true)
            }
        }
    }
}
```

- `minimize`: Minimizes Uber-Jar, only required classes are added

### Docker

```kotlin
kradle {
    jvm {
        docker.enable()
    }
}
```

Adds the task `buildImage`, which creates a Docker image using [Jib](https://github.com/GoogleContainerTools/jib).

Files in _src/main/extra/_ will be copied to the image directory _/app/extra/_.

Plugins used: [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib)

Requires [application development](#application-development).

#### Options

```kotlin
kradle {
    jvm {
        docker {
            baseImage("bellsoft/liberica-openjdk-alpine:17")
            ports.add(8080)
            jvmOpts("-Xmx1G")
            withJvmKill(/* "1.16.0" */)
            withAppSh(true)
        }
    }
}
```

- `baseImage`: The base image used
- `ports`: List of exposed ports
- `jvmOpts`: Options passed to the JVM
- `withJvmKill`: Adds [jvmkill](https://github.com/airlift/jvmkill) to the image. [jvmkill](https://github.com/airlift/jvmkill) terminates the JVM if it is unable to allocate memory.
- `withAppSh`: Uses a script as entrypoint for the container. You can provide your own script in _src/main/extra/app.sh_. If you don't, `kradle` will create one for you.

### Documentation

```kotlin
kradle {
    jvm {
        documentation.enable()
    }
}
```

Adds the task `generateDocumentation`, which uses [Dokka](https://kotlin.github.io/dokka/) to generate a HTML documentation based on
KDoc comments. The documentation can be found under _build/docs_.

Package and module documentation can be placed in files _package.md_ or _module.md_ in the project or any source directory.

Plugins used: [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka)

## Presets

Presets preconfigure `kradle` for specific use cases.

You can override the configuration. For example, you create a new library, but don't want _build.properties_ to be generated:

```kotlin
kradle {
    kotlinJvmLibrary {
        jvm {
            buildProperties.disable()
        }
    }
}
```

The overridden configuration must be placed inside the preset block. Following will **NOT** work:

```kotlin
kradle {
    kotlinJvmLibrary.activate()
    jvm {
        buildProperties.disable()
    }
}
```

### Kotlin/JVM application

```kotlin
kradle {
    kotlinJvmApplication {
        jvm {
            application {
                mainClass("...")
            }
        }
    }
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        kotlin {
            useCoroutines()
            lint {
                ktlint {
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            test {
                useKotest()
                useMockk()
            }
        }
        application {
            mainClass("...")
        }

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

        benchmark.enable()
        packaging.enable()
        docker {
            withJvmKill()
        }
        documentation.enable()
    }
}
```

### Kotlin/JVM library

```kotlin
kradle {
    kotlinJvmLibrary.activate()
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        kotlin {
            useCoroutines()
            lint {
                ktlint {
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            test {
                useKotest()
                useMockk()
            }
        }
        library.enable()
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

        benchmark.enable()
        packaging.enable()
        documentation.enable()
    }
}
```

### Java application

```kotlin
kradle {
    javaApplication {
        jvm {
            application {
                mainClass("...")
            }
        }
    }
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        java {
            codeAnalysis {
                spotBugs {
                    useFbContrib()
                    useFindSecBugs()
                }
            }
        }
        application {
            mainClass("...")
        }
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

        benchmark.enable()
        packaging.enable()
        docker {
            withJvmKill()
        }
        documentation.enable()
    }
}
```

### Java library

```kotlin
kradle {
    javaLibrary.activate()
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        java {
            codeAnalysis {
                spotBugs {
                    useFbContrib()
                    useFindSecBugs()
                }
            }
        }
        library.enable()
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

        benchmark.enable()
        packaging.enable()
        documentation.enable()
    }
}
```

## Appendix A: Configuration reference

This example configuration shows all available options.

```kotlin
kradle {

    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        targetJvm("17")
        kotlin {
            useCoroutines(/* "1.6.0" */)
            lint {
                ktlint {
                    version("0.43.2")
                    rules {
                        disable("...")
                    }
                }
            }
            codeAnalysis {
                detekt {
                    version("1.19.0")
                    configFile("detekt-config.yml")
                }
            }
            test {
                useKotest(/* "5.0.3" */)
                useMockk(/* "1.12.2" */)
            }
        }
        java {
            withPreviewFeatures(true)
            lint {
                checkstyle {
                    version("9.2.1")
                    configFile("checkstyle.xml")
                }
            }
            codeAnalysis {
                pmd {
                    version("6.41.0")
                    ruleSets {
                        bestPractices(false)
                        codeStyle(false)
                        design(false)
                        documentation(false)
                        errorProne(true)
                        multithreading(true)
                        performance(true)
                        security(true)
                    }
                }
                spotBugs {
                    version("4.5.2")
                    useFbContrib(/* 7.4.7 */)
                    useFindSecBugs(/* 1.11.0 */)
                }
            }
        }
        application {
            mainClass("...")
        }
        // library.enable() // Conflicts with application

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter(/* "5.8.2" */)
            withJacoco(/* "0.8.7" */)
        }

        benchmark {
            jmhVersion("1.34")
        }

        packaging {
            uberJar {
                minimize(true)
            }
        }

        docker {
            baseImage("bellsoft/liberica-openjdk-alpine:17")
            withJvmKill(/* "1.16.0" */)
            withAppSh(true)
            ports.add(8080)
            jvmOpts("-Xmx1G")
        }

        documentation.enable()
    }
}
```
