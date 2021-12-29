package net.bnb1.kradle.presets

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.presetRegistry
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicInteger

class PresetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("Preset") {
        val project = Mocks.project()
        val extension = KradleExtensionBase(project)
        val preset = TestPreset(project).also { project.presetRegistry.register(it) }

        When("Activate twice") {
            preset.activate(extension)
            preset.activate(extension)

            Then("The second attempt is ignored") {
                preset.activated.get() shouldBe 1
            }
        }
    }

    Given("Two presets") {
        val project = Mocks.project()
        val extension = KradleExtensionBase(project)
        val preset1 = TestPreset(project).also { project.presetRegistry.register(it) }
        val preset2 = TestPreset(project).also { project.presetRegistry.register(it) }

        When("Activate both") {
            preset1.activate(extension)

            Then("The second attempt fails") {
                shouldThrow<GradleException> { preset2.activate(extension) }
            }
        }
    }
})

class TestPreset(project: Project) : Preset(project) {

    val activated = AtomicInteger(0)

    override fun onActivate(extension: KradleExtensionBase) {
        activated.incrementAndGet()
    }
}
