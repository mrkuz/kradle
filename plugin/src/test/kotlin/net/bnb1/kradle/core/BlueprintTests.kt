package net.bnb1.kradle.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class BlueprintTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val tracer = Tracer()
    val project = mockk<Project>(relaxed = true)
    val blueprint = TestBlueprint(project)

    Given("Blueprint") {

        When("Activate twice") {
            blueprint.activate(tracer)
            blueprint.activate(tracer)

            Then("The second attempt is ignored") {
                blueprint.activated.get() shouldBe 1
            }
        }

        When("Condition for activation is not met") {
            blueprint.shouldActive.set(false)

            blueprint.activate(tracer)
            Then("Blueprint is not activated") {
                blueprint.activated.get() shouldBe 0
            }
        }
    }

    Given("Blueprint with feature dependency") {
        val feature = Feature("1")
        blueprint.dependsOn(feature)

        When("Feature is enabled") {
            feature.enable()

            Then("Blueprint is activated") {
                blueprint.activate(tracer)
                blueprint.activated.get() shouldBe 1
            }
        }

        When("Feature is not enabled") {
            
            Then("Blueprint is not activated") {
                blueprint.activate(tracer)
                blueprint.activated.get() shouldBe 0
            }
        }
    }

    given("Activated blueprint") {
        blueprint.activate(tracer)

        Then("Setting dependency feature fails") {
            val feature = Feature("1")
            shouldThrow<IllegalStateException> { blueprint dependsOn feature }
        }
    }
})

class TestBlueprint(project: Project) : Blueprint(project) {

    val shouldActive = AtomicBoolean(true)
    val activated = AtomicInteger(0)

    override fun shouldActivate() = shouldActive.get()

    override fun doConfigure() {
        activated.incrementAndGet()
    }
}
