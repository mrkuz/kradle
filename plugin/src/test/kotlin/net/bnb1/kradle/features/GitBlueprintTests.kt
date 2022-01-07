package net.bnb1.kradle.features

import io.kotest.core.spec.style.FunSpec
import io.mockk.verify
import net.bnb1.kradle.Mocks
import net.bnb1.kradle.features.general.GitBlueprint
import net.bnb1.kradle.plugins.GitPlugin
import net.bnb1.kradle.support.Tracer

class GitBlueprintTests : FunSpec({

    test("Git plugin is applied") {
        val tracer = Tracer()
        val project = Mocks.project()
        val blueprint = GitBlueprint(project)

        blueprint.activate(tracer)

        verify {
            project.pluginManager.apply(GitPlugin::class.java)
        }
    }
})
