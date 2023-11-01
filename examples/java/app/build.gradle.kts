plugins {
    java
    id("net.bitsandbobs.kradle") version "2.5.0"
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
            mainClass("com.example.demo.App")
        }

        // targetJvm("17")
        java {
            // previewFeatures(false)
            withLombok("1.18.30")
            /*
            lint {
                checkstyle.enable {
                    version("10.12.4")
                    configFile("checkstyle.xml")
                }
            }
            */
            codeAnalysis {
                /*
                pmd.enable {
                    version("6.55.0")
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
                */
                spotBugs.enable {
                    // version("4.8.0")
                    useFbContrib(/* "7.6.0" */)
                    useFindSecBugs(/* "1.12.0" */)
                }
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
            jvmOpts("...")
            arguments("...")
            ports(...)
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
