package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.buildDirAsFile
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask

class DokkaBlueprint(project: Project) : Blueprint(project) {

    override fun doCreateTasks() {
        project.createTask<DokkaTask>("generateDocumentation", "Generates Dokka HTML documentation") {
            outputDirectory.set(project.buildDirAsFile.resolve("docs"))
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
