package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.create
import net.bnb1.kradle.plugins.NoOpPlugin
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask

object DokkaBlueprint : PluginBlueprint<NoOpPlugin> {

    override fun configureEager(project: Project) {
        project.create<DokkaTask>("generateDocumentation", "Generates Dokka HTML documentation") {
            outputDirectory.set(project.buildDir.resolve("docs"))
            dokkaSourceSets
                .filter { it.name == "main" }
                .forEach {
                    project.fileTree(project.projectDir)
                        .matching {
                            include("module.md")
                            include("package.md")
                            include("src/main/**/module.md")
                            include("src/main/**/package.md")
                        }
                        .forEach { file -> it.includes.from(file) }
                }
        }
    }
}
