package net.bnb1.kradle

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.dsl.Properties

class KradleContextTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("KradleContext") {
        val context = KradleContext()

        When("Registering two instances for one class") {
            val properties1 = Properties()
            context.register(properties1)

            val properties2 = Properties()
            context.register(properties2)

            Then("The second instance is ignored") {
                context.get<Properties>() shouldBe properties1
            }
        }
    }
})
