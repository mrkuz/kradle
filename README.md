
# Kradle

Swiss army knive for Kotlin development.

This project aims to provide a solution for common Kotlin development tasks using Gradle.

- [Check for dependency updates](#check-for-dependency-updates)
- [Static code analyisis](#static-code-analysis)
- [Scan for vulnerabilities in dependencies](#scan-for-vulnerabilities-in-dependencies)
- [Run JMH benchmarks](#run-jmh-benchmarks)
- [Testing](#testing)
- [Generate documentation](#generate-documentation)
- [Packaging](#packaging)
- [Create Docker images](#create-docker-image)

It utilizies many other Gradle plugins. We will refer to them as vendor plugins.
kradle takes care of applying and configuring them.

So instead of fiddling with your build script, you just need to apply one plugin.

- For Kotlin apps: net.bnb1.kradle-app
- For Kotlin libs: net.bnb1.kradle-lib

## Quickstart

kradle is currently not available at [Gradle Plugin Portal](https://plugins.gradle.org/), so you need to install it to your local Maven repository.

```shell
./gradlew publishToMavenLocal
```

In your project, add the local repository to your repositories list.

`settings.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "demo"
```

And finally apply the plugin.

`build.gradle.kts`

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("net.bnb1.kradle-app") version "1.0.0-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

application {
    mainClass.set("com.example.DemoAppKt")
}
```

## Tasks

| Task | Description | Alias for |
|---|---|---
| [showDependencyUpdates](#dependencyupdatesblueprint) | Displays dependency updates | dependencyUpdates
| [lint](#ktlintblueprint) | Runs ktlint | ktlintCheck |
| [analyzeCode](#detektblueprint) | Runs detekt code analysis | detekt |
| [analyzeDependencies](#dependencycheckblueprint) | Analyzes dependencies for vulnerabilities | dependencyCheckAnalyze |
| [runBenchmarks](#benchmarksblueprint) | Runs all JMH benchmarks | benchmark |
| [generateDocumentation](#dokkablueprint) | Generates Dokka HTML documentation | dokkaHtml |
| [package](#javablueprint) | Creates JAR | jar |
| [buildImage](#jibblueprint) | Builds Docker image (kradle-app only) | jibDockerBuild |
| [uberJar](#shadowblueprint) | Creates Uber-JAR (kradle-app only) | shadowJar |
| [install](#mavenpublishblueprint) | Installs JAR to local Maven repository (kradle-lib only) | publishToMavenLocal |
| [generateBuildProperties](#buildpropertiesblueprint) | Generates build.properties | - |


## Configuration

kradle provides an extension for configuration. These are all available options. The lines not commented out represent the defaults.

```kotlin
kradle {
    targetJvm.set("16")
    kotlinxCoroutinesVersion.set("1.5.1")
    junitJupiterVersion.set("5.7.2")
    // useKotest()
    image {
        baseImage.set("bellsoft/liberica-openjdk-alpine:16")
        // ports.add(8080)
        // useAppSh()
        // useJvmKill("1.16.0")
    }

    // disable(BuildPropertiesBlueprint::class.java)
}
```

You will find a more detailed explanation in the upcoming sections.

## Blueprints

Configuration of vendor plugins is handled by classes called _blueprints_.

If you want do manually configure a plugin, you should disable the blueprint to avoid conflicts.

Example:

```kotlin
kradle {
    disable(JibBlueprint::class.java)
}
```

## Check for dependency updates

### DependencyUpdatesBlueprint

Plugin: [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)

> Gradle plugin that provides tasks for discovering dependency updates.

#### Description



#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Static code analyisis

### KtlintBlueprint

Plugin: [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)

> Provides a convenient wrapper plugin over the ktlint project.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### DetektBlueprint

Plugin: [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt)

> Static code analysis for Kotlin.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Scan for vulnerabilities in dependencies

### DependencyCheckBlueprint

Plugin: [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)

> OWASP dependency-check-gradle plugin.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Run JMH benchmarks

### BenchmarksBlueprint

Plugin: [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark)

> Toolkit for running benchmarks for multiplatform Kotlin code.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Testing

### TestBlueprint

Plugin: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

> The Java plugin adds Java compilation along with testing and bundling capabilities to a project.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### JacocoBlueprint

Plugin: [JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)

> The JaCoCo plugin provides code coverage metrics for Java code via integration with JaCoCo.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Generate documentation

### DokkaBlueprint

Plugin: [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka)

> Dokka, the documentation engine for Kotlin.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Packaging

### ShadowBlueprint

_net.bnb1.kradle-app only_

Plugin: [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

> A Gradle plugin for collapsing all dependencies and project code into a single Jar file.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Create Docker images

### JibBlueprint

_net.bnb1.kradle-app only_

Plugin: [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib)

> Containerize your Java application.

#### Description
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Miscellaneous

### JavaBlueprint

Plugin: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

> The Java plugin adds Java compilation along with testing and bundling capabilities to a project.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### KotlinBlueprint

Plugin: [Kotlin Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm)

> Kotlin plugins for Gradle.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### ApplicationBlueprint

_net.bnb1.kradle-app only_

Plugin: [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

> The Application plugin facilitates creating an executable JVM application.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### MavenPublishBlueprint

_net.bnb1.kradle-app only_

Plugin: [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)

> The Maven Publish Plugin provides the ability to publish build artifacts to an Apache Maven repository.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### BuildPropertiesBlueprint

Plugin: internal

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

## Other plugins

### Serialization Plugin

Plugin: [kotlinx.serialization Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization)

> Kotlin compiler plugin for kotlinx.serialization library.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### All-open Plugin

Plugin: [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen)

> Kotlin plugins for Gradle.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### Test Logger Plugin

Plugin: [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)

> A Gradle plugin for printing beautiful logs on the console while running tests.

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### Git Plugin

Plugin: internal

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### Project Properties Plugin

Plugin: internal

#### Description
#### Tasks
#### Aliases
#### Project properties
#### Configuration
#### Dependencies
#### Environment variables

### Java Library Plugin

Plugin: [Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html)

> The Java Library plugin expands the capabilities of the Java plugin by providing specific knowledge about Java libraries
