package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.featureRegistry
import org.gradle.api.GradleException

class FeatureSetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val project = Mocks.project()

    fun prepareFeature(feature: Feature) {
        feature.setParent(FeatureSet::class)
        feature.enable()
        project.featureRegistry.register(feature)
    }

    given("FeatureSet") {
        val set = FeatureSet(project)
        val feature1 = spyk<Feature1>().also { prepareFeature(it) }
        val feature2 = spyk<Feature2>().also { prepareFeature(it) }

        When("Activate twice") {
            set.activate()

            Then("The second attempt fails") {
                shouldThrow<GradleException> { set.activate() }
            }
        }

        When("Activate") {
            set.activate()

            Then("The assigned features are also activated") {
                verify {
                    feature1.activate()
                    feature2.activate()
                }
            }
        }
    }

    given("FeatureSet with ordered features") {
        val set = FeatureSet(project)
        val feature1 = spyk<Feature1>().also {
            prepareFeature(it)
            it.after(Feature3::class)
        }
        val feature2 = spyk<Feature2>().also { prepareFeature(it) }
        val feature3 = spyk<Feature3>().also {
            prepareFeature(it)
            it.after(Feature2::class)
        }

        When("Activate") {
            set.activate()

            Then("Features are activated in the defined order") {
                verifyOrder {
                    feature2.activate()
                    feature3.activate()
                    feature1.activate()
                }
            }
        }
    }

    given("FeatureSet with dependency loop") {
        val set = FeatureSet(project)
        spyk<Feature1>().also {
            prepareFeature(it)
            it.after(Feature2::class)
        }
        spyk<Feature2>().also {
            prepareFeature(it)
            it.after(Feature3::class)
        }
        spyk<Feature3>().also {
            prepareFeature(it)
            it.after(Feature1::class)
        }

        When("Activate") {
            Then("Dependency loop is detected") {
                shouldThrow<IllegalStateException> { set.activate() }
            }
        }
    }
})

class Feature1 : Feature()
class Feature2 : Feature()
class Feature3 : Feature()
