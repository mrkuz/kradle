plugins {
    `java`
    id("net.bitsandbobs.kradle") version "2.3.1"
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
            withLombok("1.18.22")
            /*
            lint {
                checkstyle.enable {
                    version("9.3")
                    configFile("checkstyle.xml")
                }
            }
            */
            codeAnalysis {
                /*
                pmd.enable {
                    version("6.42.0")
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
                    // version("4.5.3")
                    useFbContrib(/* "7.4.7" */)
                    useFindSecBugs(/* "1.11.0" */)
                }
            }
        }

        dependencies {
            // useCaffeine("3.0.5")
            // useGuava("31.0.1-jre")
            // useLog4j("2.17.1")
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
                version("5.8.2")
            }
            */
            prettyPrint(true)
            // showStandardStreams(false)
            withIntegrationTests(true)
            withFunctionalTests(true)
            // withCustomTests("...")
            // useArchUnit("0.22.0")
            // useTestcontainers("1.16.3")
        }

        codeCoverage {
            /*
            kover.enable {
                excludes("...")
            }
            jacoco.configureOnly {
                version("0.8.7")
                excludes("...")
            }
            */
        }

        benchmark {
            /*
            jmh {
                version("1.34")
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
    }
}
