package net.bnb1.kradle.dsl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class OptionalTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    var target: String? = null

    Given("Optional") {
        val optional = Optional<String>(null) { target = it }

        When("Check is not null") {
            val result = optional.notNull

            Then("Result is false") {
                result shouldBe false
            }
        }

        When("Call get") {

            Then("Fail") {
                shouldThrow<NullPointerException> { optional.get() }
            }
        }

        When("Call get with default value") {
            val result = optional.get("default")

            Then("Result is default value") {
                result shouldBe "default"
            }
        }

        When("Call set with value") {
            optional.set("new")

            Then("Target value is set") {
                target shouldBe "new"
            }
        }
    }

    Given("Optional with value set") {
        val optional = Optional<String>(null) { target = it }
        optional.set("value")

        When("Check is not null") {
            val result = optional.notNull

            Then("Result is true") {
                result shouldBe true
            }
        }

        When("Call get") {
            val result = optional.get()

            Then("Result is value") {
                result shouldBe "value"
            }
        }

        When("Call get with default value") {
            val result = optional.get("default")

            Then("Result is value") {
                result shouldBe "value"
            }
        }

        When("Check target value") {

            Then("Target value is set") {
                target shouldBe "value"
            }
        }

        When("Call unset") {
            optional.unset()

            Then("Target value is unset") {
                target shouldBe null
            }
        }

        When("Call set without value") {
            optional.set()

            Then("Target value is unset") {
                target shouldBe null
            }
        }
    }

    Given("Optional with suggestion") {
        val optional = Optional<String>("suggestion") { target = it }

        When("Call set with value") {
            optional.set("new")

            Then("Target value is set") {
                target shouldBe "new"
            }
        }

        When("Call set without value") {
            optional.set()

            Then("Target value is set to suggestion") {
                target shouldBe "suggestion"
            }
        }
    }
})
