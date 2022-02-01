package net.bnb1.kradle.support

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

class TracerTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("Tracer") {
        val tracer = Tracer()

        When("Add message") {
            tracer.trace("Test")

            Then("Trace entry should be created") {
                tracer.entries.size shouldBe 1
                tracer.entries[0].level shouldBe 0
                tracer.entries[0].message shouldBe "Test"
            }
        }

        When("Create new branch") {
            tracer.branch {
                tracer.trace("Test")
            }

            Then("Level of entry should be increased") {
                tracer.entries.size shouldBe 1
                tracer.entries[0].level shouldBe 1
                tracer.entries[0].message shouldBe "Test"
            }
        }
    }

    Given("Deactivated tracer") {
        val tracer = Tracer()
        tracer.deactivate()

        When("Add message") {
            tracer.trace("Test")

            Then("No entry should be created") {
                tracer.entries.shouldBeEmpty()
            }
        }
    }
})
