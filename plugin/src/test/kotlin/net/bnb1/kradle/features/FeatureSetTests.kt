package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.core.FeatureSet
import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException

class FeatureSetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val tracer = Tracer()

    given("FeatureSet") {
        val set = FeatureSet()
        val feature1 = spyk<Feature1>().also {
            it.enable()
            set += it
        }
        val feature2 = spyk<Feature2>().also {
            it.enable()
            set += it
        }

        When("Activate twice") {
            set.activate(tracer)

            Then("The second attempt fails") {
                shouldThrow<GradleException> { set.activate(tracer) }
            }
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
    }

    given("FeatureSet with ordered features") {
        val set = FeatureSet()
        val feature1 = spyk<Feature1>()
        val feature2 = spyk<Feature2>()
        val feature3 = spyk<Feature3>()

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

    given("FeatureSet with dependency loop") {
        val set = FeatureSet()
        val feature1 = spyk<Feature1>()
        val feature2 = spyk<Feature2>()
        val feature3 = spyk<Feature3>()

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

class Feature1 : Feature()
class Feature2 : Feature()
class Feature3 : Feature()
