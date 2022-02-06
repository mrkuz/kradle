package net.bnb1.kradle.blueprints

import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import io.mockk.verify
import net.bnb1.kradle.blueprints.general.ProjectPropertiesBlueprint
import net.bnb1.kradle.support.Tracer
import net.bnb1.kradle.support.plugins.ProjectPropertiesPlugin
import org.gradle.api.Project

class ProjectPropertiesBlueprintTests : FunSpec({

    test("Project properties plugin is applied") {
        val tracer = Tracer()
        val project = mockk<Project>(relaxed = true)
        val blueprint = ProjectPropertiesBlueprint(project)

        blueprint.activate(tracer)

        verify {
            project.pluginManager.apply(ProjectPropertiesPlugin::class.java)
        }
    }
})
