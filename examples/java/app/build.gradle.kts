plugins {
    `java`
    id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {

    // Default values and unused features are commented out, but kept for reference

    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        application {
            mainClass("com.example.demo.App")
        }

        // targetJvm("17")
        java {
            /*
            previewFeatures(false)
            lint {
                checkstyle {
                    version("9.2.1")
                    configFile("checkstyle.xml")
                }
            }
            */
            codeAnalysis {
                /*
                pmd {
                    version("6.41.0")
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
                spotBugs {
                    // version("4.5.3")
                    useFbContrib(/* "7.4.7" */)
                    useFindSecBugs(/* "1.11.0" */)
                }
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
            // useArchUnit("0.22.0")
            // useTestcontainers("1.16.3")
        }

        codeCoverage {
            kover {
                // excludes("...")
            }
            /*
            jacoco {
                version("0.8.7")
                excludes("...")
            }
            */
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
            // jvmOpts("...")
        }

        documentation.enable()
    }
}
