package net.bnb1.kradle.dsl

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

class ValueSetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val target = mutableSetOf<String>()

    Given("Value set") {

        val valueSet = ValueSet(target)

        When("Call add") {
            valueSet.add("1")

            Then("Value is added to target") {
                target shouldBe setOf("1")
            }
        }

        When("Call set") {
            valueSet.set("1", "2")

            Then("Values are added to target") {
                target shouldBe setOf("1", "2")
            }
        }

        When("Check if not null") {
            val result = valueSet.notNull

            Then("Result is true") {
                result shouldBe true
            }
        }
    }

    Given("Value set with one entry") {

        val valueSet = ValueSet(target)
        valueSet.add("1")

        When("Call get") {
            val result = valueSet.get()

            Then("Result contains one entry") {
                result shouldBe setOf("1")
            }
        }

        When("Call remove") {
            valueSet.remove("1")

            Then("Value is removed from target") {
                target.shouldBeEmpty()
            }
        }

        When("Call add") {
            valueSet.add("2")

            Then("Value is added to target") {
                target shouldBe setOf("1", "2")
            }
        }

        When("Call set") {
            valueSet.set("2")

            Then("Target values are overwritten") {
                target shouldBe setOf("2")
            }
        }

        When("Call reset") {
            valueSet.reset()

            Then("Target is empty") {
                target.shouldBeEmpty()
            }
        }
    }
})
