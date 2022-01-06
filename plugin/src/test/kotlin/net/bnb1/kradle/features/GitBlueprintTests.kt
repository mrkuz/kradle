package net.bnb1.kradle.features

import io.kotest.core.spec.style.FunSpec
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.features.general.GitBlueprint
import net.bnb1.kradle.plugins.GitPlugin

class GitBlueprintTests : FunSpec({

    test("Git plugin is applied") {
        val project = Mocks.project()
        val blueprint = GitBlueprint(project)

        blueprint.activate()

        verify {
            project.pluginManager.apply(GitPlugin::class.java)
        }
    }
})
