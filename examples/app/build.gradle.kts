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
        java {
            /*
            withPreviewFeatures(false)
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
                    // version("4.5.2")
                    useFbContrib(/* 7.4.7 */)
                    useFindSecBugs(/* 1.11.0 */)
                }
            }
        }

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            integrationTests(true)
            functionalTests(true)
            // customTests("...")
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
            // baseImage("bellsoft/liberica-openjdk-alpine:17")
            withJvmKill(/* "1.16.0" */)
            // appSh(false)
            // ports(...)
            // javaOpts("")
        }

        documentation.enable()
    }
}
