package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.Blueprint
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

    override fun createTasks() {
        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("${Catalog.Dependencies.Tools.pmd}:${pmdProperties.version.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val pmdTask = project.createHelperTask<Task>("pmd", "Runs PMD")
        project.tasks.getByName(CodeAnalysisFeature.MAIN_TASK).dependsOn(pmdTask)

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        javaExtension.sourceSets.forEach { sourceSet ->
            val taskName = "pmd" + sourceSet.name[0].toUpperCase() + sourceSet.name.substring(1)

            val enabledRuleSets = mutableListOf<String>()
            with(pmdProperties.ruleSets) {
                if (bestPractices.get()) enabledRuleSets.add("category/java/bestpractices.xml")
                if (codeStyle.get()) enabledRuleSets.add("category/java/codestyle.xml")
                if (design.get()) enabledRuleSets.add("category/java/design.xml")
                if (documentation.get()) enabledRuleSets.add("category/java/documentation.xml")
                if (errorProne.get()) enabledRuleSets.add("category/java/errorprone.xml")
                if (multithreading.get()) enabledRuleSets.add("category/java/multithreading.xml")
                if (performance.get()) enabledRuleSets.add("category/java/performance.xml")
                if (security.get()) enabledRuleSets.add("category/java/security.xml")
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
                ruleSetFiles = project.rootProject.files()
                ruleSets = enabledRuleSets
                targetJdk = TargetJdk.VERSION_1_7
                reports.forEach {
                    it.outputLocation.set(project.buildDir.resolve("reports/pmd/${sourceSet.name}.${it.name}"))
                }
                ignoreFailures = codeAnalysisProperties.ignoreFailures.get()
            }
            pmdTask.dependsOn(taskName)
        }
    }
}
