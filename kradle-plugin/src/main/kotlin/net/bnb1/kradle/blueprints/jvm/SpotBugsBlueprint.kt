package net.bnb1.kradle.blueprints.jvm

import com.github.spotbugs.snom.SpotBugsBasePlugin
import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsTask
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class SpotBugsBlueprint(project: Project) : Blueprint(project) {

    lateinit var spotBugsProperties: SpotBugsProperties
    lateinit var codeAnalysisProperties: CodeAnalysisProperties
    lateinit var extendsTask: String

    override fun doApplyPlugins() {
        project.apply(SpotBugsBasePlugin::class.java)
    }

    override fun doCreateTasks() {
        val spotbugsTask = project.createHelperTask<Task>("spotbugs", "Runs SpotBugs")
        project.tasks.getByName(extendsTask).dependsOn(spotbugsTask)

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
                    ignoreFailures = codeAnalysisProperties.ignoreFailures
                }
                spotbugsTask.dependsOn(taskName)
            }
    }

    override fun doAddDependencies() {
        project.dependencies {
            add("compileOnly", "${Catalog.Dependencies.Tools.findBugsAnnotations}:${Catalog.Versions.findBugs}")
            add("spotbugsSlf4j", "${Catalog.Dependencies.slf4jSimple}:${Catalog.Versions.slf4j}")
            spotBugsProperties.useFindSecBugs?.let {
                add("spotbugsPlugins", "${Catalog.Dependencies.Tools.findSecBugs}:$it")
            }
            spotBugsProperties.useFbContrib?.let {
                add("spotbugsPlugins", "${Catalog.Dependencies.Tools.fbContrib}:$it")
            }
        }
    }

    override fun doConfigure() {
        project.configure<SpotBugsExtension> {
            toolVersion.set(spotBugsProperties.version)
        }
    }
}
