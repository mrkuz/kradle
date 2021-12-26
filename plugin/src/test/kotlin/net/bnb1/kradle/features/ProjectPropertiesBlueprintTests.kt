package net.bnb1.kradle.features

import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import io.mockk.verify
import net.bnb1.kradle.features.general.ProjectPropertiesBlueprint
import net.bnb1.kradle.plugins.ProjectPropertiesPlugin
import org.gradle.api.Project

class ProjectPropertiesBlueprintTests : FunSpec({

    test("Project properties plugin is applied") {
        val project = mockk<Project>(relaxed = true)
        val blueprint = ProjectPropertiesBlueprint(project)

        blueprint.activate()

        verify {
            project.pluginManager.apply(ProjectPropertiesPlugin::class.java)
        }
    }
})
