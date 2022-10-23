plugins {
    `java`
    id("net.bitsandbobs.kradle") version "2.4.1"
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
            withLombok("1.18.24")
            /*
            lint {
                checkstyle.enable {
                    version("10.3.4")
                    configFile("checkstyle.xml")
                }
            }
            */
            codeAnalysis {
                /*
                pmd.enable {
                    version("6.50.0")
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
                    // version("4.7.2")
                    useFbContrib(/* "7.4.7" */)
                    useFindSecBugs(/* "1.12.0" */)
                }
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
            jvmOpts("...")
            arguments("...")
            ports(...)
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
