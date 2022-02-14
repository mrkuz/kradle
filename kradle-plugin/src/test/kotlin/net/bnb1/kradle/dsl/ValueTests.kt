package net.bnb1.kradle.dsl

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ValueTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    var target = ""

    Given("Value") {
        val value = Value("default") { target = it }

        When("Check if not null") {
            val result = value.notNull

            Then("Result is true") {
                result shouldBe true
            }
        }

        When("Check target") {

            Then("Target is set to default value") {
                target shouldBe "default"
            }
        }

        When("Call get") {
            val result = value.get()

            Then("Result is default value") {
                result shouldBe "default"
            }
        }

        When("Call set") {
            value.set("new")

            Then("Target is new value") {
                target shouldBe "new"
            }
        }
    }
})
