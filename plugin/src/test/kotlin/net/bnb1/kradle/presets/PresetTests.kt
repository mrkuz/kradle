package net.bnb1.kradle.presets

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.Mocks
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class PresetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("Preset") {
        val context = KradleContext()
        val project = Mocks.project()
        val extension = KradleExtensionBase(context, project)
        val lock = AtomicBoolean()
        val preset = TestPreset(lock)

        When("Activate twice") {
            preset.activate(extension)
            preset.activate(extension)

            Then("The second attempt is ignored") {
                preset.activated.get() shouldBe 1
            }
        }
    }

    Given("Two presets") {
        val context = KradleContext()
        val project = Mocks.project()
        val extension = KradleExtensionBase(context, project)
        val lock = AtomicBoolean()
        val preset1 = TestPreset(lock)
        val preset2 = TestPreset(lock)

        When("Activate both") {
            preset1.activate(extension)

            Then("The second attempt fails") {
                shouldThrow<GradleException> { preset2.activate(extension) }
            }
        }
    }
})

class TestPreset(lock: AtomicBoolean) : Preset(lock) {

    val activated = AtomicInteger(0)

    override fun onActivate(extension: KradleExtensionBase) {
        activated.incrementAndGet()
    }
}
