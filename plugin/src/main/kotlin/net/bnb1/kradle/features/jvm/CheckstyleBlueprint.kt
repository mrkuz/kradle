package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.sourceSets
import net.bnb1.kradle.tasks.GenerateCheckstyleConfigTask
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle

private const val CONFIGURATION_NAME = "kradleCheckstyle"

class CheckstyleBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        val properties = project.propertiesRegistry.get<JavaLintProperties>()
        val configFile = project.rootDir.resolve(properties.checkstyleConfigFile.get())

        project.createTask<GenerateCheckstyleConfigTask>("generateCheckstyleConfig", "Generates checkstyle.xml") {
            outputFile.set(configFile)
        }

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("com.puppycrawl.tools:checkstyle:${properties.checkstyleVersion.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        project.sourceSets.forEach { sourceSet ->
            val sourceFiles = sourceSet.allSource.files
                .filter { it.extension.toLowerCase() == "java" }
                .toSet()

            val taskName = "checkstyle" + sourceSet.name[0].toUpperCase() + sourceSet.name.substring(1)

            project.createHelperTask<Checkstyle>(taskName, "Runs checkstyle on '${sourceSet.name}'") {
                setSource(sourceFiles)
                checkstyleClasspath = project.configurations.getAt(CONFIGURATION_NAME)
                classpath = sourceSet.compileClasspath
                configDirectory.set(project.rootDir)
                setConfigFile(configFile)
                maxErrors = 0
                maxWarnings = 0
                reports {
                    html.required.set(true)
                    xml.required.set(false)
                }
                reports.forEach {
                    it.outputLocation.set(project.buildDir.resolve("reports/checkstyle/${sourceSet.name}.${it.name}"))
                }
                if (!configFile.exists()) {
                    dependsOn("generateCheckstyleConfig")
                }
            }
            project.tasks.getByName(LintFeature.MAIN_TASK).dependsOn(taskName)
        }
    }
}
