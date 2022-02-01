plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {

    // Defaults are commented out, but kept for reference

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
                ktlint {
                    // version("0.43.2")
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            /*
            codeAnalysis {
                detekt {
                    version("1.19.0")
                    configFile("detekt-config.yml")
                }
            }
            */
            test {
                useKotest(/* "5.0.3" */)
                useMockk(/* "1.12.2" */)
            }
        }

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint {
            // ignoreFailures(false)
        }
        codeAnalysis {
            // ignoreFailures(false)
        }
        developmentMode.enable()

        test {
            prettyPrint(true)
            // standardStreams(false)
            integrationTests(true)
            functionalTests(true)
            // customTests("...")
            junitJupiter {
                // version("5.8.2")
            }
            jacoco {
                // version("0.8.7")
            }
            // useArchUnit("0.22.0")
            // useTestcontainers("1.16.3")
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
            // baseImage("bellsoft/liberica-openjdk-alpine:17")
            withJvmKill(/* "1.16.0" */)
            // startupScript(false)
            // ports(...)
            // javaOpts("")
        }

        documentation.enable()
    }
}
