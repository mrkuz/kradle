package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.create
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask

class DokkaBlueprint(project: Project) : Blueprint(project) {
    
    override fun createTasks() {
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
