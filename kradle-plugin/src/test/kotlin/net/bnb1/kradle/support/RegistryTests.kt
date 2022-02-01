package net.bnb1.kradle.support

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.dsl.Properties

class RegistryTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("Registry") {
        val registry = Registry()

        When("Registering two instances for one class") {
            val properties1 = Properties()
            registry.add(properties1)

            val properties2 = Properties()
            Then("The second attempt should fail") {
                shouldThrow<IllegalArgumentException> { registry.add(properties2) }
            }
        }

        When("Registering two instances for one class with different name") {
            val properties1 = Properties()
            registry.add("properties1", properties1)
            val properties2 = Properties()
            registry.add("properties2", properties2)

            Then("Succeed") {
                registry.map.size shouldBe 2
            }
        }

        And("With two entries of different type") {
            val properties = Properties()
            registry.add(properties)
            registry.add(Feature("feature1"))

            When("Fetching entries per type") {
                val result = registry.withType<Properties>()

                Then("Only entries of type should be returned") {
                    result.shouldContainExactly(properties)
                }
            }
        }

        And("With one entry") {
            val properties = Properties()
            registry.add("properties", properties)

            When("Fetching entry with correct name") {
                val result = registry.named<Properties>("properties")

                Then("Entry should be returned") {
                    result shouldBe properties
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
