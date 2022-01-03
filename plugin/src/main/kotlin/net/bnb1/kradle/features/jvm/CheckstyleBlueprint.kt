package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.tasks.GenerateCheckstyleConfigTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
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
                project.dependencies.create("${Catalog.Dependencies.Tools.checkstyle}:${properties.checkstyleVersion.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val checkstyleTask = project.createHelperTask<Task>("checkstyle", "Runs checkstyle")
        project.tasks.getByName(LintFeature.MAIN_TASK).dependsOn(checkstyleTask)

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        javaExtension.sourceSets.forEach { sourceSet ->
            val taskName = "checkstyle" + sourceSet.name[0].toUpperCase() + sourceSet.name.substring(1)

            project.createHelperTask<Checkstyle>(taskName, "Runs checkstyle on '${sourceSet.name}'") {
                setSource(sourceSet.java.files)
                checkstyleClasspath = project.configurations.getAt(CONFIGURATION_NAME)
                classpath = project.objects.fileCollection().from(sourceSet.java.classesDirectory)
                configDirectory.set(project.rootDir.resolve("config"))
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
            checkstyleTask.dependsOn(taskName)
        }
    }
}