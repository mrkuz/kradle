package net.bnb1.kradle.blueprints

import net.bnb1.kradle.TaskBlueprint
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask
import java.io.File

object DokkaHtmlBlueprint : TaskBlueprint<DokkaTask> {

    override fun configure(project: Project, task: DokkaTask) = task.apply {
        task.outputDirectory.set(project.buildDir.resolve("docs"))
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