package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.features.jvm.ApplicationFeature
import net.bnb1.kradle.features.jvm.LibraryBlueprint
import net.bnb1.kradle.features.jvm.LibraryFeature
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaLibraryPlugin

class LibraryBlueprintTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("LibraryBlueprint") {
        val project = Mocks.project()
        val applicationFeature = ApplicationFeature()
        val libraryFeature = LibraryFeature()

        applicationFeature.conflictsWith = libraryFeature
        libraryFeature.conflictsWith = applicationFeature

        val blueprint = LibraryBlueprint(project)

        When("Library feature is enabled") {
            libraryFeature.enable()
            blueprint.activate()

            Then("Java library plugin is applied") {
                verify {
                    project.pluginManager.apply(JavaLibraryPlugin::class.java)
                }
            }
        }

        When("Library and application features are enabled") {
            applicationFeature.enable()
            Then("Fail") {
                shouldThrow<GradleException> { libraryFeature.enable() }
            }
        }
    }
})
