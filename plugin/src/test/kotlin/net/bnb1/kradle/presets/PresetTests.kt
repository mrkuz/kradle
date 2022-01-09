package net.bnb1.kradle.presets

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.config.AllFeatureSets
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.core.Preset
import net.bnb1.kradle.support.Registry
import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class PresetTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val context = Registry()
    val tracer = Tracer()
    val properties = AllProperties(context)
    val features = AllFeatures(context)
    val featureSets = AllFeatureSets(context)

    Given("Preset") {
        val extension = KradleExtensionDsl(tracer, featureSets, features, properties)
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
        val extension = KradleExtensionDsl(tracer, featureSets, features, properties)
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

class TestPreset(lock: AtomicBoolean) : Preset<KradleExtensionDsl>(lock) {

    val activated = AtomicInteger(0)

    override fun doActivate(extension: KradleExtensionDsl) {
        activated.incrementAndGet()
    }
}
