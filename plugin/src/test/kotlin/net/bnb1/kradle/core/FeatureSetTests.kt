package net.bnb1.kradle.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException

class FeatureSetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val tracer = Tracer()

    Given("FeatureSet with two features") {
        val set = FeatureSet("test")
        val feature1 = spyk(Feature("1")).also {
            it.enable()
            set += it
        }
        val feature2 = spyk(Feature("2")).also {
            it.enable()
            set += it
        }

        When("Activate") {
            set.activate(tracer)

            Then("The assigned features are also activated") {
                verify {
                    feature1.activate(tracer)
                    feature2.activate(tracer)
                }
            }
        }

        When("Activate twice") {
            set.activate(tracer)

            Then("The second attempt fails") {
                shouldThrow<GradleException> { set.activate(tracer) }
            }
        }
    }

    Given("FeatureSet with ordered features") {
        val set = FeatureSet("test")
        val feature1 = spyk(Feature("1"))
        val feature2 = spyk(Feature("2"))
        val feature3 = spyk(Feature("3"))

        feature1.also {
            it.enable()
            set += it
            it activatesAfter feature3
        }
        feature2.also {
            it.enable()
            set += it
        }
        feature3.also {
            it.enable()
            set += it
            it activatesAfter feature2
        }

        When("Activate") {
            set.activate(tracer)

            Then("Features are activated in the defined order") {
                verifyOrder {
                    feature2.activate(tracer)
                    feature3.activate(tracer)
                    feature1.activate(tracer)
                }
            }
        }
    }

    Given("FeatureSet with dependency loop") {
        val set = FeatureSet("test")
        val feature1 = spyk(Feature("1"))
        val feature2 = spyk(Feature("2"))
        val feature3 = spyk(Feature("3"))

        feature1.also {
            it.enable()
            set += it
            it activatesAfter feature2
        }
        feature2.also {
            it.enable()
            set += it
            it activatesAfter feature3
        }
        feature3.also {
            it.enable()
            set += it
            it activatesAfter feature1
        }

        When("Activate") {
            Then("Dependency loop is detected") {
                shouldThrow<IllegalStateException> { set.activate(tracer) }
            }
        }
    }
})
