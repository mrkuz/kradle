object BuildCatalog {

    object Versions {
        const val kotlin = "1.9.10"
        const val kotest = "5.7.2"
        const val testLogger = "4.0.0"
        const val dockerJava = "3.3.4"
    }

    object Plugins {
        val kotlinJvm = Plugin("org.jetbrains.kotlin.jvm", Versions.kotlin)
        val gradlePublish = Plugin("com.gradle.plugin-publish", "1.2.1")
        val testLogger = Plugin("com.adarshr.test-logger", Versions.testLogger)
        val shadow = Plugin("com.github.johnrengelman.shadow", "8.1.1")
        val versions = Plugin("com.github.ben-manes.versions", "0.49.0")
    }

    object Dependencies {

        object Platform {
            const val kotlin = "org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"
        }

        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val jgit = "org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r"

        object Plugins {
            const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
            const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:1.9.10"
            const val allOpen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"
            const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
            const val kotlinBenchmark =
                "org.jetbrains.kotlinx:kotlinx-benchmark-plugin:${Catalog.Versions.kotlinxBenchmarkPlugin}"
            const val testLogger = "com.adarshr:gradle-test-logger-plugin:${Versions.testLogger}"
            const val jib = "gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.2.1"
            const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.3"
            const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:11.6.1"
            const val owaspDependencyCheck = "org.owasp:dependency-check-gradle:8.4.2"
            const val kover = "org.jetbrains.kotlinx:kover:0.6.1"

            val shadow = "gradle.plugin.com.github.jengelman.gradle.plugins:shadow:" +
                BuildCatalog.Plugins.shadow.version
            val versions = "com.github.ben-manes:gradle-versions-plugin:${BuildCatalog.Plugins.versions.version}"

            // Java
            const val spotbugs = "com.github.spotbugs.snom:spotbugs-gradle-plugin:5.2.1"
        }

        object Test {
            const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
            const val kotestJunit5 = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
            const val kotestProperty = "io.kotest:kotest-property:${Versions.kotest}"
            const val dockerJava = "com.github.docker-java:docker-java:${Versions.dockerJava}"
            const val dockerJavaApache =
                "com.github.docker-java:docker-java-transport-httpclient5:${Versions.dockerJava}"
            val kotestBundle = setOf(kotestJunit5, kotestProperty)
            val dockerJavaBundle = setOf(dockerJava, dockerJavaApache)
        }
    }

    data class Plugin(val id: String, val version: String)

    object Constraints {

        val ids = setOf(
            "org.apache.ant:ant:1.10.12",
            "org.apache.ant:ant-launcher:1.10.12",
            // "com.h2database:h2:2.0.204" // Keep old version, the new one breaks OWASP dependency-check-gradle plugin
            "org.apache.httpcomponents:httpclient:4.5.13",
            "org.jdom:jdom2:2.0.6.1",
            "com.google.guava:guava:30.0-jre",
            "org.apache.logging.log4j:log4j-api:2.17.1",
            "org.apache.logging.log4j:log4j-core:2.17.1",
            "org.apache.maven:maven-model:3.0.5",
            "com.thoughtworks.xstream:xstream:1.4.19"
        )
    }
}
