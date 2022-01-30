package net.bnb1.kradle.blueprints

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import net.bnb1.kradle.blueprints.jvm.AllOpenBlueprint
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

class AllOpenBlueprintTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("AllOpenBlueprint") {
        val tracer = Tracer()
        val project = mockk<Project>(relaxed = true)
        val feature = Feature("kotlin")
        val blueprint = AllOpenBlueprint(project).also {
            it dependsOn feature
        }

        When("Kotlin feature is disabled") {
            blueprint.activate(tracer)

            Then("All-open plugin is not applied") {
                verify {
                    project.pluginManager wasNot Called
                }
            }
        }

        When("Kotlin feature is enabled") {
            feature.enable()
            blueprint.activate(tracer)

            Then("All-open plugin is applied") {
                verify {
                    project.pluginManager.apply(AllOpenGradleSubplugin::class.java)
                }
            }
        }
    }
})
