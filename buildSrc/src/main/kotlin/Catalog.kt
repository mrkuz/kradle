object Catalog {

    object Versions {
        const val jvm = "17"
        const val kotlin = "1.6.0"
        const val jmh = "1.34"
        const val jvmKill = "1.16.0"
        const val detekt = "1.19.0"
        const val ktlint = "0.43.2"
        const val kotlinCoroutines = "1.6.0"
        const val kotest = "5.0.3"
        const val mockk = "1.12.2"
        const val junit = "5.8.2"
        const val jacoco = "0.8.7"
        const val checkstyle = "9.2.1"
        const val pmd = "6.41.0"
        const val spotbugs = "4.5.3"
        const val findSecBugs = "1.11.0"
        const val fbContrib = "7.4.7"
        const val findBugs = "3.0.1"
        const val slf4j = "1.7.32"
    }

    object Dependencies {

        private val _artifacts = mutableSetOf<Artifact>()
        val artifacts
            get(): Set<Artifact> {
                // Make sure objects are initialized
                Platform
                Tools
                Test
                return _artifacts.toSet()
            }

        object Platform {
            val kotlin = artifact("org.jetbrains.kotlin", "kotlin-bom", Versions.kotlin)
        }

        val kotlinCoroutines = artifact("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.kotlinCoroutines)
        val kotlinStdlib = artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", Versions.kotlin)
        val kotlinReflect = artifact("org.jetbrains.kotlin", "kotlin-reflect", Versions.kotlin)

        object Tools {
            val kotlinxBenchmarkRuntime = artifact(
                "org.jetbrains.kotlinx",
                "kotlinx-benchmark-runtime",
                Build.Versions.kotlinxBenchmarkPlugin
            )
            val detekt = artifact("io.gitlab.arturbosch.detekt", "detekt-cli", Versions.detekt)
            val checkstyle = artifact("com.puppycrawl.tools", "checkstyle", Versions.checkstyle)
            val pmd = artifact("net.sourceforge.pmd", "pmd-java", Versions.pmd)
            val findSecBugs = artifact("com.h3xstream.findsecbugs", "findsecbugs-plugin", Versions.findSecBugs)
            val fbContrib = artifact("com.mebigfatguy.sb-contrib", "sb-contrib", Versions.fbContrib)
            val findBugsAnnotations = artifact("com.google.code.findbugs", "annotations", Versions.findBugs)
            val slf4jSimple = artifact("org.slf4j", "slf4j-simple", Versions.slf4j)

            // Not directly referred, but still used in kradle
            val jvmKill = artifact("org.cloudfoundry", "jvmkill", Versions.jvmKill)
            val jmh = artifact("org.openjdk.jmh", "jmh-core", Versions.jmh)
            val jacoco = artifact("org.jacoco", "org.jacoco.core", Versions.jacoco)
            val ktlint = artifact("com.pinterest", "ktlint", Versions.ktlint)
            val spotbugs = artifact("com.github.spotbugs", "spotbugs", Versions.spotbugs)
        }

        object Test {
            val junitApi = artifact("org.junit.jupiter", "junit-jupiter-api", Versions.junit)
            val junitEngine = artifact("org.junit.jupiter", "junit-jupiter-engine", Versions.junit)
            val kotlinTest = artifact("org.jetbrains.kotlin", "kotlin-test", Versions.kotlin)
            val kotestJunit5 = artifact("io.kotest", "kotest-runner-junit5", Versions.kotest)
            val kotestAssertions = artifact("io.kotest", "kotest-assertions-core", Versions.kotest)
            val mockk = artifact("io.mockk", "mockk", Versions.mockk)
        }

        fun artifact(group: String, name: String, version: String): String {
            val artifact = Artifact(group, name, version)
            _artifacts.add(artifact)
            return "${artifact.group}:${artifact.name}"
        }
    }

    object Build {

        object Versions {
            const val kotlin = "1.5.31"
            const val kotest = "4.6.3"
            const val mockk = "1.12.2"
            const val kotlinxBenchmarkPlugin = "0.4.1"
        }

        object Plugins {
            val kotlinJvm = Plugin("org.jetbrains.kotlin.jvm", Versions.kotlin)
            val gradlePublish = Plugin("com.gradle.plugin-publish", "0.19.0")
            val testLogger = Plugin("com.adarshr.test-logger", "3.1.0")
        }

        object Dependencies {

            object Platform {
                const val kotlin = "org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"
            }

            const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
            const val jgit = "org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r"

            object Plugins {
                const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
                const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:1.5.31"
                const val allOpen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"
                const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
                const val kotlinBenchmark =
                    "org.jetbrains.kotlinx:kotlinx-benchmark-plugin:${Versions.kotlinxBenchmarkPlugin}"
                const val testLogger = "com.adarshr:gradle-test-logger-plugin:3.1.0"
                const val shadow = "gradle.plugin.com.github.jengelman.gradle.plugins:shadow:7.0.0"
                const val jib = "gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.1.4"
                const val versions = "com.github.ben-manes:gradle-versions-plugin:0.41.0"
                const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0"
                const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.2.1"
                const val owaspDependencyCheck = "org.owasp:dependency-check-gradle:6.5.1"

                // Java
                const val spotbugs = "com.github.spotbugs.snom:spotbugs-gradle-plugin:5.0.4"
            }

            object Test {
                const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
                const val mockk = "io.mockk:mockk:${Versions.mockk}"
                const val kotestJunit5 = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
                const val kotestProperty = "io.kotest:kotest-property:${Versions.kotest}"
                const val dockerJava = "com.github.docker-java:docker-java:3.2.12"

                val kotestBundle = setOf(kotestJunit5, kotestProperty)
            }
        }

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
                "com.thoughtworks.xstream:xstream:1.4.18",
                "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}!!",
                "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}!!",
                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}!!",
                "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}!!",
                "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}!!"
            )
        }
    }

    data class Plugin(val id: String, val version: String)

    data class Artifact(val group: String, val name: String, val version: String)
}
