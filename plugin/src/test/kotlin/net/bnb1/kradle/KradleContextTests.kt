package net.bnb1.kradle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.dsl.Properties
import net.bnb1.kradle.support.Registry

class KradleContextTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("KradleContext") {
        val context = Registry()

        When("Registering two instances for one class") {
            val properties1 = Properties()
            context.add(properties1)

            val properties2 = Properties()
            Then("The second attempt should fail") {
                shouldThrow<IllegalArgumentException> { context.add(properties2) }
            }
        }
    }
})
