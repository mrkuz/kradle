package net.bnb1.kradle.features

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class BlueprintTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    given("Blueprint") {
        val tracer = Tracer()
        val project = Mocks.project()
        val blueprint = TestBlueprint(project)

        When("Activate twice") {
            blueprint.activate(tracer)
            blueprint.activate(tracer)

            Then("The second attempt is ignored") {
                blueprint.activated.get() shouldBe 1
            }
        }

        When("Condition for activation is not met") {
            blueprint.shouldActive.set(false)

            Then("Blueprint is not activated") {
                blueprint.activated.get() shouldBe 0
            }
        }
    }
})

class TestBlueprint(project: Project) : Blueprint(project) {

    val shouldActive = AtomicBoolean(true)
    val activated = AtomicInteger(0)

    override fun shouldActivate() = shouldActive.get()

    override fun configure() {
        activated.incrementAndGet()
    }
}
