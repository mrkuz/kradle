object Catalog {

    object Versions {
        const val jvm = "21"
        const val kotlin = "1.9.20"
        const val jmh = "1.37"
        const val tini = "0.19.0"
        const val jvmKill = "1.16.0"
        const val detekt = "1.23.3"
        const val ktlint = "1.0.1"
        const val kotlinCoroutines = "1.7.3"
        const val kotlinxBenchmarkPlugin = "0.4.9"
        const val kotest = "5.7.2"
        const val mockk = "1.13.8"
        const val junit = "5.10.0"
        const val jacoco = "0.8.11"
        const val checkstyle = "10.12.4"
        const val pmd = "6.55.0"
        const val spotbugs = "4.8.0"
        const val findSecBugs = "1.12.0"
        const val fbContrib = "7.6.0"
        const val findBugs = "3.0.1"
        const val slf4j = "2.0.9"
        const val archUnit = "1.1.0"
        const val testcontainers = "1.19.1"
        const val lombok = "1.18.30"
        const val guava = "32.1.3-jre"
        const val caffeine = "3.1.8"
        const val log4j = "2.21.1"
        const val springBoot = "3.1.5"
        const val gradleForIntegrationTests = "8.0"
        const val gradleForFunctionalTests = "8.5"
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
            val springBoot = artifact("org.springframework.boot", "spring-boot-dependencies", Versions.springBoot)
        }

        val kotlinCoroutines = artifact("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.kotlinCoroutines)
        val kotlinStdlib = artifact("org.jetbrains.kotlin", "kotlin-stdlib", Versions.kotlin)
        val kotlinReflect = artifact("org.jetbrains.kotlin", "kotlin-reflect", Versions.kotlin)
        val lombok = artifact("org.projectlombok", "lombok", Versions.lombok)
        val guava = artifact("com.google.guava", "guava", Versions.guava)
        val caffeine = artifact("com.github.ben-manes.caffeine", "caffeine", Versions.caffeine)
        val caffeineGuava = artifact("com.github.ben-manes.caffeine", "guava", Versions.caffeine)
        val log4jApi = artifact("org.apache.logging.log4j", "log4j-api", Versions.log4j)
        val log4jCore = artifact("org.apache.logging.log4j", "log4j-core", Versions.log4j)
        val log4jSlf4j = artifact("org.apache.logging.log4j", "log4j-slf4j2-impl", Versions.log4j)
        val slf4jApi = artifact("org.slf4j", "slf4j-api", Versions.slf4j)
        val slf4jSimple = artifact("org.slf4j", "slf4j-simple", Versions.slf4j)

        val springBootStarter = artifact("org.springframework.boot", "spring-boot-starter", Versions.springBoot)
        val springBootStarterWeb = artifact("org.springframework.boot", "spring-boot-starter-web", Versions.springBoot)
        val springBootStarterWebFlux =
            artifact("org.springframework.boot", "spring-boot-starter-webflux", Versions.springBoot)
        val springBootStarterActuator =
            artifact("org.springframework.boot", "spring-boot-starter-actuator", Versions.springBoot)
        val jacksonModuleKotlin = artifact("com.fasterxml.jackson.module", "jackson-module-kotlin", "")
        val reactorKotlinExtensions = artifact("io.projectreactor.kotlin", "reactor-kotlin-extensions", "")
        val kotlinxCoroutinesReactor = artifact("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", "")

        object Tools {
            val kotlinxBenchmarkRuntime = artifact(
                "org.jetbrains.kotlinx",
                "kotlinx-benchmark-runtime",
                Versions.kotlinxBenchmarkPlugin
            )
            val detekt = artifact("io.gitlab.arturbosch.detekt", "detekt-cli", Versions.detekt)
            val checkstyle = artifact("com.puppycrawl.tools", "checkstyle", Versions.checkstyle)
            val pmd = artifact("net.sourceforge.pmd", "pmd-java", Versions.pmd)
            val findSecBugs = artifact("com.h3xstream.findsecbugs", "findsecbugs-plugin", Versions.findSecBugs)
            val fbContrib = artifact("com.mebigfatguy.sb-contrib", "sb-contrib", Versions.fbContrib)
            val findBugsAnnotations = artifact("com.google.code.findbugs", "annotations", Versions.findBugs)
            val springBootDevtools = artifact("org.springframework.boot", "spring-boot-devtools", Versions.springBoot)

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
            val archUnit = artifact("com.tngtech.archunit", "archunit", Versions.archUnit)
            val archUnitJunit5 = artifact("com.tngtech.archunit", "archunit-junit5", Versions.archUnit)
            val testcontainers = artifact("org.testcontainers", "testcontainers", Versions.testcontainers)
            val testcontainersJunit5 = artifact("org.testcontainers", "junit-jupiter", Versions.testcontainers)

            val springBootStarterTest =
                artifact("org.springframework.boot", "spring-boot-starter-test", Versions.springBoot)
            val reactorTest = artifact("io.projectreactor", "reactor-test", "")
        }

        fun artifact(group: String, name: String, version: String): String {
            val artifact = Artifact(group, name, version)
            _artifacts.add(artifact)
            return "${artifact.group}:${artifact.name}"
        }
    }

    data class Artifact(val group: String, val name: String, val version: String)
}
