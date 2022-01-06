package net.bnb1.kradle

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.support.Registry

class RegistryTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("Registry") {
        val project = Mocks.project()
        val registry = Registry<Properties>()

        When("Registering two instances for one class") {
            val properties1 = Properties(project)
            registry.register(properties1)

            val properties2 = Properties(project)
            registry.register(properties2)

            Then("The second instance is ignored") {
                registry.get<Properties>() shouldBe properties1
            }
        }
    }
})
