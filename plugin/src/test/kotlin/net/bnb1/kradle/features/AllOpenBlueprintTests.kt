package net.bnb1.kradle.features

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Called
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.jvm.AllOpenBlueprint
import net.bnb1.kradle.features.jvm.KotlinFeature
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

class AllOpenBlueprintTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("AllOpenBlueprint") {
        val project = Mocks.project()
        val feature = KotlinFeature().also { project.featureRegistry.register(it) }
        val blueprint = AllOpenBlueprint(project)

        When("Kotlin feature is disabled") {
            blueprint.activate()

            Then("All-open plugin is not applied") {
                verify {
                    project.pluginManager wasNot Called
                }
            }
        }

        When("Kotlin feature is enabled") {
            feature.enable()
            blueprint.activate()

            Then("All-open plugin is applied") {
                verify {
                    project.pluginManager.apply(AllOpenGradleSubplugin::class.java)
                }
            }
        }
    }
})
