package net.bnb1.kradle.features

import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import io.mockk.verify
import net.bnb1.kradle.features.general.GitBlueprint
import net.bnb1.kradle.plugins.GitPlugin
import org.gradle.api.Project

class GitBlueprintTests : FunSpec({

    test("Git plugin is applied") {
        val project = mockk<Project>(relaxed = true)
        val blueprint = GitBlueprint(project)

        blueprint.activate()

        verify {
            project.pluginManager.apply(GitPlugin::class.java)
        }
    }
})
