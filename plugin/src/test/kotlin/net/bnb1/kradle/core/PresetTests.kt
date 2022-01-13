package net.bnb1.kradle.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.config.AllFeatureSets
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.config.dsl.KradleExtensionDsl
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
    val extension = KradleExtensionDsl(tracer, featureSets, features, properties)
    val lock = AtomicBoolean()

    Given("Preset") {
        val preset = TestPreset(lock)

        When("Activate twice") {
            preset.activate(extension)
            preset.activate(extension)

            Then("The second attempt is ignored") {
                preset.activations.get() shouldBe 1
            }
        }
    }

    Given("Two presets") {
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

    val activations = AtomicInteger(0)

    override fun doActivate(target: KradleExtensionDsl) {
        activations.incrementAndGet()
    }
}
