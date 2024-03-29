package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.buildDirAsFile
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.support.tasks.GenerateCheckstyleConfigTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Checkstyle

private const val CONFIGURATION_NAME = "kradleCheckstyle"

class CheckstyleBlueprint(project: Project) : Blueprint(project) {

    lateinit var checkstyleProperties: CheckstyleProperties
    lateinit var lintProperties: LintProperties
    lateinit var extendsTask: String
    lateinit var extendsBootstrapTask: String

    override fun doCreateTasks() {
        val configFile = project.projectDir.resolve(checkstyleProperties.configFile)

        val generateTask = project.createHelperTask<GenerateCheckstyleConfigTask>(
            "generateCheckstyleConfig",
            "Generates checkstyle.xml"
        ) {
            outputFile.set(configFile)
        }
        project.tasks.findByPath(extendsBootstrapTask)?.dependsOn(generateTask)

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create(
                    "${Catalog.Dependencies.Tools.checkstyle}:${checkstyleProperties.version}"
                )
            }
            dependencies.addLater(dependencyProvider)
        }

        val checkstyleTask = project.createHelperTask<Task>("checkstyle", "Runs checkstyle")
        project.tasks.getByName(extendsTask).dependsOn(checkstyleTask)

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        javaExtension.sourceSets.forEach { sourceSet ->
            val taskName = "checkstyle" + sourceSet.name[0].uppercaseChar() + sourceSet.name.substring(1)

            project.createHelperTask<Checkstyle>(taskName, "Runs checkstyle on '${sourceSet.name}'") {
                setSource(sourceSet.java.files)
                checkstyleClasspath = project.configurations.getAt(CONFIGURATION_NAME)
                classpath = project.objects.fileCollection().from(sourceSet.java.classesDirectory)
                configDirectory.set(project.projectDir.resolve("config"))
                setConfigFile(configFile)
                maxErrors = 0
                maxWarnings = 0
                reports {
                    html.required.set(true)
                    xml.required.set(false)
                }
                reports.forEach {
                    it.outputLocation.set(
                        project.buildDirAsFile.resolve("reports/checkstyle/${sourceSet.name}.${it.name}")
                    )
                }
                if (!configFile.exists()) {
                    dependsOn("generateCheckstyleConfig")
                }
                ignoreFailures = lintProperties.ignoreFailures
            }
            checkstyleTask.dependsOn(taskName)
        }
    }
}
