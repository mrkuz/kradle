package net.bnb1.kradle.support

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.core.FeatureSet
import net.bnb1.kradle.core.Properties

class RegistryTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("Registry") {
        val registry = Registry()

        When("Registering two instances for one class") {
            val featureSet1 = FeatureSet("1")
            registry.add(featureSet1)

            val featureSet2 = FeatureSet("2")
            Then("The second attempt should fail") {
                shouldThrow<IllegalArgumentException> { registry.add(featureSet2) }
            }
        }

        When("Registering two instances for one class with different name") {
            val featureSet1 = FeatureSet("1")
            registry.add("featureSet1", featureSet1)
            val featureSet2 = FeatureSet("2")
            registry.add("featureSet2", featureSet2)

            Then("Succeed") {
                registry.map.size shouldBe 2
            }
        }

        And("With two entries of different type") {
            val featureSet = FeatureSet("1")
            registry.add(featureSet)
            registry.add(Feature("feature1"))

            When("Fetching entries per type") {
                val result = registry.withType<FeatureSet>()

                Then("Only entries of type should be returned") {
                    result.shouldContainExactly(featureSet)
                }
            }
        }

        And("With one entry") {
            val featureSet = FeatureSet("1")
            registry.add("featureSet", featureSet)

            When("Fetching entry with correct name") {
                val result = registry.named<FeatureSet>("featureSet")

                Then("Entry should be returned") {
                    result shouldBe featureSet
                }
            }

            When("Fetching non-existing entry") {
                Then("Fails") {
                    shouldThrow<NullPointerException> { registry.named<Properties>("fail") }
                }
            }
        }
    }
})
