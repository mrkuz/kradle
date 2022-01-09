package net.bnb1.kradle.features

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project

class FeatureTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val tracer = Tracer()
    val project = Mocks.project()

    given("Feature with blueprints and listeners") {
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

    given("Feature") {
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

        When("Disable is called") {
            feature.disable()
            feature.enable()

            Then("Enable is no longer possible") {
                feature.isEnabled shouldBe false
            }
        }
    }
})

class Blueprint1(project: Project) : Blueprint(project)
class Blueprint2(project: Project) : Blueprint(project)
