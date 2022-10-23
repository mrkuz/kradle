plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
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
            useCoroutines(/* "1.6.4" */)
            lint {
                ktlint.enable {
                    // version("0.43.2")
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            /*
            codeAnalysis {
                detekt.enable {
                    version("1.21.0")
                    configFile("detekt-config.yml")
                }
            }
            */
            test {
                useKotest(/* "5.2.3" */)
                useMockk(/* "1.13.2" */)
            }
        }

        dependencies {
            // useCaffeine("3.1.1")
            // useGuava("31.1-jre")
            // useLog4j("2.19.0")
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
                version("5.9.1")
            }
            */
            prettyPrint(true)
            // showStandardStreams(false)
            withIntegrationTests(true)
            withFunctionalTests(true)
            // withCustomTests("...")
            // useArchUnit("1.0.0")
            // useTestcontainers("1.17.5")
        }

        codeCoverage {
            /*
            kover.enable {
                excludes("...")
            }
            jacoco.configureOnly {
                version("0.8.8")
                excludes("...")
            }
            */
        }

        benchmark {
            /*
            jmh {
                version("1.35")
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

        /*
        frameworks {
            springBoot {
                version("2.7.4")
                withDevTools("2.7.4")
                useWeb("2.7.4")
                useWebFlux("2.7.4")
                useActuator("2.7.4")
            }
        }
        */
    }
}
