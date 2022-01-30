package net.bnb1.kradle.blueprints

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import io.mockk.verify
import net.bnb1.kradle.blueprints.jvm.LibraryBlueprint
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

class LibraryBlueprintTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("LibraryBlueprint") {
        val tracer = Tracer()
        val project = mockk<Project>(relaxed = true)
        val applicationFeature = Feature("application")
        val libraryFeature = Feature("library")

        applicationFeature conflictsWith libraryFeature
        libraryFeature conflictsWith applicationFeature

        val blueprint = LibraryBlueprint(project)

        When("Library feature is enabled") {
            libraryFeature.enable()
            blueprint.activate(tracer)

            Then("Java library plugin is applied") {
                verify {
                    project.pluginManager.apply(JavaLibraryPlugin::class.java)
                }
            }
        }

        When("Library and application features are enabled") {
            libraryFeature.enable()
            applicationFeature.enable()
            Then("Fail") {
                shouldThrow<GradleException> { libraryFeature.activate(tracer) }
            }
        }
    }
})
