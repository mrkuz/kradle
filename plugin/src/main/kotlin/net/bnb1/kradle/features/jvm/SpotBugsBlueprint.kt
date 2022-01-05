package net.bnb1.kradle.features.jvm

import com.github.spotbugs.snom.SpotBugsBasePlugin
import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsTask
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.apply
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class SpotBugsBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(SpotBugsBasePlugin::class.java)
    }

    override fun createTasks() {
        val codeAnalysisProperties = project.propertiesRegistry.get<CodeAnalysisProperties>()
        val spotbugsTask = project.createHelperTask<Task>("spotbugs", "Runs SpotBugs")
        project.tasks.getByName(CodeAnalysisFeature.MAIN_TASK).dependsOn(spotbugsTask)

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        javaExtension.sourceSets
            .filter { it.java.files.isNotEmpty() }
            .forEach { sourceSet ->
                val taskName = "spotbugs" + sourceSet.name[0].toUpperCase() + sourceSet.name.substring(1)
                project.createHelperTask<SpotBugsTask>(taskName, "Runs SpotBugs on '${sourceSet.name}'") {
                    sourceDirs = sourceSet.java.sourceDirectories
                    classes = project.objects.fileCollection().from(sourceSet.java.classesDirectory)
                    auxClassPaths = sourceSet.compileClasspath
                    reportsDir.set(project.buildDir.resolve("reports/spotbugs"))
                    reports.create("html") {
                        required.set(true)
                        outputLocation.set(project.buildDir.resolve("reports/spotbugs/${sourceSet.name}.html"))
                    }
                    ignoreFailures = codeAnalysisProperties.ignoreFailures.get()
                }
                spotbugsTask.dependsOn(taskName)
            }
    }

    override fun addDependencies() {
        val properties = project.propertiesRegistry.get<SpotBugsProperties>()
        project.dependencies {
            add("compileOnly", "${Catalog.Dependencies.Tools.findBugsAnnotations}:${Catalog.Versions.findBugs}")
            add("spotbugsSlf4j", "${Catalog.Dependencies.Tools.slf4jSimple}:${Catalog.Versions.slf4j}")
            if (properties.findSecBugs.hasValue) {
                add("spotbugsPlugins", "${Catalog.Dependencies.Tools.findSecBugs}:${properties.findSecBugs.get()}")
            }
            if (properties.fbContrib.hasValue) {
                add("spotbugsPlugins", "${Catalog.Dependencies.Tools.fbContrib}:${properties.fbContrib.get()}")
            }
        }
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<SpotBugsProperties>()
        project.configure<SpotBugsExtension> {
            toolVersion.set(properties.version.get())
        }
    }
}
