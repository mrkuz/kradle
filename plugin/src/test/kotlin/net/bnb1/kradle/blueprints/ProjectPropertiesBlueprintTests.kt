package net.bnb1.kradle.blueprints

import io.kotest.core.spec.style.FunSpec
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.blueprints.general.ProjectPropertiesBlueprint
import net.bnb1.kradle.support.Tracer
import net.bnb1.kradle.support.plugins.ProjectPropertiesPlugin

class ProjectPropertiesBlueprintTests : FunSpec({

    test("Project properties plugin is applied") {
        val tracer = Tracer()
        val project = Mocks.project()
        val blueprint = ProjectPropertiesBlueprint(project)

        blueprint.activate(tracer)

        verify {
            project.pluginManager.apply(ProjectPropertiesPlugin::class.java)
        }
    }
})
