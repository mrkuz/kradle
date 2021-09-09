package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask

object DokkaBlueprint : PluginBlueprint<DokkaPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.tasks.named<DokkaTask>("dokkaHtml").configure {
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

        project.alias("generateDocumentation", "Generates Dokka HTML documentation", "dokkaHtml")
    }
}