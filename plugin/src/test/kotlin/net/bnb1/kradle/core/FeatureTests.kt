package net.bnb1.kradle.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import org.gradle.api.Project

class FeatureTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val tracer = Tracer()
    val project = mockk<Project>(relaxed = true)

    Given("Feature with blueprints and listeners") {
        val feature = Feature("test").also { it.enable() }
        val blueprint1 = spyk(Blueprint1(project)).also { feature += it }
        val blueprint2 = spyk(Blueprint2(project)).also { feature += it }

        When("Activate") {
            feature.activate(tracer)

            Then("The assigned blueprints are also activated") {
                verify {
                    blueprint1.activate(tracer)
                    blueprint2.activate(tracer)
                }
            }
        }
    }

    Given("Feature") {
        val feature = Feature("test").also { it.enable() }

        When("Add blueprint twice") {
            val blueprint = spyk(Blueprint1(project)).also {
                feature += it
                feature += it
            }
            feature.activate(tracer)

            Then("The second attempt is ignored") {
                verify(exactly = 1) {
                    blueprint.activate(tracer)
                }
            }
        }

        When("Get default task name") {
            val taskName = feature.defaultTaskName

            Then("It should be NONE") {
                taskName shouldBe "NONE"
            }
        }
    }

    Given("Feature with blueprints") {
        val feature = Feature("test").also { it.enable() }
        val blueprint = spyk(Blueprint1(project)).also { feature += it }

        When("Feature is disabled") {
            feature.disable()

            Then("Activate does nothing") {
                feature.activate(tracer)
                verify(exactly = 0) {
                    blueprint.activate(tracer)
                }
            }
        }

        When("Activate twice") {
            feature.activate(tracer)
            feature.activate(tracer)

            Then("The second attempt is ignored") {
                verify(exactly = 1) {
                    blueprint.activate(tracer)
                }
            }
        }
    }

    Given("Active feature") {
        val feature = Feature("1").also {
            it.enable()
            it.activate(tracer)
        }

        When("Set required feature") {
            Then("Fail") {
                shouldThrow<IllegalStateException> { feature.requires(Feature("2")) }
            }
        }

        When("Set conflicting feature") {
            Then("Fail") {
                shouldThrow<IllegalStateException> { feature conflictsWith Feature("2") }
            }
        }

        When("Set activates after") {
            Then("Fail") {
                shouldThrow<IllegalStateException> { feature activatesAfter Feature("2") }
            }
        }

        When("Add blueprint") {
            Then("Fail") {
                shouldThrow<IllegalStateException> { feature.also { it += Blueprint1(project) } }
            }
        }
    }

    Given("Two features") {
        val feature1 = Feature("1")
        val feature2 = Feature("2")

        And("Feature 1 requires feature 2") {
            feature1 requires feature2

            When("Both are enabled") {
                feature1.enable()
                feature2.enable()

                Then("Activate feature 1 succeeds") {
                    feature1.activate(tracer)
                    feature1.isActive shouldBe true
                }
            }

            When("Feature 2 is disabled") {
                feature1.enable()
                feature2.disable()

                Then("Activate feature 1 fails") {
                    shouldThrow<GradleException> { feature1.activate(tracer) }
                }
            }

            When("Feature 1 is disabled") {
                feature1.disable()
                feature2.enable()

                Then("Activate feature 2 succeeds") {
                    feature2.activate(tracer)
                    feature2.isActive shouldBe true
                }
            }
        }

        And("Feature 1 conflicts with feature 2") {
            feature1 conflictsWith feature2

            When("Both are enabled") {
                feature1.enable()
                feature2.enable()

                Then("Activate feature 1 fails") {
                    shouldThrow<GradleException> { feature1.activate(tracer) }
                }

                Then("Activate feature 2 fails") {
                    shouldThrow<GradleException> { feature2.activate(tracer) }
                }
            }

            When("Feature 2 is disabled") {
                feature1.enable()
                feature2.disable()

                Then("Activate feature 1 succeeds") {
                    feature1.activate(tracer)
                    feature1.isActive shouldBe true
                }
            }

            When("Feature 1 is disabled") {
                feature1.disable()
                feature2.enable()

                Then("Activate feature 2 succeeds") {
                    feature2.activate(tracer)
                    feature2.isActive shouldBe true
                }
            }
        }
    }
})

class Blueprint1(project: Project) : Blueprint(project)
class Blueprint2(project: Project) : Blueprint(project)
