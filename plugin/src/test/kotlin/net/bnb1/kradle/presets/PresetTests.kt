package net.bnb1.kradle.presets

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.features.AllFeatures
import net.bnb1.kradle.features.AllProperties
import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class PresetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val project = Mocks.project()
    val context = KradleContext()
    val tracer = Tracer()
    val properties = AllProperties(context)
    val features = AllFeatures(context)

    Given("Preset") {
        val extension = KradleExtensionBase(context, tracer, features, properties, project)
        val lock = AtomicBoolean()
        val preset = TestPreset(extension, lock)

        When("Activate twice") {
            preset.activate()
            preset.activate()

            Then("The second attempt is ignored") {
                preset.activated.get() shouldBe 1
            }
        }
    }

    Given("Two presets") {
        val extension = KradleExtensionBase(context, tracer, features, properties, project)
        val lock = AtomicBoolean()
        val preset1 = TestPreset(extension, lock)
        val preset2 = TestPreset(extension, lock)

        When("Activate both") {
            preset1.activate()

            Then("The second attempt fails") {
                shouldThrow<GradleException> { preset2.activate() }
            }
        }
    }
})

class TestPreset(extension: KradleExtensionBase, lock: AtomicBoolean) : Preset(extension, lock) {

    val activated = AtomicInteger(0)

    override fun doActivate(extension: KradleExtensionBase) {
        activated.incrementAndGet()
    }
}
