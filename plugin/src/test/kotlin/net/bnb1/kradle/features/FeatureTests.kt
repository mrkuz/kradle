package net.bnb1.kradle.features

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import net.bnb1.kradle.Mocks
import org.gradle.api.Project

class FeatureTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val project = Mocks.project()

    given("Feature with blueprints and listeners") {
        val feature = Feature().also { it.enable() }
        val blueprint1 = spyk(Blueprint1(project)).also { feature.addBlueprint(it) }
        val blueprint2 = spyk(Blueprint2(project)).also { feature.addBlueprint(it) }
        val listener1 = mockk<FeatureListener>(relaxed = true).also { feature.addListener(it) }
        val listener2 = mockk<FeatureListener>(relaxed = true).also { feature.addListener(it) }

        When("Activate") {
            feature.activate()

            Then("The assigned blueprints are also activated") {
                verify {
                    blueprint1.activate()
                    blueprint2.activate()
                }
            }

            Then("The assigned listeners are notified") {
                verify {
                    listener1.onFeatureActivate(Feature::class)
                    listener2.onFeatureActivate(Feature::class)
                }
            }
        }

        When("Activate twice") {
            feature.activate()
            feature.activate()

            Then("The second attempt is ignored") {
                verify(exactly = 1) {
                    listener1.onFeatureActivate(Feature::class)
                }
            }
        }

        When("Activate disabled feature") {
            feature.disable()
            feature.activate()

            Then("Feature is not activated") {
                verify {
                    listener1 wasNot Called
                }
            }
        }
    }

    given("Feature") {
        val feature = Feature().also { it.enable() }

        When("Add blueprint twice") {
            val blueprint = spyk(Blueprint1(project)).also {
                feature.addBlueprint(it)
                feature.addBlueprint(it)
            }
            feature.activate()

            Then("The second attempt is ignored") {
                verify(exactly = 1) {
                    blueprint.activate()
                }
            }
        }

        When("Add listener twice") {
            val listener = mockk<FeatureListener>(relaxed = true).also {
                feature.addListener(it)
                feature.addListener(it)
            }
            feature.activate()

            Then("The second attempt is ignored") {
                verify(exactly = 1) {
                    listener.onFeatureActivate(Feature::class)
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
