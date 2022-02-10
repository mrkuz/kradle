package net.bnb1.kradle.dsl

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FlagTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    var target = false

    Given("Flag") {

        val flag = Flag { target = it }

        When("Call enable") {
            flag.enable()

            Then("Target is enabled") {
                target shouldBe true
            }
        }

        When("Call set(true)") {
            flag.set(true)

            Then("Target is enabled") {
                target shouldBe true
            }
        }

        When("Call toggle") {
            flag.toggle()

            Then("Target is enabled") {
                target shouldBe true
            }
        }

        When("Check if not null") {
            val result = flag.notNull

            Then("Result is true") {
                result shouldBe true
            }
        }
    }

    Given("Enabled flag") {

        val flag = Flag { target = it }.also { it.enable() }

        When("Call get") {
            val result = flag.get()

            Then("Result is true") {
                result shouldBe true
            }
        }

        When("Call disable") {
            flag.disable()

            Then("Target is disabled") {
                target shouldBe false
            }
        }
    }
})
