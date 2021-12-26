package net.bnb1.kradle.features

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.jvm.ApplicationFeature
import net.bnb1.kradle.features.jvm.ApplicationProperties
import net.bnb1.kradle.features.jvm.KotlinBootstrapBlueprint
import net.bnb1.kradle.features.jvm.LibraryFeature
import net.bnb1.kradle.plugins.BootstrapAppPlugin
import net.bnb1.kradle.plugins.BootstrapLibPlugin
import net.bnb1.kradle.propertiesRegistry

class KotlinBootstrapBlueprintTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("KotlinBootstrapBlueprint") {
        val project = Mocks.project()
        val applicationFeature = ApplicationFeature().also { project.featureRegistry.register(it) }
        val libraryFeature = LibraryFeature().also { project.featureRegistry.register(it) }
        val blueprint = KotlinBootstrapBlueprint(project)

        When("Application feature is enabled") {
            ApplicationProperties(project).also {
                it.mainClass.set("com.example.demo.AppKt")
                project.propertiesRegistry.register(it)
            }
            applicationFeature.enable()
            blueprint.activate()

            Then("Bootstrap app plugin is applied") {
                verify {
                    project.pluginManager.apply(BootstrapAppPlugin::class.java)
                }
            }
        }

        When("Library feature is enabled") {
            libraryFeature.enable()
            blueprint.activate()

            Then("Bootstrap lib plugin is applied") {
                verify {
                    project.pluginManager.apply(BootstrapLibPlugin::class.java)
                }
            }
        }
    }
})
