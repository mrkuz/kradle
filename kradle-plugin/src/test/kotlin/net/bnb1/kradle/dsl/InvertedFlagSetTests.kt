package net.bnb1.kradle.dsl

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

class InvertedFlagSetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    var target = mutableSetOf<String>()

    Given("Inverted flags") {

        val flags = InvertedFlagSet(target)

        When("Call disable") {
            flags.disable("1")

            Then("Value is added to target") {
                target shouldBe setOf("1")
            }
        }

        When("Check if not null") {
            val result = flags.notNull

            Then("Result is true") {
                result shouldBe true
            }
        }
    }

    Given("Inverted flags with one disabled entry") {

        val flags = InvertedFlagSet(target)
        flags.disable("1")

        When("Call get") {
            val result = flags.get()

            Then("Result contains one entry") {
                result shouldBe setOf("1")
            }
        }

        When("Call enable") {
            flags.enable("1")

            Then("Value is removed from target") {
                target.shouldBeEmpty()
            }
        }

        When("Call reset") {
            flags.reset()

            Then("Target is empty") {
                target.shouldBeEmpty()
            }
        }
    }
})
