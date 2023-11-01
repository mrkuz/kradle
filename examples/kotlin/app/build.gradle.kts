plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {

    // Default values and unused features are commented out, but kept for reference

    general {
        bootstrap.enable()
        git.enable()
        /*
        buildProfiles {
            active("default")
        }
        */
        projectProperties.enable()
        buildProperties.enable()
        scripts {
            "dockerRun" {
                description("Runs Docker image")
                dependsOn("buildImage")
                commands("docker run --rm ${project.name}")
            }
        }
        /*
        helm {
            releaseName(project.name)
            valuesFile("...")
        }
        */
    }

    jvm {
        application {
            mainClass("com.example.demo.AppKt")
        }

        // targetJvm("17")
        kotlin {
            useCoroutines(/* "1.7.3" */)
            lint {
                ktlint.enable {
                    // version("1.0.1")
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            /*
            codeAnalysis {
                detekt.enable {
                    version("1.23.3")
                    configFile("detekt-config.yml")
                }
            }
            */
            test {
                useKotest(/* "5.7.2" */)
                useMockk(/* "1.13.8" */)
            }
        }

        dependencies {
            // useCaffeine("3.1.8")
            // useGuava("32.1.3-jre")
        }

        vulnerabilityScan.enable()
        lint {
            // ignoreFailures(false)
        }
        codeAnalysis {
            // ignoreFailures(false)
        }
        developmentMode.enable()

        test {
            /*
            junitJupiter.enable {
                version("5.10.0")
            }
            */
            prettyPrint(true)
            // showStandardStreams(false)
            withIntegrationTests(true)
            withFunctionalTests(true)
            // withCustomTests("...")
            // useArchUnit("1.1.0")
            // useTestcontainers("1.19.1")
        }

        codeCoverage {
            /*
            kover.enable {
                excludes("...")
            }
            jacoco.configureOnly {
                version("0.8.11")
                excludes("...")
            }
            */
        }

        benchmark {
            /*
            jmh {
                version("1.37")
            }
            */
        }

        packaging {
            /*
            uberJar {
                minimize(false)
            }
            */
        }

        docker {
            /*
            baseImage("bellsoft/liberica-openjdk-alpine:17")
            imageName(project.name)
            allowInsecureRegistries(false)
            ports(...)
            jvmOpts("...")
            arguments("...")
            */
            withJvmKill(/* "1.16.0" */)
            // withStartupScript(false)
        }

        documentation.enable()

        logging {
            // withSlf4j("2.0.9")
            // withLog4j("2.21.1")
        }

        /*
        frameworks {
            springBoot {
                version("3.1.5")
                withDevTools("3.1.5")
                useWeb("3.1.5")
                useWebFlux("3.1.5")
                useActuator("3.1.5")
            }
        }
        */
    }
}
