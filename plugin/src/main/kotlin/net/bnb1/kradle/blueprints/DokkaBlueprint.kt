package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import java.io.File

object DokkaBlueprint : PluginBlueprint<DokkaPlugin> {

    override fun configure(project: Project) {
        project.tasks.named<DokkaTask>("dokkaHtml").configure {
            outputDirectory.set(project.buildDir.resolve("docs"))
            dokkaSourceSets.forEach {
                fun includeIfExists(fileName: String): Boolean {
                    if (File("${project.projectDir}/${fileName}").exists()) {
                        it.includes.setFrom(fileName)
                        return true
                    }
                    return false
                }

                // These files are used for package/module documentation. First match wins.
                if (includeIfExists("src/main/kotlin/module.md")) return@forEach
                if (includeIfExists("src/main/kotlin/package.md")) return@forEach
                if (includeIfExists("module.md")) return@forEach
                if (includeIfExists("package.md")) return@forEach
            }
        }
    }
}