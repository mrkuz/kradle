package net.bnb1.kradle.blueprints

import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import io.mockk.verify
import net.bnb1.kradle.blueprints.general.GitBlueprint
import net.bnb1.kradle.support.Tracer
import net.bnb1.kradle.support.plugins.GitPlugin
import org.gradle.api.Project

class GitBlueprintTests : FunSpec({

    test("Git plugin is applied") {
        val tracer = Tracer()
        val project = mockk<Project>(relaxed = true)
        val blueprint = GitBlueprint(project)

        blueprint.activate(tracer)

        verify {
            project.pluginManager.apply(GitPlugin::class.java)
        }
    }
})
