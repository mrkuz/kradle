package net.bnb1.kradle.tasks

import net.bnb1.kradle.featureRegistry
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GradleVersion
import java.nio.file.Paths
import java.util.*
import java.util.zip.ZipFile
import kotlin.reflect.KClass
import kotlin.streams.asSequence

open class KradleDumpTask : DefaultTask() {

    init {
        // Ensure that this task is always executed
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun run() {
        val properties = Properties()
        javaClass.getResource("/build.properties").openStream().use {
            properties.load(it)
        }
        val kradleVersion = properties["project.version"]
        dump(
            """
            kradle version: $kradleVersion"
            Gradle version: ${GradleVersion.current().version}
            """.trimIndent()
        )

        printEnabledFeatures()
        printAppliedPlugins()
        printTasks()
    }

    private fun printEnabledFeatures() {
        dump(
            """
            
            Enabled features:
            """.trimIndent()
        )

        project.featureRegistry.map.values.asSequence()
            .filter { it.isEnabled }
            .sortedBy { it::class.java.name }
            .forEach { dump("- $${it::class.java.name} (active: ${!it.isInactive})") }
    }

    private fun printTasks() {
        dump(
            """
           
            Tasks:
            """.trimIndent()
        )
        project.tasks.asSequence()
            .sortedBy { it.name }
            .forEach {
                val jar = getJar(it::class)
                dump("- ${it.name} ($jar, ${it::class.java.name})")
            }
    }

    private fun printAppliedPlugins() {
        dump(
            """
            
            Applied plugins:
            """.trimIndent()
        )

        val plugins = project.buildscript.configurations.asSequence()
            .flatMap { it.resolvedConfiguration.lenientConfiguration.artifacts }
            .filter { it.type == "jar" }
            .flatMap {
                ZipFile(it.file).use { zip ->
                    zip.stream().asSequence()
                        .filter { entry -> entry.name.startsWith("META-INF/gradle-plugins/") }
                        .filter { entry -> entry.name.endsWith(".properties") }
                        .map { entry -> entry.name.replace(Regex("^META-INF/gradle-plugins/"), "") }
                        .map { name -> name.replace(Regex("\\.properties$"), "") }
                        .map { id -> Pair(id, it.file.name) }
                        .toSet()
                }
            }
            .sortedBy { it.first }
            .toSet()

        val internal = setOf("application", "checkstyle", "jacoco", "java", "java-library", "maven-publish", "pmd")
        internal.forEach {
            if (project.pluginManager.hasPlugin(it)) {
                val plugin = project.plugins.getPlugin(it)
                var jar = getJar(plugin::class)
                dump("- $it ($jar, ${plugin::class.java.name})")
            }
        }

        plugins.forEach {
            if (project.pluginManager.hasPlugin(it.first)) {
                val plugin = project.plugins.getPlugin(it.first)
                dump("- ${it.first} (${it.second}, ${plugin::class.java.name})")
            }
        }
    }

    private fun getJar(clazz: KClass<*>) =
        Paths.get(clazz.java.protectionDomain.codeSource.location.toURI()).fileName

    private fun dump(text: String) {
        project.logger.lifecycle(text)
    }
}
