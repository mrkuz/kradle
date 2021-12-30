package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.create
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.tasks.GenerateCheckstyleConfigTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Checkstyle

private const val CONFIGURATION_NAME = "kradleCheckstyle"
private const val TASK_NAME = "checkstyle"

class CheckstyleBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        val properties = project.propertiesRegistry.get<JavaLintProperties>()
        val configFile = project.rootDir.resolve(properties.checkstyleConfigFile.get())

        project.create<GenerateCheckstyleConfigTask>("generateCheckstyleConfig", "Generates checkstyle.xml") {
            outputFile.set(configFile)
        }

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("com.puppycrawl.tools:checkstyle:${properties.checkstyleVersion.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        val sourceFiles = javaExtension.sourceSets
            .asSequence()
            .flatMap { it.java.files }
            .filter { it.extension.toLowerCase() == "java" }
            .toSet()

        project.create<Checkstyle>(TASK_NAME, "Run checkstyle") {
            setSource(sourceFiles)
            checkstyleClasspath = project.configurations.getAt(CONFIGURATION_NAME)
            classpath = project.objects.fileCollection()
            configDirectory.set(project.rootDir)
            setConfigFile(configFile)
            maxErrors = 0
            maxWarnings = 0
            reports {
                html.required.set(true)
                xml.required.set(false)
            }
            reports.forEach {
                it.outputLocation.set(project.buildDir.resolve("reports/checkstyle/checkstyle.${it.name}"))
            }
            if (!configFile.exists()) {
                dependsOn("generateCheckstyleConfig")
            }
        }
        project.tasks.getByName(LintFeature.MAIN_TASK).dependsOn(TASK_NAME)
    }
}
