plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {

    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        application {
            mainClass("com.example.demo.AppKt")
        }

        // targetJvm("17")
        kotlin {
            useCoroutines(/* "1.6.0" */)
            lint {
                // ktlintVersion("0.43.2")
            }
            codeAnalysis {
                // detektConfigFile("detekt-config.yml")
                // detektVersion("1.19.0")
            }
            test {
                useKotest(/* "5.0.3" */)
                useMockk(/* "1.12.2" */)
            }
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
            withJunitJupiter(/* "5.8.2" */)
            withJacoco(/* "0.8.7" */)
        }

        benchmark {
            // jmhVersion("1.34")
        }

        packaging {
            uberJar {
                // minimize(false)
            }
        }

        docker {
            withJvmKill(/* "1.16.0" */)
            // baseImage("bellsoft/liberica-openjdk-alpine:17")
            // withAppSh(false)
            // ports.add()
            // javaOpts("")
        }

        documentation.enable()
    }
}
