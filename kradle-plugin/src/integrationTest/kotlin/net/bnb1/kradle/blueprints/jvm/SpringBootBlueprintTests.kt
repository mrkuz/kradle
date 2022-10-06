package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject

class SpringBootBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                developmentMode.enable()
                frameworks {
                    springBoot.enable()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("spring-boot-starter is available") {
                project.shouldHaveDependency("implementation", "org.springframework.boot:spring-boot-starter")

                // And: spring-boot-starter-test is available
                project.shouldHaveDependency("testImplementation", "org.springframework.boot:spring-boot-starter-test")

                // And: spring-boot-starter-web is not available
                project.shouldNotHaveDependency("implementation", "org.springframework.boot:spring-boot-starter-web")

                // And: spring-boot-starter-webflux is not available
                project.shouldNotHaveDependency(
                    "implementation",
                    "org.springframework.boot:spring-boot-starter-webflux"
                )

                // And: spring-boot-starter-actuator is not available
                project.shouldNotHaveDependency(
                    "implementation",
                    "org.springframework.boot:spring-boot-starter-actuator"
                )

                // And: spring-boot-devtools is not available
                project.shouldNotHaveDependency("implementation", "org.springframework.boot:spring-boot-devtool")
                project.shouldNotHaveDependency("kradleDev", "org.springframework.boot:spring-boot-devtool")
            }
        }
    }

    Given("springBoot.version = 2.6.0") {
        project.setUp {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                developmentMode.enable()
                frameworks {
                    springBoot {
                        version("2.6.0")
                    }
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("spring-boot-starter is available") {
                project.shouldHaveDependency("implementation", "org.springframework.boot:spring-boot-starter:2.6.0")

                // And: spring-boot-starter-test is available
                project.shouldHaveDependency(
                    "testImplementation",
                    "org.springframework.boot:spring-boot-starter-test:2.6.0"
                )
            }
        }
    }

    Given("springBoot.useWeb()") {
        project.setUp {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                developmentMode.enable()
                frameworks {
                    springBoot {
                        useWeb()
                    }
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("spring-boot-starter-web is available") {
                project.shouldHaveDependency("implementation", "org.springframework.boot:spring-boot-starter-web")
            }
        }
    }

    Given("springBoot.useWeb(2.6.0)") {
        project.setUp {
            """
            jvm {
                java.enable()
                application {
                   mainClass("com.example.demo.App")
                }
                developmentMode.enable()
                frameworks {
                    springBoot {
                        version("2.7.0")
                        useWeb("2.6.0")
                    }
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("spring-boot-starter is available") {
                project.shouldHaveDependency("implementation", "org.springframework.boot:spring-boot-starter:2.7.0")

                // And: spring-boot-starter-test is available
                project.shouldHaveDependency(
                    "testImplementation",
                    "org.springframework.boot:spring-boot-starter-test:2.7.0"
                )

                // And: spring-boot-starter-web is not available
                project.shouldHaveDependency(
                    "implementation",
                    "org.springframework.boot:spring-boot-starter-web:2.6.0"
                )
            }
        }
    }

    Given("springBoot.useWebFlux()") {
        project.setUp {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                developmentMode.enable()
                frameworks {
                    springBoot {
                        useWebFlux()
                    }
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("spring-boot-starter-webflux is available") {
                project.shouldHaveDependency(
                    "implementation",
                    "org.springframework.boot:spring-boot-starter-webflux"
                )
            }
        }
    }

    Given("springBoot.useActuator()") {
        project.setUp {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                developmentMode.enable()
                frameworks {
                    springBoot {
                        useActuator()
                    }
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("spring-boot-starter-actuator is available") {
                project.shouldHaveDependency(
                    "implementation",
                    "org.springframework.boot:spring-boot-starter-actuator"
                )
            }
        }
    }

    Given("springBoot.withDevTools()") {
        project.setUp {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                developmentMode.enable()
                frameworks {
                    springBoot {
                        withDevTools()
                    }
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("spring-boot-starter-devtools is available in kradleDev") {
                project.shouldHaveDependency("kradleDev", "org.springframework.boot:spring-boot-devtool")

                // And: spring-boot-starter-web is not available in implementation
                project.shouldNotHaveDependency("implementation", "org.springframework.boot:spring-boot-devtool")
            }
        }
    }
})