package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.buildDirAsFile
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.TargetJdk

private const val CONFIGURATION_NAME = "kradlePmd"
private const val DEFAULT_MINIMUM_PRIORITY = 5

class PmdBlueprint(project: Project) : Blueprint(project) {

    lateinit var pmdProperties: PmdProperties
    lateinit var codeAnalysisProperties: CodeAnalysisProperties
    lateinit var extendsTask: String

    override fun doCreateTasks() {
        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("${Catalog.Dependencies.Tools.pmd}:${pmdProperties.version}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val pmdTask = project.createHelperTask<Task>("pmd", "Runs PMD")
        project.tasks.getByName(extendsTask).dependsOn(pmdTask)

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        javaExtension.sourceSets.forEach { sourceSet ->
            val taskName = "pmd" + sourceSet.name[0].uppercaseChar() + sourceSet.name.substring(1)

            val enabledRuleSets = mutableListOf<String>()
            with(pmdProperties.ruleSets) {
                if (bestPractices) enabledRuleSets.add("category/java/bestpractices.xml")
                if (codeStyle) enabledRuleSets.add("category/java/codestyle.xml")
                if (design) enabledRuleSets.add("category/java/design.xml")
                if (documentation) enabledRuleSets.add("category/java/documentation.xml")
                if (errorProne) enabledRuleSets.add("category/java/errorprone.xml")
                if (multithreading) enabledRuleSets.add("category/java/multithreading.xml")
                if (performance) enabledRuleSets.add("category/java/performance.xml")
                if (security) enabledRuleSets.add("category/java/security.xml")
            }

            project.createHelperTask<Pmd>(taskName, "Runs PMD on '${sourceSet.name}'") {
                setSource(sourceSet.java.files)
                pmdClasspath = project.configurations.getAt(CONFIGURATION_NAME)
                classpath = project.objects.fileCollection().from(sourceSet.java.classesDirectory)
                reports {
                    html.required.set(true)
                    xml.required.set(false)
                }
                isConsoleOutput = true
                maxFailures.set(0)
                rulesMinimumPriority.set(DEFAULT_MINIMUM_PRIORITY)
                incrementalAnalysis.set(true)
                ruleSetFiles = project.files()
                ruleSets = enabledRuleSets
                targetJdk = TargetJdk.VERSION_1_7
                reports.forEach {
                    it.outputLocation.set(project.buildDirAsFile.resolve("reports/pmd/${sourceSet.name}.${it.name}"))
                }
                ignoreFailures = codeAnalysisProperties.ignoreFailures
                threads.set(1)
            }
            pmdTask.dependsOn(taskName)
        }
    }
}
