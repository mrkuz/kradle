package net.bnb1.kradle.features

import io.kotest.core.spec.style.FunSpec
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.features.general.ProjectPropertiesBlueprint
import net.bnb1.kradle.plugins.ProjectPropertiesPlugin

class ProjectPropertiesBlueprintTests : FunSpec({

    test("Project properties plugin is applied") {
        val project = Mocks.project()
        val blueprint = ProjectPropertiesBlueprint(project)

        blueprint.activate()

        verify {
            project.pluginManager.apply(ProjectPropertiesPlugin::class.java)
        }
    }
})
